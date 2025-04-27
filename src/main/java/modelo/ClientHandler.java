package modelo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

    private Socket socket;
    private BufferedReader inputStream;
    private PrintWriter outputStream;
    private String user;
    private Server servidor;
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    
    public ClientHandler(Socket socket, String nombre, Server servidor){
        try {
            this.socket = socket;
            this.inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.outputStream = new PrintWriter(socket.getOutputStream(), true); 
            this.user = nombre;
            this.servidor= servidor;
            ClientHandler.clientHandlers.add(this);
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
                            Contacto contacto = buscaDir(nombre);
                            if (contacto == null)
                            outputStream.println("dir/"+ "null" + ":" + "null" + ":" + "null"); 
                            else
                            outputStream.println("dir/"+ contacto.getNombre() + ":" + contacto.getIp() + ":" + contacto.getPuerto());
                            }
                        else {
                            mensaje = mensaje.split("/",2)[1];
                            System.out.println("EL MENSAJE DE TEXTO ES: " + mensaje);
                            String[] partes = mensaje.split(";", 4);
                            String[] datos = partes[0].split(":", 3); //datos[0]=nombre / datos[1]= ip / datos[2] = puerto
                            System.out.println("PARTES: "+partes[0] + " /" + partes[1]+ "/ " + partes[2] + "/" + partes[3]);
                            System.out.println("VOY A INVOCAR usuarioEstaOnline con " + partes[3]);
                            boolean esta = usuarioEstaOnline(partes[3]);
                            if (esta)
                                enviaMensaje(partes[3], mensaje); 
                            else{
                                System.out.println("bien 2");
                                servidor.getgestorMensajesGuardados().guardaMensaje(partes[3], mensaje);
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
        System.out.println(contacto + " esta online ?, voy a verificarlo !");
        for (ClientHandler c : ClientHandler.clientHandlers){
            System.out.println(c.getUser() + " esta online, pero es igual a " + contacto + " ?");
            if (c.getUser().equals(contacto)){
                System.out.println("SIP");
                return true;
            }
            System.out.println("NOP");
        }
        return false;
    }
    
    private Contacto buscaDir(String nombre){
        Contacto found = null;
        for (Contacto c : servidor.getDir()){
            if (c.getNombre().equals(nombre)){
                found = new Contacto(nombre,c.getIp(),c.getPuerto());
                return found;
            }
        }
        return found;
    }

    private void enviaMensaje(String receptor, String mensaje) {
        System.out.println("Receptor: "+receptor+" Mensaje: "+mensaje);
        for (ClientHandler c : ClientHandler.clientHandlers) {
            try {
                System.out.println("[ClientHandler]: Nombre de cliente:"+c.user);
                //if (c.user.equals(receptor) && !c.user.equals(user)){ // Faltaria implementar que guarde el mensaje si el receptor no esta conectado
                if (c.user.equalsIgnoreCase(receptor)){
                    c.outputStream.println("texto/" + mensaje); 
                }   
            }
            catch(Exception e){//este catch no funciona
                System.err.println("Error al enviar mensaje: " + e.getMessage());
                cierraConexion(socket, inputStream, outputStream); 
            }
        }
    }

    private void cierraConexion(Socket socket1, BufferedReader inputStream1, PrintWriter outputStream1) {
        System.out.println(this.user +" se desconecto ");
        clientHandlers.remove(this);
        System.out.println("ClientHandlers:"+ ClientHandler.clientHandlers);
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
