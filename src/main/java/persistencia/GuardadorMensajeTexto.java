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

import cliente.modelo.Mensaje;
import cliente.modelo.FabricaMensajes;
import cliente.modelo.IMensaje;

public class GuardadorMensajeTexto implements GuardadorMensaje {

    private final String ARCHIVO;
    private final String usuario;
    
    public GuardadorMensajeTexto(String usuario){

        this.ARCHIVO = "persistencia/mensajes"+usuario+".txt";
        this.usuario = usuario;
    }
    @Override
    public void guardarMensaje(String emisor, String mensaje, String hora, String receptor) {
        System.out.println("Intentando guardar mensaje en archivo de texto...");

        String linea = emisor + ":" + ";" + mensaje + ";" + hora + ";" + receptor;
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
    public Map<String, List<IMensaje>> cargarMensajes() {
        System.out.println("Cargando mensajes desde archivo de texto...");

        Map<String, List<IMensaje>> mensajes = new HashMap<>();

        try (BufferedReader lector = new BufferedReader(new FileReader(ARCHIVO))) {
            String linea;
            int contador = 0;

            while ((linea = lector.readLine()) != null) {
                IMensaje mensaje = FabricaMensajes.getInstancia().creaMensaje();
                // emisor:ip;mensaje;hora;receptor
                String[] partes = linea.split(";");
                if (partes.length == 4) {
                    String[] emisorIp = partes[0].split(":");
                    if (emisorIp.length == 2) {
                        mensaje.setNombreEmisor(emisorIp[0]);
                        mensaje.setIpEmisor("127.0.1.1");
                        mensaje.setMensaje(partes[1]);
                        mensaje.setHora(partes[2]);
                        mensaje.setReceptor(partes[3]);
                        String chat = mensaje.getReceptor();
                        if (chat.equals(usuario))
                            chat = mensaje.getNombreEmisor();
                        mensajes.computeIfAbsent(chat, k -> new ArrayList<>()).add(mensaje);
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