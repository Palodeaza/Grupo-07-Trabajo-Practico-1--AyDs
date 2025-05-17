package modelo;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private ServerSocket serverSocket;
    private IGestionDir gestorDir = new GestorDir();
    private IGestionMensajesGuardados gestorMensajesGuardados = new GestorMensajesGuardados();
    
    public IGestionMensajesGuardados getGestorMensajesGuardados(){
        return this.gestorMensajesGuardados;
    }

    public IGestionDir getGestorDir(){
        return this.gestorDir;
    }

    public Server(){
        try {
            serverSocket = new ServerSocket(3333);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void iniciarServidor(){
        new Thread(() -> {
            try {
                while (true) {
                    Socket clientSocket = serverSocket.accept(); 
                    BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    String user = input.readLine();
                    System.out.println("Soy el server y se conecto:" + user);
                    
                    for (ClientHandler c : ClientHandler.clientHandlers){ // veo si no estaba conectado todavia
                        if (c.getUser().equals(user)){
                            System.out.println(user + " ya se habia conectado ");
                            PrintWriter outputStream = new PrintWriter(clientSocket.getOutputStream(), true); 
                            outputStream.println("dupe/dupe");
                            clientSocket.close();
                            break;
                        }
                    }      
                    
                    if (!(getGestorDir().tieneEnElDir(user))){ // si user no estaba en dir, lo agrego, se agrego por primera vez!
                        Contacto c = new Contacto(user,clientSocket.getInetAddress().toString(),clientSocket.getPort());
                        getGestorDir().agregaAlDir(c);
                    }
                    
                    new Thread(new ClientHandler(clientSocket, user, this)).start();
                    
                    if (gestorMensajesGuardados.tieneMensajePendiente(user)){
                        PrintWriter outputStream = new PrintWriter(clientSocket.getOutputStream(), true); 
                        gestorMensajesGuardados.enviaMensajesGuardados(user, outputStream);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error en el servidor: " + e.getMessage());
            }
        }).start();
    }
    
    public static void main(String[] args) {
        Server server = new Server();
        server.iniciarServidor();
        System.out.println("Servidor iniciado");
    }
}
