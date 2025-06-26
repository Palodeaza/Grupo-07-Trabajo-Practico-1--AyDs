package cliente.modelo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cliente.controlador.IGestionInterfaz;
import cliente.modelo.IMensaje;
import cliente.modelo.IGestionRed;
import cliente.modelo.GestorRed.MessageHandler;
import persistencia.GuardadorMensaje;

public class GestorReconexion implements IGestionReconexion{

    public GestorReconexion(){
    }

    @Override
    public boolean reconectarBackup(IGestionRed gestor) {
        try {
            Socket socketMonitor = new Socket("localhost",1050);
            PrintWriter outMonitor = new PrintWriter(socketMonitor.getOutputStream(),true);
            BufferedReader inMonitor = new BufferedReader(new InputStreamReader(socketMonitor.getInputStream()));
            outMonitor.println("servidoractivo");
            String nuevoServidor = inMonitor.readLine();
            String ip =  ConfigLoader.getProperty(gestor.getUsuario(),"server.ip");
            int puerto = Integer.parseInt(ConfigLoader.getProperty(gestor.getUsuario(),"server" + nuevoServidor + ".puerto"));
            System.out.println("Intentando reconectar al servidor" + nuevoServidor);
            Socket nuevoSocket = new Socket(ip, puerto);

            gestor.setSocket(nuevoSocket);
            gestor.setOutputStream(new PrintWriter(nuevoSocket.getOutputStream(), true));
            gestor.getOutputStream().println(gestor.getUsuario());
            //new Thread(new MessageHandler(nuevoSocket, gestor.getUsuario())).start();
            gestor.iniciarMessageHandler(nuevoSocket, gestor.getUsuario());
            gestor.setServidorActivo(nuevoServidor);
            return true;
        } catch (IOException e) {
            System.err.println("Fallo al reconectar con servidor" );
            return false;
        }
    }


    @Override
    public void usuarioOnline(IGestionInterfaz controlador, IGestionRed gestor, String emisor, String serverN){
        try {
            Socket socketMonitor = new Socket("localhost",1050);
            PrintWriter outMonitor = new PrintWriter(socketMonitor.getOutputStream(),true);
            BufferedReader inMonitor = new BufferedReader(new InputStreamReader(socketMonitor.getInputStream()));
            outMonitor.println("servidoractivo");
            String servidoractivo = inMonitor.readLine();
            String ip =  ConfigLoader.getProperty(emisor,"server.ip");
            int puerto;
            if (servidoractivo.equals("1")){
            puerto = Integer.parseInt(ConfigLoader.getProperty(emisor,"server1.puerto"));
            }
            else{
                puerto = Integer.parseInt(ConfigLoader.getProperty(emisor,"server2.puerto"));
            }            

            Socket nuevoSocket = new Socket(ip, puerto);
            gestor.setUsuario(emisor);
            gestor.setSocket(nuevoSocket);  
            gestor.setOutputStream(new PrintWriter(nuevoSocket.getOutputStream(), true)); 
            gestor.getOutputStream().println(emisor);
            //new Thread(new MessageHandler(nuevoSocket,emisor)).start();
            gestor.iniciarMessageHandler(nuevoSocket, emisor);
        } catch (IOException e) {
            System.err.println("Error al conectar: " + e.getMessage()); 
            System.err.println("Intentando reconectar al servidor alternativo...");
            if (gestor.getIntentos()==0) {
                usuarioOnline(controlador, gestor , emisor,"server2");
            } else {
                gestor.setIntentos(gestor.getIntentos() + 1);
                System.err.println("No se pudo conectar con el secundario.");
                controlador.mostrarCartelErrorConexion();
            }
        }
    }

}
