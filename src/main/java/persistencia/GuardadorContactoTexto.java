package persistencia;

import cliente.modelo.Contacto;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GuardadorContactoTexto implements GuardadorContacto {
    private final String ARCHIVO;

    public GuardadorContactoTexto(String usuario) {
        this.ARCHIVO = "persistencia/contactos" + usuario + ".txt";
    }

    @Override
    public void guardarContacto(Contacto contacto) {
        File archivo = new File(ARCHIVO);
        archivo.getParentFile().mkdirs();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true))) {
            // Formato: nombre;ip;puerto
            String linea = contacto.getNombre() + ";" + contacto.getIp() + ";" + contacto.getPuerto();
            bw.write(linea);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("[TXT] Error al guardar contacto:");
            e.printStackTrace();
        }
    }

    @Override
    public List<Contacto> cargarContactos() {
        List<Contacto> contactos = new ArrayList<>();
        File archivo = new File(ARCHIVO);

        if (!archivo.exists()) {
            return contactos;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length == 3) {
                    String nombre = partes[0];
                    String ip = partes[1];
                    int puerto = Integer.parseInt(partes[2]);
                    contactos.add(new Contacto(nombre, ip, puerto));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("[TXT] Error al cargar contactos:");
            e.printStackTrace();
        }

        return contactos;
    }
}