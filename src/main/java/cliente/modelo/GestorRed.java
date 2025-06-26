package cliente.modelo;

import cifrado.CifradoAES;
import cifrado.ContextoCifrado;
import cliente.modelo.ConfigLoader;
import cliente.modelo.IGestionReconexion;
import cliente.modelo.UsuarioDuplicadoException;
//import main.java.cliente.modelo.IMensaje;
import cliente.modelo.IMensaje;
import cliente.controlador.IGestionInterfaz;
import cliente.modelo.Mensaje;

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
    private IGestionReconexion gestorReconexion;
    private ArrayList<String> conexionesActivas = new ArrayList<>();
    private String usuario;
    private String servidorActivo;

    public GestorRed(IGestionInterfaz controlador, IGestionContactos gestorcontactos, IGestionMensajes gestormensajes, IGestionReconexion gestorReconexion) {
        this.controlador = controlador;
        this.gestorcontactos = gestorcontactos;
        this.gestormensajes = gestormensajes;
        this.gestorReconexion = gestorReconexion;
        this.intentos=0;
    }
    
    public GestorRed(IGestionContactos gestorcontactos, IGestionMensajes gestormensajes, IGestionReconexion gestorReconexion) {
        this.controlador = null; 
        this.gestorcontactos = gestorcontactos;
        this.gestormensajes = gestormensajes;
        this.gestorReconexion = gestorReconexion;
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
    public void iniciarMessageHandler(Socket socket, String usuario) {
        new Thread(new MessageHandler(socket, usuario)).start();
    }

    public boolean reconectarBackup(){
        return gestorReconexion.reconectarBackup(this);
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
    public void enviarMensaje(IMensaje mensaje) { //String contacto, String mensaje
        if (this.outputStream != null) {
            try {
                this.outputStream.println(mensaje.getOutputString());

            } catch (Exception e) {
                System.err.println("Error al enviar mensaje: " + e.getMessage());
                System.err.println("Intentando reconectar al servidor alternativo...");

                if (reconectarBackup()) {
                    enviarMensaje(mensaje);
                } else {
                    System.err.println("No se pudo reconectar.");
                }
            }
        } else {
            System.err.println("No hay conexión activa con " + mensaje.getReceptor());
        }
    }
    
    public class MessageHandler implements Runnable {
        private Socket socket;
        private BufferedReader inputStream;
        private PrintWriter outputStream;
        private String nombreCliente;
        private String nombreUsuario;
        
        public MessageHandler(Socket socket, String nombre) {
            this.socket = socket;
            this.nombreUsuario = nombre;

        }
        
        public MessageHandler(Socket socket) {
            this.socket = socket;
        }
        
        @Override
        public void run() {
            try {
                inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                outputStream = new PrintWriter(socket.getOutputStream(), true); 
                while (true) {
                    try {
                        String mensaje = inputStream.readLine();

                        IMensaje mensajeObjeto = FabricaMensajes.getInstancia().creaMensaje();
                        mensajeObjeto.setConOutputString(mensaje);

                        if (mensajeObjeto.getTipo().equals("dupe")){
                            throw new UsuarioDuplicadoException("Usuario duplicado detectado.");
                        } 
                        if (mensajeObjeto.getTipo().equals("dir")){ 
                            if (mensajeObjeto.getNombreEmisor().equals("null")){
                                controlador.mostrarCartelErrorDir();
                            }
                            else{
                                if (gestorcontactos.agregarContacto(mensajeObjeto.getNombreEmisor()))
                                    controlador.agregadoExitoso();
                                else 
                                    controlador.agregadoRepetido();
                                }
                        }
                        else { //me mandaron mensaje de texto
                            try {
                                    System.out.println("Mensaje cifrado: " + mensajeObjeto.getMensaje());
                                    String mensajeDescifrado = controlador.getContextocifrado().descifrarMensaje(mensajeObjeto.getMensaje(), controlador.getContextocifrado().crearClave(ConfigLoader.getProperty(this.nombreUsuario,"clave")));
                                    mensajeObjeto.setMensaje(mensajeDescifrado);
                                    System.out.println("Mensaje descifrado: " + mensajeObjeto.getMensaje());
                                } catch (Exception e) {
                                    System.err.println("Error al descifrar mensaje: " + e.getMessage());
                                    return; 
                                }

                            nombreCliente = gestorcontactos.buscaContacto(mensajeObjeto.getNombreEmisor());
                            if (nombreCliente == null) {                 
                                gestorcontactos.agregarContacto(mensajeObjeto.getNombreEmisor());
                                controlador.actualizaListaContactos();
                                nombreCliente = mensajeObjeto.getNombreEmisor();
                            }

                            if (!conexionesActivas.contains(nombreCliente)) {
                                conexionesActivas.add(nombreCliente);
                                controlador.refreshConversaciones();
                            }

                            mensajeObjeto.setTipo("texto");
                            gestormensajes.agregaMensaje(nombreCliente ,mensajeObjeto);
                            controlador.mostrarMensajeEnChat(mensajeObjeto);  
                        
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

    /**
     * @param conexionesActivas the conexionesActivas to set
     */
    public void setConexionesActivas(ArrayList<String> conexionesActivas) {
        this.conexionesActivas = conexionesActivas;
    }

    public int getIntentos() {
        return intentos;
    }

    public void setIntentos(int intentos) {
        this.intentos = intentos;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public PrintWriter getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(PrintWriter outputStream) {
        this.outputStream = outputStream;
    }

    public IGestionInterfaz getControlador() {
        return controlador;
    }

    public IGestionContactos getGestorcontactos() {
        return gestorcontactos;
    }

    public void setGestorcontactos(IGestionContactos gestorcontactos) {
        this.gestorcontactos = gestorcontactos;
    }

    public IGestionMensajes getGestormensajes() {
        return gestormensajes;
    }

    public void setGestormensajes(IGestionMensajes gestormensajes) {
        this.gestormensajes = gestormensajes;
    }

    public IGestionReconexion getGestorReconexion() {
        return gestorReconexion;
    }

    public void setGestorReconexion(IGestionReconexion gestorReconexion) {
        this.gestorReconexion = gestorReconexion;
    }

    public ArrayList<String> getConexionesActivas() {
        return conexionesActivas;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getServidorActivo() {
        return servidorActivo;
    }

    public void setServidorActivo(String servidorActivo) {
        this.servidorActivo = servidorActivo;
    }
}
