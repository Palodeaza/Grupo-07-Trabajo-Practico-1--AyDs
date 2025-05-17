package modelo;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import main.java.server.GestorSinc;
import main.java.server.IGestionSinc;
import main.java.server.ServerHandler;
import modelo.GestorRed.MessageHandler;

public class Server {
    private ServerSocket serverSocket;
    private Socket socketSinc;
    private BufferedReader inputStreamSinc;
    private PrintWriter outputStreamSinc;
    private IGestionDir gestorDir = new GestorDir();
    private IGestionSinc gestorSinc = new GestorSinc();
    private IGestionMensajesGuardados gestorMensajesGuardados = new GestorMensajesGuardados();
    
    public IGestionMensajesGuardados getGestorMensajesGuardados(){
        return this.gestorMensajesGuardados;
    }

    public IGestionMensajesGuardados getGestorSinc(){
        return this.gestorSinc;
    }

    public IGestionDir getGestorDir(){
        return this.gestorDir;
    }

    public Server(int puertoMio, int puertoOtro){
        try {
            serverSocket = new ServerSocket(puertoMio);
            this.socketSinc = new Socket("localhost", puertoOtro);   
            this.outputStreamSinc = new PrintWriter(socketSinc.getOutputStream(), true);  
            //this.inputStreamSinc = new BufferedReader(new InputStreamReader(socketSinc.getInputStream()));
            outputStreamSinc.println("admin");
            //new Thread(new MessageHandler(socket,emisor)).start();
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
                    if (user=="admin"){
                        System.out.println("wow me hablo el otro server");
                        new Thread(new ServerHandler(clientSocket, this)).start();
                    }else{
                        
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
                            ipC = clientSocket.getInetAddress().toString();
                            puertoC = clientSocket.getPort();
                            Contacto c = new Contacto(user, ipC, puertoC);
                            getGestorDir().agregaAlDir(c);
                            outputStreamSinc.println("diragrega/"+ user + ";" + ipC + ";" + puertoC);
                        }
                        
                        new Thread(new ClientHandler(clientSocket, user, this, outputStreamSinc)).start();
                        
                        if (gestorMensajesGuardados.tieneMensajePendiente(user)){
                            PrintWriter outputStream = new PrintWriter(clientSocket.getOutputStream(), true); 
                            gestorMensajesGuardados.enviaMensajesGuardados(user, outputStream);
                            outputStreamSinc.println("msjclear/"+ user);
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Error en el servidor: " + e.getMessage());
            }
        }).start();
    }
}
