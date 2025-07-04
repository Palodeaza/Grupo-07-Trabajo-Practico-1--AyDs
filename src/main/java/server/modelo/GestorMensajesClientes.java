package server.modelo;

import server.modelo.Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import cliente.modelo.Contacto;

public class GestorMensajesClientes implements Runnable{

    private Socket socket;
    private BufferedReader inputStream;
    private PrintWriter outputStream;
    private String user;
    private Server servidor;
    public static ArrayList<GestorMensajesClientes> clientHandlers = new ArrayList<>();
    
    public GestorMensajesClientes(Socket socket, String nombre, Server servidor){
        try {
            this.socket = socket;
            this.inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.outputStream = new PrintWriter(socket.getOutputStream(), true); 
            this.user = nombre;
            this.servidor= servidor;
            GestorMensajesClientes.clientHandlers.add(this);
            System.out.println("Agregue a " + this.toString() + " a ClientHandlers");
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    

    @Override
    public void run() {
        String mensaje;
        while (socket.isConnected()){
            try{
                mensaje = inputStream.readLine();
                System.out.println("[ClientHandler] Me llego el mensaje: "+mensaje);
                if (mensaje==null){
                    cierraConexion(socket, inputStream, outputStream);
                    break;
                }
                String operacion = mensaje.split("/",2)[0];
                        if (operacion.equals("dir")){
                            String nombre= mensaje.split("/",2)[1]; // yo chequeo si esta el nombre y despues les aviso sus datos
                            Contacto contacto = servidor.buscaDirContacto(nombre);
                            if (contacto == null)
                            outputStream.println("dir/"+ "null" + ":" + "null" + ":" + "null"); 
                            else
                            outputStream.println("dir/"+ contacto.getNombre() + ":" + contacto.getIp() + ":" + contacto.getPuerto());
                            }
                        else {
                            mensaje = mensaje.split("/",2)[1];
                            String[] partes = mensaje.split(";", 4);
                            String[] datos = partes[0].split(":", 3); //datos[0]=nombre / datos[1]= ip / datos[2] = puerto
                            boolean esta = usuarioEstaOnline(partes[3]);
                            if (esta)
                                enviaMensaje(partes[3], mensaje); 
                            else{
                                System.out.println("bien 2");
                                servidor.guardaMensaje(partes[3], mensaje); //receptor y mensaje
                                if (servidor.getOutputStreamSecundario()!=null)
                                    servidor.getOutputStreamSecundario().println("msjguardar/"+ partes[3] + ";" + mensaje);
                            } 
                        }
            }
            catch(IOException e){
                cierraConexion(socket, inputStream, outputStream); // aca ejecuta cuando se desconecta un cliente
                e.printStackTrace();
                break;
            }
        }
    }

    private boolean usuarioEstaOnline(String contacto){
        for (GestorMensajesClientes c : GestorMensajesClientes.clientHandlers){
            if (c.getUser().equals(contacto)){
                return true;
            }
        }
        return false;
    }

    private void enviaMensaje(String receptor, String mensaje) {
        System.out.println("Receptor: "+receptor+" Mensaje: "+mensaje);
        for (GestorMensajesClientes c : GestorMensajesClientes.clientHandlers) {
            try {
                System.out.println("[ClientHandler]: Nombre de cliente:"+c.user);
                if (c.user.equalsIgnoreCase(receptor)){
                    c.outputStream.println("texto/" + mensaje); 
                }   
            }
            catch(Exception e){//este catch funciona?
                System.err.println("Error al enviar mensaje: " + e.getMessage());
                cierraConexion(socket, inputStream, outputStream); 
            }
        }
    }

    private void cierraConexion(Socket socket1, BufferedReader inputStream1, PrintWriter outputStream1) {
        System.out.println(this.user +" se desconecto ");
        clientHandlers.remove(this);
        System.out.println("ClientHandlers:"+ GestorMensajesClientes.clientHandlers);
        try {
            if (socket1!=null){
                socket.close();
            }
            if (inputStream1!=null){
                inputStream1.close();
            }
            if (outputStream1!=null){
                outputStream1.close();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
        public Socket getSocket() {
        return socket;
    }

    public BufferedReader getInputStream() {
        return inputStream;
    }

    public PrintWriter getOutputStream() {
        return outputStream;
    }

    public String getUser() {
        return user;
    }

    public Server getServidor() {
        return servidor;
    }
}
