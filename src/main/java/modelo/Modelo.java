package modelo;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Modelo {
private ServerSocket serverSocket;
private Map<String, String[]> contactos = new HashMap<>();
private Map<String, Socket> conexionesActivas = new HashMap<>();
private Map<String, PrintWriter> flujosSalida = new HashMap<>();
private Map<String, List<String>> mensajes = new HashMap<>(); 
private MensajeListener mensajeListener;

    public interface MensajeListener {
        void onMensajeRecibido(String mensaje); 
    }

    public void setMensajeListener(MensajeListener listener) {
        this.mensajeListener = listener;
    }

    public boolean validarCredenciales(String usuario, int puerto){
        return !usuario.isEmpty() && puerto > 0;
    }

    public void iniciarServidor(int puerto) {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(puerto);
                System.out.println("Servidor iniciado en el puerto " + puerto);
                while (true) {
                    Socket clientSocket = serverSocket.accept(); // por cada cliente que se quiera conectar, le doy un socket
                    System.out.println("Nueva conexion desde " + clientSocket.getInetAddress());
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
            System.out.println("Agregado");
            return true;
        }
        return false;
    }

    public String[] obtenerDatosContacto(String nombre) {
        return contactos.get(nombre);
    }

    public List<String> getListaConexiones() {
        return new ArrayList<>(conexionesActivas.keySet()); // Devuelve solo los nombres de los contactos
    }

    public List<String> getListaContactos() {
        return new ArrayList<>(contactos.keySet()); // Devuelve solo los nombres de los contactos
    }

    public boolean conversacionActiva(String contacto) {
        return conexionesActivas.containsKey(contacto);
    }

    public void iniciarConexionCliente(String nombre, String ip, int puerto) {
        new Thread(() -> {
            try {
                Socket socket = new Socket(ip, puerto);
                conexionesActivas.put(nombre, socket);
                PrintWriter outputStream = new PrintWriter(socket.getOutputStream(), true); 
                BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String mensajeInicial = "Conexion establecida con " + nombre;
                outputStream.println(mensajeInicial);
                flujosSalida.put(nombre, outputStream);
                new Thread(new ClientHandler(socket)).start();
                System.out.println("Conectado con " + ip + ":" + puerto);
            } catch (IOException e) {
                System.err.println("Error al conectar con el contacto: " + e.getMessage());
            }
        }).start();
    }

    public void enviarMensaje(String contacto, String mensaje) {
        PrintWriter outputStream = flujosSalida.get(contacto);
        if (outputStream != null) {
            try {
                outputStream.println(mensaje); 
            } catch (Exception e) {
                System.err.println("Error al enviar mensaje: " + e.getMessage());
                cerrarConexion(contacto);
            }
        } else {
            System.err.println("No hay conexion activa con " + contacto);
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
            System.out.println("Conexion con " + contacto + " cerrada.");
        } catch (IOException e) {
            System.err.println("Error al cerrar conexion: " + e.getMessage());
        }
    }

    private class ClientHandler implements Runnable {
        private Socket socket;
        private BufferedReader inputStream;
        private PrintWriter outputStream;
        private String nombreCliente;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                outputStream = new PrintWriter(socket.getOutputStream(), true); 

                String mensajeInicial = inputStream.readLine();
                System.out.println("Mensaje recibido: " + mensajeInicial);
                nombreCliente = mensajeInicial;  // Esto ya no sirve asi
                if (!conversacionActiva(nombreCliente)) {
                    conexionesActivas.put(nombreCliente, socket);
                    flujosSalida.put(nombreCliente, outputStream);
                }
                System.out.println(nombreCliente + " se ha conectado desde " + socket.getInetAddress());
                if (mensajeListener != null) {
                    mensajeListener.onMensajeRecibido(mensajeInicial);
                }
                mensajes.computeIfAbsent(nombreCliente, k -> new ArrayList<>()).add(mensajeInicial);
                while (true) {
                    try {
                        String mensaje = inputStream.readLine();
                        if (mensaje == null) {
                            break;
                        }
                        System.out.println("Mensaje recibido dentro del while: " + mensaje);
                        if (mensajeListener != null) {
                            mensajeListener.onMensajeRecibido(mensaje);
                        }
                        mensajes.computeIfAbsent(nombreCliente, k -> new ArrayList<>()).add(mensaje);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Error en la recepción del mensaje: " + e.getMessage());
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();// PORQUE TE ROMPEEEEEES
                System.out.println("Error en la conexión con el cliente: " + e.getMessage());
            } finally {
                if (nombreCliente != null) { // cambiar esto
                    cerrarConexion(nombreCliente);
                }
            }
        }
    }


    public Map<String, List<String>> getMensajes() {
        return this.mensajes;
    }
}