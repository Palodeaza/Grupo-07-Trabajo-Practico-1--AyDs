package modelo;

import controlador.Controlador;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Modelo {
private ServerSocket serverSocket;
private ArrayList<Contacto> contactos = new ArrayList<>();
private Map<String, Socket> conexionesActivas = new HashMap<>();
private Map<String, PrintWriter> flujosSalida = new HashMap<>();
private Map<String, List<String>> mensajes = new HashMap<>(); 
private MensajeListener mensajeListener;
private Controlador controlador;

    public interface MensajeListener {
        void onMensajeRecibido(String mensaje); 
    }

    public void setControlador(Controlador controlador){
        this.controlador = controlador;
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
        for (Contacto c: contactos){
            if (c.getNombre().equalsIgnoreCase(nombre) || (c.getIp().equalsIgnoreCase(ip) && (c.getPuerto() == puerto))){
                return false;
            }
        }
        Contacto c = new Contacto(nombre,ip,puerto);
        contactos.add(c);
        return true;
    }

    public String[] obtenerDatosContacto(String contactoSeleccionado) {
        for (Contacto c : contactos) {
            if (c.getNombre().equalsIgnoreCase(contactoSeleccionado))
                return new String[]{c.getIp(), String.valueOf(c.getPuerto())};
            }
        return null;
    }

    public List<String> getListaConexiones() {
        return new ArrayList<>(conexionesActivas.keySet()); // Devuelve solo los nombres de los contactos
    }

    public List<Contacto> getListaContactos() {
        return this.contactos; // Devuelve solo los nombres de los contactos
    }

    public boolean conversacionActiva(String contacto) {
        return conexionesActivas.containsKey(contacto);
    }
    
    public String obtenerIPLocal() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "Desconocido";
        }
    }
    
    public String buscaContacto(String ip, String puerto) {
        for (Contacto contacto : this.contactos) {
            if ((contacto.getIp().equals(ip) || contacto.getIp().equals("localhost")) && String.valueOf(contacto.getPuerto()).equals(puerto)) {
                return contacto.getNombre();
            }
        }
        return null;
    }

    public void iniciarConexionCliente(String nombre, String ip, int puerto) {
        new Thread(() -> {
            try {
                Socket socket = new Socket(ip, puerto);                
                conexionesActivas.put(nombre, socket);
                controlador.refreshConversaciones();
                controlador.getInitView().getChatList().setSelectedValue(nombre, true);
                PrintWriter outputStream = new PrintWriter(socket.getOutputStream(), true); 
                BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
                String[] partes = mensajeInicial.split(";", 2);
                String[] datos = partes[0].split(":",2); //datos[0]=ip / datos[1]=puerto
                nombreCliente = buscaContacto(datos[0], datos[1]);
                if (!conversacionActiva(nombreCliente)) {
                    conexionesActivas.put(nombreCliente, socket);
                    flujosSalida.put(nombreCliente, outputStream);
                    controlador.refreshConversaciones();
                }
                System.out.println(nombreCliente + " se ha conectado desde " + socket.getInetAddress());
                mensajes.computeIfAbsent(nombreCliente, k -> new ArrayList<>()).add(mensajeInicial);
                if (mensajeListener != null) {
                    mensajeListener.onMensajeRecibido(mensajeInicial);
                }
                while (true) {
                    try {
                        String mensaje = inputStream.readLine();
                        if (mensaje == null) {
                            break;
                        }
                        System.out.println("Mensaje recibido dentro del while: " + mensaje);
                        mensajes.computeIfAbsent(nombreCliente, k -> new ArrayList<>()).add(mensaje);
                        if (mensajeListener != null) {
                            mensajeListener.onMensajeRecibido(mensaje);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Error en la recepción del mensaje: " + e.getMessage());
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error en la conexión con el cliente: " + e.getMessage());
            } finally {
                if (nombreCliente != null) { //cambiar esto
                    cerrarConexion(nombreCliente);
                }
            }
        }
    }


    public Map<String, List<String>> getMensajes() {
        return this.mensajes;
    }
}