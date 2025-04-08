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
    
    private ArrayList<Contacto> contactos = new ArrayList<>();
    private ArrayList<String> conexionesActivas = new ArrayList<>();
    private Map<String, List<String>> mensajes = new HashMap<>(); 
    private Controlador controlador;
    private Socket socket;
    private PrintWriter outputStream;

    public void setControlador(Controlador controlador){
        this.controlador = controlador;
    }
   
    public List<String> getListaConexiones() {
        return this.conexionesActivas; // Devuelve solo los nombres de los contactos
    }

    public Map<String, List<String>> getMensajes() {
        return this.mensajes;
    }
    
    public List<Contacto> getListaContactos() {
        return this.contactos; // Devuelve solo los nombres de los contactos
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
    
    public String buscaContacto(String ip, String puerto) {
        for (Contacto contacto : this.contactos) {
            if ((contacto.getIp().equals(ip) || contacto.getIp().equals("localhost")) && String.valueOf(contacto.getPuerto()).equals(puerto)) {
                return contacto.getNombre();
            }
        }
        return null;
    }
    
    public String[] obtenerDatosContacto(String contactoSeleccionado) {
        for (Contacto c : contactos) {
            if (c.getNombre().equalsIgnoreCase(contactoSeleccionado))
                return new String[]{c.getIp(), String.valueOf(c.getPuerto())};
            }
        return null;
    }

    public String obtenerIPLocal() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "Desconocido";
        }
    }

    public void usuarioOnline(String emisor) {
        try {
            this.socket = new Socket("localhost", 3333);               
            this.outputStream = new PrintWriter(socket.getOutputStream(), true); 
            outputStream.println(emisor);
            new Thread(new MessageHandler(socket)).start();
        } catch (IOException e) {
            System.err.println("Error al conectar: " + e.getMessage()); 
            controlador.mostrarCartelErrorConexion();
        }
    }

    public void iniciarConexionCliente(String nombre, String ip, int puerto, String emisor) {
        try {              
            conexionesActivas.add(nombre);
            controlador.refreshConversaciones();
            controlador.getInitView().getChatList().setSelectedValue(nombre, true);
        } catch (Exception e) {
            System.err.println("Error al conectar con el contacto: " + e.getMessage()); 
            controlador.mostrarCartelErrorConexion();
        }
    }

    public void enviarMensaje(String contacto, String mensaje) {
        if (this.outputStream != null) {
            try {
                this.outputStream.println(mensaje);
            } catch (Exception e) {
                System.err.println("Error al enviar mensaje: " + e.getMessage());
                cerrarConexion(contacto);
            }
        } else {
            System.err.println("No hay conexion activa con " + contacto);
        }
    }

    public void cerrarConexion(String contacto) { // metodo descontinuado
        try {
            if (conexionesActivas.contains(contacto)) {
                conexionesActivas.remove(contacto);
            }
            controlador.mostrarCartelErrorConexion();
            controlador.borraChat(contacto);
            controlador.refreshConversaciones();
        } catch (Exception e) {
            System.err.println("Error al cerrar conexion: " + e.getMessage());
        }
    }

    private class MessageHandler implements Runnable {
        private Socket socket;
        private BufferedReader inputStream;
        private PrintWriter outputStream;
        private String nombreCliente;

        public MessageHandler(Socket socket, String nombre) {
            this.socket = socket;
            this.nombreCliente = nombre;
        }
        public MessageHandler(Socket socket) {
            this.socket = socket;
        }
        @Override
        public void run() {
            try {
                inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                outputStream = new PrintWriter(socket.getOutputStream(), true); 
                String[] partes;
                String[] datos;
                while (true) {
                    try {
                        String mensaje = inputStream.readLine();
                        if (mensaje == null) {
                            break;
                        }
                        partes = mensaje.split(";", 4);
                        datos = partes[0].split(":", 3);
                        System.out.println("me llego mensaje de: " + datos[0]+" "+datos[1]);
                        nombreCliente = buscaContacto(datos[1], datos[2]);
                        System.out.println("Su nombre es: " + nombreCliente);
                        
                        if (nombreCliente==null){// si me llega mensaje desconocido, lo agendo                  
                            agregarContacto(datos[0],datos[1],Integer.parseInt(datos[2]));
                            controlador.actualizaListaContactos();
                            nombreCliente = datos[0];
                        }
                        if (!conexionesActivas.contains(nombreCliente)) {
                            conexionesActivas.add(nombreCliente);
                            controlador.refreshConversaciones();
                        }
                        
                        mensajes.computeIfAbsent(nombreCliente, k -> new ArrayList<>()).add(mensaje);
                        controlador.mostrarMensajeEnChat(mensaje);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.err.println("Error en la recepción del mensaje: " + e.getMessage());
                        cerrarConexion(nombreCliente); // Esto ya no deberia de cerrar la conexion de cliente, sino que deberia de cerrar la conexion con el server o quedarse esperando a que vuelva a abrirse
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error en la conexión con el servidor: " + e.getMessage());
                cerrarConexion(nombreCliente);
            }
        }
    }
}
