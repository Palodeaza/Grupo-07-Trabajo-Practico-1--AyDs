package modelo;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private ServerSocket serverSocket;
    private ArrayList<String> dir = new ArrayList<>();
    private Map<String, ArrayList<String>> mensajesGuardados = new HashMap<>();
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
                    
                    if (mensajesGuardados.containsKey(user)){
                        PrintWriter outputStream = new PrintWriter(clientSocket.getOutputStream(), true); 
                        enviaMensajesGuardados(user, outputStream);
                    }
                    }
                
            } catch (IOException e) {
                System.err.println("Error en el servidor: " + e.getMessage());
            }
        }).start();
    }
    
    public void guardaMensaje(String receptor, String mensaje){
        System.out.println("bien 3");
        mensajesGuardados.putIfAbsent(receptor, new ArrayList<>());
        mensajesGuardados.get(receptor).add(mensaje);
        System.out.println("Mensaje guardado para " + receptor + ": " + mensaje);
        System.out.println(mensajesGuardados);
    }

    private void enviaMensajesGuardados(String user, PrintWriter outputStream){
        ArrayList<String> nuevosMensajes = mensajesGuardados.get(user);
        for (String mensaje : nuevosMensajes)
            outputStream.println(mensaje);
        mensajesGuardados.remove(user);
    }
    
    public ArrayList<String> getDir(){
        return dir;
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.iniciarServidor();
    }
}
