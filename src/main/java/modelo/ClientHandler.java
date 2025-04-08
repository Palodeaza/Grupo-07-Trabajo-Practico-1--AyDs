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
            clientHandlers.add(this);
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
                if (mensaje.equals("getDir")){
                    String dir = String.join(";", this.servidor.getDir()); // sale el dir asi-> marcos;palo;tomi
                    outputStream.println(dir);
                }
                else{
                String[] partes = mensaje.split(";", 4);
                String[] datos = partes[0].split(":", 3); //datos[0]=nombre / datos[1]= ip / datos[2] = puerto
                System.out.println(partes[3]+" esta online?: "+usuarioEstaOnline(datos[1]));
                if (usuarioEstaOnline(partes[3]))
                    enviaMensaje(partes[3], mensaje); 
                    else{
                        System.out.println("bien 2");
                        servidor.guardaMensaje(partes[3], mensaje);
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
        for (ClientHandler c : clientHandlers){
            if (c.getUser().equals(contacto))
                return true;
        }
        return false;
    }

    private void enviaMensaje(String receptor, String mensaje) {
        System.out.println("Receptor: "+receptor+" Mensaje: "+mensaje);
        for (ClientHandler c : clientHandlers) {
            try {
                System.out.println("[ClientHandler]: Nombre de cliente:"+c.user);
                //if (c.user.equals(receptor) && !c.user.equals(user)){ // Faltaria implementar que guarde el mensaje si el receptor no esta conectado
                if (c.user.equals(receptor)){
                    c.outputStream.println(mensaje); 
                }   
            }
            catch(Exception e){//creo que aca iria el guardado del mensaje no enviado //este catch no funciona
                System.err.println("Error al enviar mensaje: " + e.getMessage());
                cierraConexion(socket, inputStream, outputStream); 
            }
        }
    }

    private void cierraConexion(Socket socket1, BufferedReader inputStream1, PrintWriter outputStream1) {
        System.out.println(this.user +" se desconecto ");
        clientHandlers.remove(this);
        System.out.println("dlientHandlers:"+ clientHandlers);
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
