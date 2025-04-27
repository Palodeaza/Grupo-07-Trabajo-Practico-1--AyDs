package modelo;

import controlador.GestorInterfaz;
import controlador.IGestionInterfaz;
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
    
    //private ArrayList<Contacto> contactos = new ArrayList<>();
    //private ArrayList<String> conexionesActivas = new ArrayList<>();
    //private Map<String, List<String>> mensajes = new HashMap<>(); 
    //private IGestionInterfaz controlador;
    //private Socket socket;
    //private PrintWriter outputStream;
    //private IGestionContactos gestorcontactos = new GestorContactos();
    //private IGestionMensajes gestormensajes = new GestorMensajes();
    //private IGestionRed gestored;

    /*public void setControlador(IGestionInterfaz controlador){
        this.controlador = controlador;
    }*/
    
    /*public void setGestorRed(){
        gestored = new GestorRed(controlador, gestorcontactos, gestormensajes);

    }*/
    
    //public IGestionContactos getGestorContactos(){
    //    return this.gestorcontactos;
    //}

    //public IGestionMensajes getGestormensajes() {
    //    return gestormensajes;
    //}

    //public IGestionRed getGestored() {
    //    return gestored;
    //}
   
   /* public List<String> getListaConexiones() {
        return this.conexionesActivas; // Devuelve solo los nombres de los contactos
    }
   */
   /* public Map<String, List<String>> getMensajes() {
        return this.mensajes;
    }
    */
    /*public List<Contacto> getListaContactos() {
        return this.contactos; // Devuelve solo los nombres de los contactos
    }
    */
    
}
/*public boolean validarCredenciales(String usuario, int puerto) {
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
} */

    /*public boolean agregarContacto(String nombre) {
        for (Contacto c: contactos){
            if (c.getNombre().equalsIgnoreCase(nombre)){
                return false;
            }
        }
        Contacto c = new Contacto(nombre,"localhost",3333);
        contactos.add(c);
        return true;
    }
    */
    /*
    public String buscaContacto(String ip, String puerto) {
        for (Contacto contacto : this.contactos) {
            if ((contacto.getIp().equals(ip) || contacto.getIp().equals("localhost")) && String.valueOf(contacto.getPuerto()).equals(puerto)) {
                return contacto.getNombre();
            }
        }
        return null;
    }
    */
    /*public String[] obtenerDatosContacto(String contactoSeleccionado) {
        for (Contacto c : contactos) {
            if (c.getNombre().equalsIgnoreCase(contactoSeleccionado))
                return new String[]{c.getIp(), String.valueOf(c.getPuerto())};
            }
        return null;
    }
    */
    
    //public String obtenerIPLocal() {
       // try {
         //   return InetAddress.getLocalHost().getHostAddress();
       // } catch (UnknownHostException e) {
       //     e.printStackTrace();
      //      return "Desconocido";
     //   }
   // }
    
    /*public void usuarioOnline(String emisor) {
        try {
            this.socket = new Socket("localhost", 3333);               
            this.outputStream = new PrintWriter(socket.getOutputStream(), true); 
            outputStream.println(emisor);
            new Thread(new MessageHandler(socket,emisor)).start();
        } catch (IOException e) {
            System.err.println("Error al conectar: " + e.getMessage()); 
            controlador.mostrarCartelErrorConexion();
        }
    }
*/
   /* public void iniciarConexionCliente(String nombre, String ip, int puerto, String emisor) {
        try {              
            conexionesActivas.add(nombre);
            controlador.refreshConversaciones();
            controlador.getInitView().getChatList().setSelectedValue(nombre, true);
        } catch (Exception e) {
            System.err.println("Error al conectar con el contacto: " + e.getMessage()); 
            controlador.mostrarCartelErrorConexion();
        }
    }
*/
   /* public void enviarMensaje(String contacto, String mensaje) { // quien hace esto ?
        if (this.outputStream != null) {
            try {
                this.outputStream.println(mensaje);
            } catch (Exception e) {
                System.err.println("Error al enviar mensaje: " + e.getMessage());
                gestored.cerrarConexion(contacto);
            }
        } else {
            System.err.println("No hay conexion activa con " + contacto);
        }
    }
}*/
  /*  public void cerrarConexion(String contacto) { // metodo descontinuado
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
    */
    /*
    public void checkDir(Contacto contacto) {
            this.outputStream.println("dir/" + contacto.getNombre());
    }
*/
  /*  public class MessageHandler implements Runnable {
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
                        System.out.println(" SOY " + nombreCliente + " y me llego ->  " + mensaje);
                        String operacion = mensaje.split("/",2)[0];
                        if (operacion.equals("dir")){ 
                            datos = mensaje.split("/",2)[1].split(":",3); // el dir llega de la forma dir/ Juan:ip:puerto si lo encontro, todo null si no
                            if (datos[0].equals("null")){
                                controlador.mostrarCartelErrorDir();
                            }
                            else{
                                    if (gestorcontactos.agregarContacto(datos[0]))//deberia poder pasarle otras cosas...
                                        controlador.agregadoExitoso();
                                    else // esto queda horrible, nose que se les ocurre para hacer esto en el controlador
                                        controlador.agregadoRepetido();
                                }
                        }
                        else{ //me mandaron mensaje de texto
                        mensaje = mensaje.split("/",2)[1]; // me quedo con todo menos operacion
                        partes = mensaje.split(";", 4);
                        if (partes.length == 4){ 
                            datos = partes[0].split(":", 3);
                            System.out.println("me llego mensaje de: " + datos[0]+" "+datos[1]);
                            nombreCliente = gestorcontactos.buscaContacto(datos[1], datos[2]);
                            System.out.println("Su nombre es: " + nombreCliente);
                            if (nombreCliente==null){// si me llega mensaje desconocido, lo agendo                  
                                gestorcontactos.agregarContacto(datos[0]);
                                controlador.actualizaListaContactos();
                                nombreCliente = datos[0];
                                System.out.println("No lo habia encontrado, lo agregue y su nombre es: " + nombreCliente);
                            }
                            if (!conexionesActivas.contains(nombreCliente)) {
                                conexionesActivas.add(nombreCliente);
                                controlador.refreshConversaciones();
                            }
                            mensajes.computeIfAbsent(nombreCliente, k -> new ArrayList<>()).add(mensaje);
                            System.out.println("LE VOY A DECIR A CONTROLADOR QUE MUESTRE MENSAJE, LE PASO-> " + mensaje);
                            controlador.mostrarMensajeEnChat(mensaje);  
                        }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.err.println("Error en la recepción del mensaje: " + e.getMessage());
                        //cerrarConexion(nombreCliente); // Esto ya no deberia de cerrar la conexion de cliente, sino que deberia de cerrar la conexion con el server o quedarse esperando a que vuelva a abrirse
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
*/