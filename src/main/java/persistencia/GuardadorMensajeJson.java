package persistencia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import org.json.JSONObject;

import main.java.cliente.modelo.Mensaje;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;

public class GuardadorMensajeJson implements GuardadorMensaje {

    private static final String ARCHIVO = "persistencia/mensajes.json";

    @Override
    public void guardarMensaje(String emisor, String ip, String mensaje, String hora, String receptor) {
        System.out.println("[JSON] Intentando guardar mensaje...");

        JSONObject nuevoMensaje = new JSONObject();
        nuevoMensaje.put("emisor", emisor);
        nuevoMensaje.put("ip", ip);
        nuevoMensaje.put("mensaje", mensaje);
        nuevoMensaje.put("hora", hora);
        nuevoMensaje.put("receptor", receptor);

        JSONArray mensajesExistentes = new JSONArray();

        File archivo = new File(ARCHIVO);
        archivo.getParentFile().mkdirs();

        if (archivo.exists()) {
            try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
                StringBuilder sb = new StringBuilder();
                String linea;
                while ((linea = lector.readLine()) != null) {
                    sb.append(linea);
                }

                if (!sb.toString().isEmpty()) {
                    mensajesExistentes = new JSONArray(sb.toString());
                }
                System.out.println("[JSON] Mensajes previos cargados: " + mensajesExistentes.length());

            } catch (IOException | JSONException e) {
                System.out.println("[JSON] No se pudieron leer mensajes anteriores. Se creará un nuevo archivo.");
            }
        }

        mensajesExistentes.put(nuevoMensaje);

        try (FileWriter escritor = new FileWriter(archivo)) {
            escritor.write(mensajesExistentes.toString(2)); 
            System.out.println("[JSON] Mensaje guardado exitosamente.");
        } catch (IOException e) {
            System.out.println("[JSON] Error al guardar el mensaje:");
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, List<Mensaje>> cargarMensajes() {
        System.out.println("[JSON] Cargando mensajes desde archivo JSON...");

        Map<String, List<Mensaje>> mensajes = new HashMap<>();

        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) {
            System.out.println("[JSON] El archivo no existe. No hay mensajes.");
            return mensajes;
        }

        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            StringBuilder sb = new StringBuilder();
            String linea;
            while ((linea = lector.readLine()) != null) {
                sb.append(linea);
            }

            JSONArray jsonMensajes = new JSONArray(sb.toString());
            System.out.println("[JSON] Total de mensajes cargados: " + jsonMensajes.length());

            Mensaje mensaje = new Mensaje();
            for (int i = 0; i < jsonMensajes.length(); i++) {
                JSONObject obj = jsonMensajes.getJSONObject(i);

                mensaje.setNombreEmisor(obj.getString("emisor"));
                mensaje.setIpEmisor(obj.getString("ip"));
                mensaje.setMensaje(obj.getString("mensaje"));
                mensaje.setHora(obj.getString("hora"));
                mensaje.setReceptor(obj.getString("receptor"));

                mensajes.computeIfAbsent(mensaje.getReceptor(), k -> new ArrayList<>()).add(mensaje);
                System.out.println("[JSON] Mensaje cargado: " + mensaje.getOutputString());
            }

        } catch (IOException | JSONException e) {
            System.out.println("[JSON] Error al cargar mensajes:");
            e.printStackTrace();
        }

        return mensajes;
    }
}