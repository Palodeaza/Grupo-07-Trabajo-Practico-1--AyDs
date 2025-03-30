package modelo;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Modelo {
    private ServerSocket serverSocket;
    private Map<String, String[]> contactos = new HashMap<>();
    private Map<String, Socket> conexionesActivas = new HashMap<>();
    private Map<String, ObjectOutputStream> flujosSalida = new HashMap<>();
    private Map<String, List<Mensaje>> mensajes = new HashMap<>();//clave: nombre contacto/valor: lista de mensajes
    private MensajeListener mensajeListener; 

    public interface MensajeListener {
        void onMensajeRecibido(Mensaje mensaje);
    }

    public void setMensajeListener(MensajeListener listener) {
        this.mensajeListener = listener;
    }
    public boolean validarCredenciales(String usuario, int puerto) {
        return !usuario.isEmpty() && puerto > 0;
    }

    public void iniciarServidor(int puerto) {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(puerto);
                System.out.println("Servidor iniciado en el puerto " + puerto);
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Nueva conexión desde " + clientSocket.getInetAddress());
                    new Thread(new ClientHandler(clientSocket)).start();
                }
            } catch (IOException e) {
                System.err.println("Error en el servidor: " + e.getMessage());
            }
        }).start();
    }

    public boolean agregarContacto(String nombre, String ip, int puerto) {
        if (!contactos.containsKey(nombre)) {
            contactos.put(nombre, new String[]{ip, String.valueOf(puerto)});
            return true;
        }
        return false;
    }

    public String[] obtenerDatosContacto(String nombre) {
        return contactos.get(nombre);
    }

    public boolean conversacionActiva(String contacto) {
        return conexionesActivas.containsKey(contacto);
    }

    public void iniciarConexionCliente(String nombre, String ip, int puerto) {
        new Thread(() -> {
            try {
                Socket socket = new Socket(ip, puerto);
                conexionesActivas.put(nombre, socket);
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                flujosSalida.put(nombre, outputStream);
                new Thread(new ClientHandler(socket)).start();
                System.out.println("Conectado con " + ip + ":" + puerto);
            } catch (IOException e) {
                System.err.println("Error al conectar con el contacto: " + e.getMessage());
            }
        }).start();
    }

    public void enviarMensaje(String contacto, Object mensaje) {
        ObjectOutputStream outputStream = flujosSalida.get(contacto);
        if (outputStream != null) {
            try {
                outputStream.writeObject(mensaje);
                outputStream.flush();
            } catch (IOException e) {
                System.err.println("Error al enviar mensaje: " + e.getMessage());
                cerrarConexion(contacto);
            }
        } else {
            System.err.println("No hay conexión activa con " + contacto);
        }
    }

    public void cerrarConexion(String contacto) {
        try {
            if (flujosSalida.containsKey(contacto)) {
                flujosSalida.get(contacto).close();
                flujosSalida.remove(contacto);
            }
            if (conexionesActivas.containsKey(contacto)) {
                conexionesActivas.get(contacto).close();
                conexionesActivas.remove(contacto);
            }
            System.out.println("Conexión con " + contacto + " cerrada.");
        } catch (IOException e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
        }
    }

    private class ClientHandler implements Runnable { // tarea para otro dia... meterlo en otro archivo tomson vago de los cojones
        private Socket socket;
        private String nombreCliente;
        
        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                Object mensaje = inputStream.readObject();
                if (mensaje instanceof Mensaje) { //manejo de errores viejo..
                    Mensaje mensajeRecibido = (Mensaje) mensaje;
                    nombreCliente = mensajeRecibido.getEmisor();
                    if (!conversacionActiva(nombreCliente)) {
                        conexionesActivas.put(nombreCliente, socket);
                        flujosSalida.put(nombreCliente, outputStream);
                    }
                    System.out.println(nombreCliente + " se ha conectado desde " + socket.getInetAddress());
                    if (mensajeListener != null) {
                        mensajeListener.onMensajeRecibido(mensajeRecibido);
                    }
                    mensajes.computeIfAbsent(nombreCliente, k -> new ArrayList<>()).add(mensajeRecibido);
                }
                while (true) {
                    mensaje = inputStream.readObject();
                    if (mensaje instanceof Mensaje) {
                        Mensaje mensajeRecibido = (Mensaje) mensaje;
                        if (mensajeListener != null) {
                            mensajeListener.onMensajeRecibido(mensajeRecibido);
                        }
                        mensajes.computeIfAbsent(nombreCliente, k -> new ArrayList<>()).add(mensajeRecibido);//agrega la lista si no existe
                    } else {
                        System.err.println("Objeto recibido no es un mensaje válido.");
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Cliente desconectado: " + e.getMessage());
            } finally {
                if (nombreCliente != null) {
                    cerrarConexion(nombreCliente);
                }
            }
        }
    }
    public Map<String, List<Mensaje>> getMensajes(){
        return this.mensajes;
    }
}