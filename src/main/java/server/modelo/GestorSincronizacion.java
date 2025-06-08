package server.modelo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import cliente.modelo.Contacto;

public class GestorSincronizacion implements Runnable {

    private Socket socket;
    private Server servidor;
    private BufferedReader inputStream;

    public GestorSincronizacion(Socket socket, Server servidor){
        try {
            this.socket = socket;
            this.servidor = servidor;
            this.inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Inicie conversacion con el servidor principal");
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String mensaje;
        while (socket.isConnected()){
            try {
                mensaje = inputStream.readLine();
                System.out.println("[ServerHandler] Mensaje recibido: " + mensaje);
                if (mensaje == null) {
                    // Si mensaje es null, significa que se cortó la conexión
                    System.out.println("Conexion cerrada con el otro servidor");
                    break;
                }
                String operacion = mensaje.split("/", 2)[0];
                mensaje = mensaje.split("/",2)[1]; 
                switch (operacion) {
                    case "diragrega" -> {
                        // Agregar nuevo contacto al directorio
                        String[] partesDir = mensaje.split(";", 3);
                        Contacto contacto = new Contacto(partesDir[0], partesDir[1], Integer.parseInt(partesDir[2]));
                        servidor.agregaContactoAlDir(contacto);
                    }
                    case "msjguardar" -> {
                        // Guardar un mensaje para un usuario
                        String[] partesMsj = mensaje.split(";", 2);
                        servidor.guardaMensaje(partesMsj[0], partesMsj[1]);
                    }
                    case "msjclear" -> {
                        // Eliminar mensajes pendientes de un usuario
                        servidor.getMensajesGuardados().remove(mensaje);
                    }
                }
            } catch(IOException e) {
                System.out.println("El otro servidor se desconecto");
                break;
            }
        }
    }
}