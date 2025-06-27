package persistencia;

import cliente.modelo.Contacto;
import java.io.*;
import java.util.*;
import org.json.*;

public class GuardadorContactoJson implements GuardadorContacto {
    private final String ARCHIVO;

    public GuardadorContactoJson(String usuario) {
        this.ARCHIVO = "persistencia/contactos" + usuario + ".json";
    }

    @Override
    public void guardarContacto(Contacto contacto) {
        JSONArray contactos = new JSONArray();

        File archivo = new File(ARCHIVO);
        archivo.getParentFile().mkdirs();

        if (archivo.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                StringBuilder sb = new StringBuilder();
                String linea;
                while ((linea = br.readLine()) != null) sb.append(linea);
                if (!sb.toString().isEmpty())
                    contactos = new JSONArray(sb.toString());
            } catch (IOException | JSONException e) {
                System.out.println("[JSON] No se pudieron leer contactos anteriores.");
            }
        }

        JSONObject nuevo = new JSONObject();
        nuevo.put("nombre", contacto.getNombre());
        //nuevo.put("ip", contacto.getIp());
        //nuevo.put("puerto", contacto.getPuerto());

        contactos.put(nuevo);

        try (FileWriter fw = new FileWriter(archivo)) {
            fw.write(contactos.toString(2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Contacto> cargarContactos() {
        List<Contacto> lista = new ArrayList<>();
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            StringBuilder sb = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null) sb.append(linea);

            JSONArray arr = new JSONArray(sb.toString());
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                Contacto c = new Contacto(obj.getString("nombre"), "127.0.1.1", 3333);
                lista.add(c);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return lista;
    }
}