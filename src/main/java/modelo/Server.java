package modelo;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private ServerSocket serverSocket;
    private ArrayList<Contacto> dir = new ArrayList<>();
    private Map<String, ArrayList<String>> mensajesGuardados = new HashMap<>();
    
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
                            clientSocket.close();
                            break;
                        }
                    }      
                    
                    if (!(dir.contains(user))){ // si user no estaba en dir, lo agrego, se agrego por primera vez!
                        Contacto c = new Contacto(user,clientSocket.getInetAddress().toString(),clientSocket.getPort());
                        dir.add(c);
                    }
                    
                    new Thread(new ClientHandler(clientSocket, user, this)).start();
                    
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
    
    public ArrayList<Contacto> getDir(){
        return dir;
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.iniciarServidor();
        System.out.println("Servidor iniciado");
    }
}
