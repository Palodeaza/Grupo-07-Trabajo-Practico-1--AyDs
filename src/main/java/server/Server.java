package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import modelo.Contacto;

//import main.java.server.GestorSinc;
//import main.java.server.IGestionSinc;
import server.ServerHandler;
import modelo.GestorRed.MessageHandler;

public class Server {
    
    private ServerSocket serverSocket;
    private Socket socketSinc;
    private Socket serverSecundario;
    private BufferedReader inputStreamSinc;
    private PrintWriter outputStreamSinc;
    private PrintWriter outputStreamSecundario;
    private IGestionDir gestorDir = new GestorDir();
    //private IGestionSinc gestorSinc = new GestorSinc();
    private IGestionMensajesGuardados gestorMensajesGuardados = new GestorMensajesGuardados();
    
    public IGestionMensajesGuardados getGestorMensajesGuardados(){
        return this.gestorMensajesGuardados;
    }

    /*public IGestionMensajesGuardados getGestorSinc(){
        return this.gestorSinc;
    }
    */
    public IGestionDir getGestorDir(){
        return this.gestorDir;
    }

    public Server(int puertoMio, int puertoOtro){
        try {
            serverSocket = new ServerSocket(puertoMio);
            this.socketSinc = new Socket("localhost", puertoOtro);   
            this.outputStreamSinc = new PrintWriter(socketSinc.getOutputStream(), true);  
            //this.inputStreamSinc = new BufferedReader(new InputStreamReader(socketSinc.getInputStream()));
            outputStreamSinc.println("admin"); //ME INTENTO CONECTAR AL SERVER PRINCIPAL (SI ES QUE EXISTE)
            System.out.println("Me conecte al principal");
            new Thread(new ServerHandler(socketSinc,this)).start();//Server Hanndler se encarga de lidiar con los mensajes que me llegan del principal
        } catch (IOException ex) {
            System.out.println("ESTOY SOLITO");
            //ex.printStackTrace();
        }
    }
    
    public void iniciarServidor(){
        
        new Thread(() -> {
            try {
                while (true) {
                    System.out.println("Esperando mensaje...");
                    Socket clientSocket = serverSocket.accept(); 
                    BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    String user = input.readLine();
                    if (user.equals("admin")){
                        System.out.println("wow me hablo el otro server"); // le tengo que pasar todo, y mantenerlo abierto
                        this.serverSecundario = clientSocket;
                        this.outputStreamSecundario = new PrintWriter(serverSecundario.getOutputStream(), true);   
                        
                        //MANDO TODO EL DIR
                        System.out.println("Mando todo el dir...");
                        for (Contacto c : this.gestorDir.getDir()) {
                            this.outputStreamSecundario.print("diragrega/" + c.getNombre() + ";" + c.getIp() + ";" + c.getPuerto());
                        }
                        System.out.println("Mando todo el el hashmap...");
                        //QUE PAJA MANDAR UN HASHMAP LA PUTA QUE LO PARIO
                        for (String usuario : this.getGestorMensajesGuardados().getMensajesGuardados().keySet()) {
                            ArrayList<String> mensajes = this.getGestorMensajesGuardados().getMensajesGuardados().get(usuario);
                            for (String mensaje : mensajes) {
                                this.outputStreamSecundario.print("msjguardar/" + usuario + ";" + mensaje);
                            }
                        }
                        //SI ES LA PRIMERA VEZ QUE SE CONECTAN LOS DOS, ESTA BIEN, LAS LISTAS VAN A ESTAR VACIAS
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
                            String ipC = clientSocket.getInetAddress().toString();
                            int puertoC = clientSocket.getPort();
                            Contacto c = new Contacto(user, ipC, puertoC);
                            getGestorDir().agregaAlDir(c);
                            outputStreamSecundario.println("diragrega/"+ user + ";" + ipC + ";" + puertoC); // CREO QUE ACA HAY QUE CAMBIAR TODO A LO REFERIDO A SOCKET SERVER SECUNDARIO, NO SOCKET SINC
                        }
                        
                        new Thread(new ClientHandler(clientSocket, user, this, outputStreamSecundario)).start();
                        
                        if (gestorMensajesGuardados.tieneMensajePendiente(user)){
                            PrintWriter outputStream = new PrintWriter(clientSocket.getOutputStream(), true); 
                            gestorMensajesGuardados.enviaMensajesGuardados(user, outputStream);
                            outputStreamSecundario.println("msjclear/"+ user);
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Error en el servidor: " + e.getMessage());
            }
        }).start();
    }
}
