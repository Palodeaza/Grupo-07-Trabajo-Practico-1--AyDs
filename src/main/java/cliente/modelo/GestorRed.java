package modelo;

import cifrado.CifradoAES;
import cifrado.ContextoCifrado;
import cliente.modelo.ConfigLoader;
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

public class GestorRed implements IGestionRed{
    
    private int intentos;
    private Socket socket;
    
    private PrintWriter outputStream;
    private IGestionInterfaz controlador;
    private IGestionContactos gestorcontactos;
    private IGestionMensajes gestormensajes;
    private ArrayList<String> conexionesActivas = new ArrayList<>();
    private String usuario;
    private String servidorActivo;

    public GestorRed(IGestionInterfaz controlador, IGestionContactos gestorcontactos, IGestionMensajes gestormensajes) {
        this.controlador = controlador;
        this.gestorcontactos = gestorcontactos;
        this.gestormensajes = gestormensajes;
        this.intentos=0;
    }
    
    public GestorRed(IGestionContactos gestorcontactos, IGestionMensajes gestormensajes) {
        this.controlador = null; 
        this.gestorcontactos = gestorcontactos;
        this.gestormensajes = gestormensajes;
        this.intentos=0;
    }
    
    @Override
    public boolean isSocket(){
        return this.socket!=null;
    }

    public void setControlador(IGestionInterfaz controlador) {
        this.controlador = controlador;
    }
    
    @Override
    public void usuarioOnline(String emisor, String serverN){
        try {
            Socket socketMonitor = new Socket("localhost",1010);
            PrintWriter outMonitor = new PrintWriter(socketMonitor.getOutputStream(),true);
            BufferedReader inMonitor = new BufferedReader(new InputStreamReader(socketMonitor.getInputStream()));
            outMonitor.println("servidoractivo");
            String servidoractivo = inMonitor.readLine();
            System.out.println("AL conectar por primera vez, MONITOR ME DIJO QUE SERVIDOR PRINCIPAL ES " + servidoractivo);
            String ip =  ConfigLoader.getProperty("server.ip");
            int puerto;
            if (servidoractivo.equals("1")){
            puerto = Integer.parseInt(ConfigLoader.getProperty("server1.puerto"));
            }
            else{
                puerto = Integer.parseInt(ConfigLoader.getProperty("server2.puerto"));
            }            

            this.usuario = emisor;
            this.socket = new Socket(ip, puerto);  
            this.outputStream = new PrintWriter(socket.getOutputStream(), true); 
            outputStream.println(emisor);
            new Thread(new MessageHandler(socket,emisor)).start();
        } catch (IOException e) {
            System.err.println("Error al conectar: " + e.getMessage()); 
            System.err.println("Intentando reconectar al servidor alternativo...");
            if (intentos==0) {
                System.out.println("Reconexion exitosa papu");
                usuarioOnline(emisor,"server2");
            } else {
                this.intentos= this.intentos + 1;
                System.err.println("No se pudo conectar con el secundario.");
                controlador.mostrarCartelErrorConexion();
            }
        }
    }
    
    public boolean reconectarBackup() {
        try {
        Socket socketMonitor = new Socket("localhost",1010);
        PrintWriter outMonitor = new PrintWriter(socketMonitor.getOutputStream(),true);
        BufferedReader inMonitor = new BufferedReader(new InputStreamReader(socketMonitor.getInputStream()));
        outMonitor.println("servidoractivo");
        String nuevoServidor = inMonitor.readLine();
        System.out.println("AL RECONECTAR, MONITOR ME DIJO QUE SERVIDOR PRINCIPAL ES " + nuevoServidor);
        String ip =  ConfigLoader.getProperty("server.ip");
        int puerto = Integer.parseInt(ConfigLoader.getProperty("server" + nuevoServidor + ".puerto"));
        System.out.println("Intentando reconectar al servidor" + nuevoServidor);
        Socket nuevoSocket = new Socket(ip, puerto);
        this.socket = nuevoSocket;
        this.outputStream = new PrintWriter(nuevoSocket.getOutputStream(), true);
        this.outputStream.println(this.usuario);
        new Thread(new MessageHandler(nuevoSocket, this.usuario)).start();
        servidorActivo = nuevoServidor;
        return true;
        } catch (IOException e) {
            System.err.println("Fallo al reconectar con servidor" );
            return false;
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
        return this.conexionesActivas; 
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
                System.err.println("Intentando reconectar al servidor alternativo...");

                if (reconectarBackup()) {
                    System.out.println("Reconexion exitosa papu, reenviando el mensaje...");
                    enviarMensaje(contacto, mensaje);
                } else {
                    System.err.println("No se pudo reconectar.");
                }
            }
        } else {
            System.err.println("No hay conexión activa con " + contacto);
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
                            datos = mensaje.split("/",2)[1].split(":",3);
                            if (datos[0].equals("null")){
                                controlador.mostrarCartelErrorDir();
                            }
                            else{
                                if (gestorcontactos.agregarContacto(datos[0]))
                                    controlador.agregadoExitoso();
                                else 
                                    controlador.agregadoRepetido();
                                }
                        }
                        else { //me mandaron mensaje de texto
                            mensaje = mensaje.split("/", 2)[1]; // me quedo con todo menos operación
                            partes = mensaje.split(";", 4);

                            if (partes.length == 4) { 
                                datos = partes[0].split(":", 3);
                                System.out.println("me llegó mensaje de: " + datos[0] + " " + datos[1]);
                                try {
                                    System.out.println("Mensaje cifrado: " + partes[1]);
                                    String mensajeDescifrado = controlador.getContextocifrado().descifrarMensaje(partes[1], controlador.getContextocifrado().crearClave(ConfigLoader.getProperty("clave")));
                                    partes[1] = mensajeDescifrado; 
                                    System.out.println("Mensaje descifrado: " + partes[1]);
                                    mensaje = partes[0] + ";" + partes[1] + ";" + partes[2] + ";" + partes[3];
                                    //hay q hacer la clase mensaje....
                                } catch (Exception e) {
                                    System.err.println("Error al descifrar mensaje: " + e.getMessage());
                                    return; 
                                }

                                nombreCliente = gestorcontactos.buscaContacto(datos[0]);
                                if (nombreCliente == null) {                 
                                    gestorcontactos.agregarContacto(datos[0]);
                                    controlador.actualizaListaContactos();
                                    nombreCliente = datos[0];
                                }

                                if (!conexionesActivas.contains(nombreCliente)) {
                                    conexionesActivas.add(nombreCliente);
                                    controlador.refreshConversaciones();
                                }

                                gestormensajes.agregaMensaje(nombreCliente, mensaje);
                                controlador.mostrarMensajeEnChat("texto/" + mensaje);  
                            }
                        }
                    } catch (UsuarioDuplicadoException e) {
                        System.err.println("Usuario duplicado: " + e.getMessage());
                        controlador.mostrarCartelErrorUsuarioConectado();
                        break;
                    } catch (NullPointerException | IOException e) {
                        e.printStackTrace();
                        if (reconectarBackup()) {
                            System.out.println("Reconexion exitosa al servidor alternativo.");
                            return; // el nuevo MessageHandler ya esta corriendo
                        } else {
                            System.err.println("No se pudo reconectar al servidor alternativo.");
                            cerrarConexion();
                        }
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
