package modelo;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private ServerSocket serverSocket;
    private ArrayList<String> dir = new ArrayList<>();
    private ArrayList<String> dir_activos = new ArrayList<>();
    private ArrayList<String> dir_inactivos = new ArrayList<>();
    //asumo que tambien tendria que tener acceso al modelo, para poder enviarle los mensajes a los usuarios... o no?
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
                    Socket clientSocket = serverSocket.accept(); // por cada cliente que se quiera conectar, le doy un socket
                    BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    String user = input.readLine();
                    System.out.println("Soy el server y se conecto:" + user);
                    
                    for (ClientHandler c : ClientHandler.clientHandlers){ // veo si no estaba conectado todavia
                        if (c.getUser().equals(user)){
                            System.out.println(user + " ya se habia conectado ");
                            clientSocket.close();
                            break;
                        }
                    }      
                    
                    if (!(dir.contains(user))){ // si user no estaba en dir, lo agrego, se agrego por primera vez!
                        dir.add(user);
                    }
                        
                    new Thread(new ClientHandler(clientSocket, user, this)).start(); // como cojones sacamos el user? podemos mandar un mensaje default

                    }
                
            } catch (IOException e) {
                System.err.println("Error en el servidor: " + e.getMessage());
            }
        }).start();
    }
    
    public static void main(String[] args) {
        Server server = new Server();
        server.iniciarServidor();
    }
}
