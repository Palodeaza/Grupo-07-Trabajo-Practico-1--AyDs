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
import javax.swing.JOptionPane;

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

    public boolean validarCredenciales(String usuario, int puerto) {
        if (usuario.isEmpty() || puerto <= 0) {
            return false;
        }

        ServerSocket testSocket = null;
        try {
            testSocket = new ServerSocket(puerto);
            return true; // Puerto disponible
        } catch (IOException e) {
            return false; // Puerto en uso
        } finally {
            if (testSocket != null) {
                try {
                    testSocket.close();
                } catch (IOException ignored) {}
            }
        }
    }

    public void iniciarServidor(int puerto) {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(puerto);
                while (true) {
                    Socket clientSocket = serverSocket.accept(); // por cada cliente que se quiera conectar, le doy un socket
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
                new Thread(new ClientHandler(socket, nombre)).start();
            } catch (IOException e) {
                System.err.println("Error al conectar con el contacto: " + e.getMessage()); 
                controlador.mostrarCartelErrorConexion();
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
            controlador.mostrarCartelErrorConexion();
            controlador.borraChat(contacto);
            controlador.refreshConversaciones();
        } catch (IOException e) {
            System.err.println("Error al cerrar conexion: " + e.getMessage());
        }
    }

    private class ClientHandler implements Runnable {
        private Socket socket;
        private BufferedReader inputStream;
        private PrintWriter outputStream;
        private String nombreCliente;

        public ClientHandler(Socket socket, String nombre) {
            this.socket = socket;
            this.nombreCliente = nombre;
        }
        public ClientHandler(Socket socket) {
            this.socket = socket;
        }
        @Override
        public void run() {
            try {
                inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                outputStream = new PrintWriter(socket.getOutputStream(), true); 

                String mensajeInicial = inputStream.readLine();
                String[] partes = mensajeInicial.split(";", 3);
                String[] datos = partes[0].split(":",3); //datos[0]=nombre / datos[1]=ip / datos[2]=puerto
                nombreCliente = buscaContacto(datos[1], datos[2]);
                if (nombreCliente==null){// si me llega mensaje desconocido, lo agendo                  
                    agregarContacto(datos[0],datos[1],Integer.parseInt(datos[2]));
                    controlador.actualizaListaContactos();
                    nombreCliente = datos[0];
                }
                if (!conversacionActiva(nombreCliente)) {
                    conexionesActivas.put(nombreCliente, socket);
                    flujosSalida.put(nombreCliente, outputStream);
                    controlador.refreshConversaciones();
                }
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
                        mensajes.computeIfAbsent(nombreCliente, k -> new ArrayList<>()).add(mensaje);
                        if (mensajeListener != null) {
                            mensajeListener.onMensajeRecibido(mensaje);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.err.println("Error en la recepción del mensaje: " + e.getMessage());
                        cerrarConexion(nombreCliente);
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error en la conexión con el cliente: " + e.getMessage());
                cerrarConexion(nombreCliente);
            } finally {
                if (nombreCliente != null) { 
                    //System.out.println("CIERRO CONEXION"); NO ERA NECESARIO ACA ???
                    //cerrarConexion(nombreCliente);
                }
            }
        }
    }


    public Map<String, List<String>> getMensajes() {
        return this.mensajes;
    }
}