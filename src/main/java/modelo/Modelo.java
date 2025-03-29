package modelo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class Modelo {
    private ServerSocket serverSocket;
    private Map<String, String[]> contactos = new HashMap<>(); // Nombre->[ip, puerto1] // puse este por poner pero me parece q es mejor de otra forma...

    public boolean validarCredenciales(String usuario, int puerto) {
        return !usuario.isEmpty() && puerto > 0; //falta mensaje base..
    }

    public void iniciarServidor(int puerto) {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(puerto);
                System.out.println("Servidor iniciado en el puerto " + puerto);
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Cliente conectado: " + clientSocket.getInetAddress());
                    //falta agregar el client handler
                }
            } catch (IOException e) {
                System.err.println("Error en el servidor: " + e.getMessage());
            }
        }).start();
    }

    public boolean agregarContacto(String nombre, String ip, int puerto) {
        if (!contactos.containsKey(nombre)) {
            contactos.put(nombre, new String[]{ip, String.valueOf(puerto)});
            return true;
        }
        return false;
    }

    public String[] obtenerDatosContacto(String nombre) {
        return contactos.get(nombre);
    }

    public boolean conversacionActiva(String contacto) {
        //falta armar logica
        return false;
    }

    public void iniciarConexionCliente(String ip, int puerto) { // Falta cambiar los tipos de los buffers 
        new Thread(() -> {
            try {
                Socket socket = new Socket(ip, puerto);
                System.out.println("Conectado con " + ip + ":" + puerto);
                // Esto creo que es al pedo
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                System.err.println("Error al conectar con el contacto: " + e.getMessage());
            }
        }).start();
    }
}
