package main.java.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import modelo.ClientHandler;
import modelo.Contacto;
import modelo.Server;

public class ServerHandler implements Runnable {

    private Socket socket;
    private Server servidor;
    private BufferedReader inputStream;

    public ServerHandler(Socket socket, Server servidor){
        try {
            this.socket = socket;
            this.servidor= servidor;
            this.inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Inicie conversacion con otro Server!!");
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
                System.out.println("[ServerHandler] Me llego el mensaje: "+ mensaje);
                if (mensaje==null){
                    cierraConexion(socket, inputStream);
                    break;
                }
                String operacion = mensaje.split("/",2)[0];
                        if (operacion.equals("diragrega")){
                            String[] partes = mensaje.split(";", 3); // user + ";" + ipC + ";" + puertoC
                            Contacto c = new Contacto(partes[0],partes[1],partes[2]);
                            servidor.getGestorDir().agregaAlDir(c);
                            }
                        else if (operacion.equals("msjguardar")){
                            String[] partes = mensaje.split(";", 2); // receptor y mensaje
                            servidor.getGestorMensajesGuardados().guardaMensaje(partes[0], partes[1]);
                        }
                        else if (operacion.equals("msjclear")){
                            servidor.getGestorMensajesGuardados().getMensajesGuardados().remove(mensaje.split("/",2)[1]);
                        }
                        else{ //actualiza toda la base

                        }
            }
            catch(IOException e){
                cierraConexion(socket, inputStream, outputStream); // aca ejecuta cuando se desconecta un cliente
                e.printStackTrace();
                break;
            }
        }
    }


}
