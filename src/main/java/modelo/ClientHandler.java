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
    private static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    
    public ClientHandler(Socket socket, String nombre){
        try {
            this.socket = socket;
            this.inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.outputStream = new PrintWriter(socket.getOutputStream(), true); 
            this.user = nombre;
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
                String[] partes = mensaje.split(";", 3);
                String[] datos = partes[0].split(":",3); //datos[0]=nombre / datos[1]=ip / datos[2]=puerto
                enviaMensaje(datos[0], partes[1]); //TENEMOS QUE MANDAR EL RECEPTOR EN EL MENSAJE
            }
            catch(IOException e){
                cierraConexion(socket, inputStream, outputStream);
                e.printStackTrace();
                break;
            }
        }
    }

    private void enviaMensaje(String receptor, String mensaje) { //MANDAR EL RECEPTOR EN EL MENSAJE
        for (ClientHandler c : clientHandlers) {
            try {
                if (c.user.equals(receptor)){ // Faltaria implementar que guarde el mensaje si el receptor no esta conectado
                    c.outputStream.println(mensaje); 
                }   
            }
            catch(Exception e){//creo que aca iria el guardado del mensaje no enviado
                System.err.println("Error al enviar mensaje: " + e.getMessage());
                cierraConexion(socket, inputStream, outputStream); 
            }
        }
    }

    private void cierraConexion(Socket socket1, BufferedReader inputStream1, PrintWriter outputStream1) {
        clientHandlers.remove(this);
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
}
