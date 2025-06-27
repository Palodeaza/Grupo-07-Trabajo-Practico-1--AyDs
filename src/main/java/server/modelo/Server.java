package server.modelo;

import server.modelo.GestorSincronizacion;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

import cliente.modelo.Contacto;

public class Server {

    private ServerSocket serverSocket;
    private Socket socketSinc; // Socket usado para conectarse al servidor principal
    private Socket serverSecundario; // Socket usado para aceptar conexión del servidor secundario
    private BufferedReader inputStreamSinc;
    private PrintWriter outputStreamSinc; // Para enviar datos al servidor principal
    private PrintWriter outputStreamSecundario; // Para enviar datos al servidor secundario

    private IGestionDir gestorDir = new GestorDir(); // Directorio de contactos
    private IGestionMensajesGuardados gestorMensajesGuardados = new GestorMensajesGuardados(); // Mensajes pendientes
    private IValidador validadorDeActividad = new ValidadorDeActividad();

    public ArrayList<Contacto> getDirContactos(){
        return gestorDir.getDir();
    }

    public boolean tieneMensajePendiente(String user){
        return gestorMensajesGuardados.tieneMensajePendiente(user);
    }

    public void enviaMensajesGuardados(String user, PrintWriter outputStream){
        gestorMensajesGuardados.enviaMensajesGuardados(user, outputStream);
    }

    public void agregaContactoAlDir(Contacto contacto){
        gestorDir.agregaAlDir(contacto);
    }

    public void guardaMensaje(String receptor, String mensaje){
        gestorMensajesGuardados.guardaMensaje(receptor, mensaje);
    }

    public Map<String, ArrayList<String>> getMensajesGuardados(){
        return gestorMensajesGuardados.getMensajesGuardados();
    }

    public Contacto buscaDirContacto(String nombre){
        return gestorDir.buscaDir(nombre);
    }

    public PrintWriter getOutputStreamSecundario(){
        return outputStreamSecundario;
    }

    public boolean tieneEnElDirContacto(String user){
        return gestorDir.tieneEnElDir(user);
    }

    // Constructor: crea el servidor y se conecta al otro servidor (principal)
    public Server(int puertoMio, int puertoOtro){
        try {
            serverSocket = new ServerSocket(puertoMio); // Abre socket para clientes
            this.socketSinc = new Socket("localhost", puertoOtro); // Me conecto al otro servidor
            this.outputStreamSinc = new PrintWriter(socketSinc.getOutputStream(), true);  
            
            // Envío mensaje inicial al otro servidor para identificarme como servidor secundario
            outputStreamSinc.println("admin"); 

            // Maneja los mensajes que lleguen desde el otro servidor
            new Thread(new GestorSincronizacion(socketSinc,this)).start();
        } catch (IOException ex) {
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
                        case "ping" -> {
                            // Se trata de un chequeo de disponibilidad
                            validadorDeActividad.validacion(clientSocket);
                        }
                        case "admin" -> {
                            // Se conecta el servidor secundario
                            this.serverSecundario = clientSocket;
                            this.outputStreamSecundario = new PrintWriter(serverSecundario.getOutputStream(), true);
                            // Enviar todo el directorio actual al servidor secundario
                            for (Contacto c : getDirContactos()) {
                                this.outputStreamSecundario.println("diragrega/" + c.getNombre() + ";" + c.getIp() + ";" + c.getPuerto());
                            }   // Enviar mensajes guardados también
                            for (String usuario : getMensajesGuardados().keySet()) {
                                ArrayList<String> mensajes = getMensajesGuardados().get(usuario);
                                for (String mensaje : mensajes) {
                                    this.outputStreamSecundario.println("msjguardar/" + usuario + ";" + mensaje);
                                }
                            }
                        }
                        default -> {
                            // Se conecta un cliente
                            System.out.println("Se conecto cliente: " + user);
                            // Verificar si ya estaba conectado
                            for (GestorMensajesClientes c : GestorMensajesClientes.clientHandlers) {
                                if (c.getUser().equals(user)) {
                                    PrintWriter outputStream = new PrintWriter(clientSocket.getOutputStream(), true);
                                    outputStream.println("dupe/dupe"); // Mensaje de conexión duplicada
                                    clientSocket.close();
                                    break;
                                }
                            }   // Si es un cliente nuevo, agregar al directorio
                            if (!tieneEnElDirContacto(user)) {
                                String ipC = clientSocket.getInetAddress().toString();
                                int puertoC = clientSocket.getPort();
                                Contacto c = new Contacto(user, ipC, puertoC);
                                agregaContactoAlDir(c);
                                
                                // También se lo informamos al servidor secundario si existe
                                if (this.outputStreamSecundario != null) {
                                    outputStreamSecundario.println("diragrega/" + user + ";" + ipC + ";" + puertoC);
                                }
                            }   // Creamos el hilo que manejará al cliente
                            new Thread(new GestorMensajesClientes(clientSocket, user, this)).start();
                            // Si el cliente tenía mensajes pendientes, los enviamos
                            if (tieneMensajePendiente(user)) {
                                PrintWriter outputStream = new PrintWriter(clientSocket.getOutputStream(), true);
                                enviaMensajesGuardados(user, outputStream);
                                // Limpiamos la lista de mensajes si hay un secundario
                                if (this.outputStreamSecundario != null) {
                                    outputStreamSecundario.println("msjclear/" + user);
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Error en el servidor: " + e.getMessage());
            }
        }).start();
    }
}
