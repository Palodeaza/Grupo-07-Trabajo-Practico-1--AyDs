package persistencia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.cliente.modelo.Mensaje;

public class GuardadorMensajeTexto implements GuardadorMensaje {

    private static final String ARCHIVO = "persistencia/mensajes.txt";
    
    @Override
    public void guardarMensaje(String emisor, String ip, String mensaje, String hora, String receptor) {
        System.out.println("Intentando guardar mensaje en archivo de texto...");

        String linea = emisor + ":" + ip + ";" + mensaje + ";" + hora + ";" + receptor;
        File archivo = new File(ARCHIVO);
        archivo.getParentFile().mkdirs();
        try (FileWriter escritor = new FileWriter(archivo, true)) {
            escritor.write(linea + System.lineSeparator());
            System.out.println("Mensaje guardado: " + linea);
        } catch (IOException e) {
            System.out.println("Error al guardar el mensaje en texto:");
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, List<Mensaje>> cargarMensajes() {
        System.out.println("Cargando mensajes desde archivo de texto...");

        Map<String, List<Mensaje>> mensajes = new HashMap<>();

        try (BufferedReader lector = new BufferedReader(new FileReader(ARCHIVO))) {
            String linea;
            int contador = 0;

            while ((linea = lector.readLine()) != null) {
                Mensaje mensaje = new Mensaje();
                // emisor:ip;mensaje;hora;receptor
                String[] partes = linea.split(";");
                if (partes.length == 4) {
                    String[] emisorIp = partes[0].split(":");
                    if (emisorIp.length == 2) {
                        mensaje.setNombreEmisor(emisorIp[0]);
                        mensaje.setIpEmisor(emisorIp[1]);
                        mensaje.setMensaje(partes[1]);
                        mensaje.setHora(partes[2]);
                        mensaje.setReceptor(partes[3]);

                        mensajes.computeIfAbsent(mensaje.getReceptor(), k -> new ArrayList<>()).add(mensaje);
                        System.out.println("Mensaje cargado: " + mensaje.getOutputString());
                        contador++;
                    } else {
                        System.out.println("Formato incorrecto en línea: " + linea);
                    }
                } else {
                    System.out.println("Formato incompleto en línea: " + linea);
                }
            }

            System.out.println("Total de mensajes cargados: " + contador);
        } catch (IOException e) {
            System.out.println("Error al cargar mensajes desde archivo de texto:");
            e.printStackTrace();
        }

        return mensajes;
    }
}