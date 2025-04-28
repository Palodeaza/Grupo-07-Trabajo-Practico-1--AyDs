/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import cliente.modelo.UsuarioDuplicadoException;
import controlador.IGestionInterfaz;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author felis
 */
public class GestorRed implements IGestionRed{
    
    private Socket socket;
    private PrintWriter outputStream;
    private IGestionInterfaz controlador;
    private IGestionContactos gestorcontactos;
    private IGestionMensajes gestormensajes;
    private ArrayList<String> conexionesActivas = new ArrayList<>();

    public GestorRed(IGestionInterfaz controlador, IGestionContactos gestorcontactos, IGestionMensajes gestormensajes) {
        this.controlador = controlador;
        this.gestorcontactos = gestorcontactos;
        this.gestormensajes = gestormensajes;
    }
    public GestorRed(IGestionContactos gestorcontactos, IGestionMensajes gestormensajes) {
        this.controlador=null; //lo debe setear luego
        this.gestorcontactos = gestorcontactos;
        this.gestormensajes = gestormensajes;
    }
    @Override
    public boolean isSocket(){
        return this.socket!=null;
    }

    public void setControlador(IGestionInterfaz controlador) {
        this.controlador = controlador;
    }
    
    
    @Override
    public void usuarioOnline(String emisor){
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

    @Override
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
    
    @Override
    public void cerrarConexion() {
        try {
            this.socket=null;
            conexionesActivas.clear();
            controlador.mostrarCartelErrorConexion();
            controlador.refreshConversaciones();
            controlador.borraChat();
        } catch (Exception e) {
            System.err.println("Error al cerrar conexiones: " + e.getMessage());
        }
    }

    @Override
    public void checkDir(Contacto contacto) {
        
        this.outputStream.println("dir/" + contacto.getNombre());

    }

    @Override
    public List<String> getListaConexiones() {
        
        return this.conexionesActivas; // Devuelve solo los nombres de los contactos
    }

    @Override
    public boolean estaConectado(String conexion) {
        return conexionesActivas.contains(conexion);
    }

    @Override
    public String obtenerIPLocal() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "Desconocido";
        }
    }

    @Override
    public void enviarMensaje(String contacto, String mensaje) {
        if (this.outputStream != null) {
            try {
                this.outputStream.println(mensaje);
            } catch (Exception e) {
                System.err.println("Error al enviar mensaje: " + e.getMessage());
            }
        } else {
            System.err.println("No hay conexion activa con " + contacto);
        }
    }
    
        public class MessageHandler implements Runnable {
            
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

                        System.out.println(" SOY " + nombreCliente + " y me llego ->  " + mensaje);
                        String operacion = mensaje.split("/",2)[0];
                        if (operacion.equals("dupe")){
                            throw new UsuarioDuplicadoException("Usuario duplicado detectado.");
                        } 
                        if (operacion.equals("dir")){ 
                            datos = mensaje.split("/",2)[1].split(":",3); // el dir llega de la forma dir/ Juan:ip:puerto si lo encontro, todo null si no
                            if (datos[0].equals("null")){
                                controlador.mostrarCartelErrorDir();
                            }
                            else{
                                    if (gestorcontactos.agregarContacto(datos[0]))//deberia poder pasarle otras cosas...
                                        controlador.agregadoExitoso();
                                    else 
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
                            }
                            if (!conexionesActivas.contains(nombreCliente)) {
                                conexionesActivas.add(nombreCliente);
                                controlador.refreshConversaciones();
                            }
                            gestormensajes.agregaMensaje(nombreCliente,mensaje);
                            controlador.mostrarMensajeEnChat(mensaje);  
                        }
                        }
                    } catch (UsuarioDuplicadoException e) {
                        System.err.println("Usuario duplicado: " + e.getMessage());
                        controlador.mostrarCartelErrorUsuarioConectado();
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.err.println("Error en la recepción del mensaje: " + e.getMessage());
                        cerrarConexion();
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error en la conexión con el servidor: " + e.getMessage());
            }
        }
    }
}
