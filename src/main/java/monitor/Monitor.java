package monitor;

import java.io.BufferedReader;
import cliente.modelo.ConfigLoader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Monitor {

    private int serverPrimario = 1; // 1 = primario, 2 = secundario
    private String ip1;
    private int puerto1;
    private String ip2;
    private int puerto2;
    private ServerSocket serverSocket;

    private static final Logger logger = Logger.getLogger(Monitor.class.getName());

    public Monitor() throws IOException {
        this.ip1 = ConfigLoader.getProperty("server.ip");
        this.puerto1 = Integer.parseInt(ConfigLoader.getProperty("server1.puerto"));
        this.ip2 = ConfigLoader.getProperty("server.ip");
        this.puerto2 = Integer.parseInt(ConfigLoader.getProperty("server2.puerto"));
        serverSocket = new ServerSocket(1010);
        /*this.ip1 = "localhost";
        this.puerto1 = 1111;
        this.ip2 = "localhost";
        this.puerto2 = 2222;*/
        //iniciarMonitor();
    }

    public void iniciarMonitor() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("ENTRE AL RUN");
                boolean server1 = estaActivo(ip1, puerto1);
                boolean server2 = estaActivo(ip2, puerto2);
                System.out.println("SALI DE LOS BOOLEANS");
                if (server1 && serverPrimario == 1) {
                    serverPrimario = 1;
                    logger.info("Servidor primarioactivo y es el 1.");
                } else {
                    if (server2 && serverPrimario == 2) {
                    serverPrimario = 2;
                    logger.info("Servidor primarioactivo y es el 2.");
                    }else{
                    logger.warning("Servidor primario caído.");
                    if (server2) {
                        serverPrimario = 2;
                        logger.info(" Usando servidor secundario. Primario es ahora 2");
                    } else {
                        if (server1){
                            serverPrimario = 1;
                            logger.info(" Usando servidor secundario. Primario es ahora 1");
                        } else{
                            logger.severe("Ambos servidores están caídos.");
                        }
                    }

                    }
                }
            }
        }, 0, 5000); // cada 5 segundos
    }
    
    
    public void listening(){
        new Thread(() -> {
            try{
                while (true) {
                    System.out.println("Esperando mensaje...");
                    Socket clientSocket = serverSocket.accept(); 
                    BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    String user = input.readLine();
                    if(user.equals("servidoractivo")){
                        
                    //tengo que hacer un ping manual
                    
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    boolean server1 = estaActivo(ip1, puerto1);
                    boolean server2 = estaActivo(ip2, puerto2);
                    System.out.println("ME PREGUNTARON CUAL ERA PRIMARIO");
                    if (server1 && serverPrimario == 1) {
                        serverPrimario = 1;
                        logger.info("Servidor primarioactivo y es el 1.");
                    } else {
                        if (server2 && serverPrimario == 2) {
                        serverPrimario = 2;
                        logger.info("Servidor primarioactivo y es el 2.");
                        }else{
                        logger.warning("Servidor primario caído.");
                        if (server2) {
                            serverPrimario = 2;
                            logger.info(" Usando servidor secundario. Primario es ahora 2");
                        } else {
                            if (server1){
                                serverPrimario = 1;
                                logger.info(" Usando servidor secundario. Primario es ahora 1");
                            } else{
                                logger.severe("Ambos servidores están caídos.");
                            }
                        }

                        }
                    }
                    out.println(serverPrimario);
                    }
                    }            
                }
            catch(IOException e){
                System.out.println("Desconectado cliente de monitor");
            }  
             
             
            
        }).start();
    }
    
    
    private boolean estaActivo(String ip, int puerto) {
        try {
            System.out.println("ME INTENTO CONECTAR A " + puerto);
            Socket socket = new Socket(ip, puerto);
            System.out.println("ME CONECTE");
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            // Enviar "ping" + salto de línea
            out.println("ping");
            System.out.println("mande ping");
            // Leer respuesta
            String respuesta = in.readLine();
            System.out.println("me llego respuesta");
            // Comprobar si la respuesta es "pong"
            return "pong".equalsIgnoreCase(respuesta);

        } catch (IOException e) {
            return false;
        }
    
    }
    public int getServidorActivo() {
        return serverPrimario;
    }
}
