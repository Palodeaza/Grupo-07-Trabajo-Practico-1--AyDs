package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import modelo.Contacto;

public class Server {

    private ServerSocket serverSocket;
    private Socket socketSinc; // Socket usado para conectarse al servidor principal
    private Socket serverSecundario; // Socket usado para aceptar conexión del servidor secundario
    private BufferedReader inputStreamSinc;
    private PrintWriter outputStreamSinc; // Para enviar datos al servidor principal
    private PrintWriter outputStreamSecundario; // Para enviar datos al servidor secundario

    private IGestionDir gestorDir = new GestorDir(); // Directorio de contactos
    private IGestionMensajesGuardados gestorMensajesGuardados = new GestorMensajesGuardados(); // Mensajes pendientes

    public IGestionMensajesGuardados getGestorMensajesGuardados(){
        return this.gestorMensajesGuardados;
    }

    public IGestionDir getGestorDir(){
        return this.gestorDir;
    }

    // Constructor: crea el servidor y se conecta al otro servidor (principal)
    public Server(int puertoMio, int puertoOtro){
        try {
            serverSocket = new ServerSocket(puertoMio); // Abre socket para clientes
            this.socketSinc = new Socket("localhost", puertoOtro); // Me conecto al otro servidor
            this.outputStreamSinc = new PrintWriter(socketSinc.getOutputStream(), true);  
            
            // Envío mensaje inicial al otro servidor para identificarme como servidor secundario
            outputStreamSinc.println("admin"); 
            System.out.println("Me conecte al principal");

            // Maneja los mensajes que lleguen desde el otro servidor
            new Thread(new ServerHandler(socketSinc,this)).start();
        } catch (IOException ex) {
            System.out.println("ESTOY SOLITO (no hay otro servidor disponible)");
        }
    }

    // Método principal del servidor: espera conexiones de clientes o del otro servidor
    public void iniciarServidor(){
        new Thread(() -> {
            try {
                while (true) {
                    System.out.println("Esperando mensaje...");
                    Socket clientSocket = serverSocket.accept(); // Acepta conexión entrante
                    BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    String user = input.readLine(); // Primer mensaje debe ser el nombre de usuario o "admin"
                    switch (user) {
                        case "ping":
                            // Se trata de un chequeo de disponibilidad
                            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                            out.println("pong");
                            System.out.println("Mandé pong");
                            break;
                        case "admin":
                            // Se conecta el servidor secundario
                            System.out.println("Se conectó el otro servidor (secundario)");
                            this.serverSecundario = clientSocket;
                            this.outputStreamSecundario = new PrintWriter(serverSecundario.getOutputStream(), true);
                            // Enviar todo el directorio actual al servidor secundario
                            System.out.println("Enviando directorio...");
                            for (Contacto c : this.gestorDir.getDir()) {
                                this.outputStreamSecundario.println("diragrega/" + c.getNombre() + ";" + c.getIp() + ";" + c.getPuerto());
                            }   // Enviar mensajes guardados también
                            System.out.println("Enviando mensajes guardados...");
                            for (String usuario : this.getGestorMensajesGuardados().getMensajesGuardados().keySet()) {
                                ArrayList<String> mensajes = this.getGestorMensajesGuardados().getMensajesGuardados().get(usuario);
                                for (String mensaje : mensajes) {
                                    this.outputStreamSecundario.println("msjguardar/" + usuario + ";" + mensaje);
                                }
                            }   break;
                        default:
                            // Se conecta un cliente
                            System.out.println("Se conectó cliente: " + user);
                            // Verificar si ya estaba conectado
                            for (ClientHandler c : ClientHandler.clientHandlers) {
                                if (c.getUser().equals(user)) {
                                    System.out.println(user + " ya estaba conectado");
                                    PrintWriter outputStream = new PrintWriter(clientSocket.getOutputStream(), true);
                                    outputStream.println("dupe/dupe"); // Mensaje de conexión duplicada
                                    clientSocket.close();
                                    break;
                                }
                            }   // Si es un cliente nuevo, agregar al directorio
                            if (!getGestorDir().tieneEnElDir(user)) {
                                String ipC = clientSocket.getInetAddress().toString();
                                int puertoC = clientSocket.getPort();
                                Contacto c = new Contacto(user, ipC, puertoC);
                                getGestorDir().agregaAlDir(c);
                                
                                // También se lo informamos al servidor secundario si existe
                                if (this.outputStreamSecundario != null) {
                                    outputStreamSecundario.println("diragrega/" + user + ";" + ipC + ";" + puertoC);
                                }
                            }   // Creamos el hilo que manejará al cliente
                            new Thread(new ClientHandler(clientSocket, user, this, outputStreamSecundario)).start();
                            // Si el cliente tenía mensajes pendientes, los enviamos
                            if (gestorMensajesGuardados.tieneMensajePendiente(user)) {
                                PrintWriter outputStream = new PrintWriter(clientSocket.getOutputStream(), true);
                                gestorMensajesGuardados.enviaMensajesGuardados(user, outputStream);
                                
                                // Limpiamos la lista de mensajes si hay un secundario
                                if (this.outputStreamSecundario != null) {
                                    outputStreamSecundario.println("msjclear/" + user);
                                }
                            }   break;
                    }
                }
            } catch (IOException e) {
                System.err.println("Error en el servidor: " + e.getMessage());
            }
        }).start();
    }
}
