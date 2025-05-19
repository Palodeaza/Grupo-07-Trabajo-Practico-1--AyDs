package persistencia;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.NodeList;

public class GuardadorMensajeXml implements GuardadorMensaje {

    private static final String ARCHIVO = "persistencia/mensajes.xml";

    @Override
    public void guardarMensaje(String emisor, String ip, String mensaje, String hora, String receptor) {
        System.out.println("Intentando guardar mensaje en XML...");
        
        StringBuilder xml = new StringBuilder();
        xml.append("<mensaje>\n");
        xml.append("  <emisor>").append(emisor).append("</emisor>\n");
        xml.append("  <ip>").append(ip).append("</ip>\n");
        xml.append("  <contenido>").append(mensaje).append("</contenido>\n");
        xml.append("  <hora>").append(hora).append("</hora>\n");
        xml.append("  <receptor>").append(receptor).append("</receptor>\n");
        xml.append("</mensaje>\n");
        
        File archivo = new File(ARCHIVO);
        archivo.getParentFile().mkdirs();

        try (FileWriter escritor = new FileWriter(archivo, true)) {
            escritor.write(xml.toString());
            System.out.println("Mensaje guardado exitosamente en mensajes.xml");
        } catch (IOException e) {
            System.out.println("Error al guardar el mensaje en XML:");
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, List<String>> cargarMensajes() {
        System.out.println("Cargando mensajes desde XML...");

        Map<String, List<String>> mensajes = new HashMap<>();

        File archivo = new File(ARCHIVO);
        System.out.println("Ruta del archivo XML: " + archivo.getAbsolutePath());
        
        if (!archivo.exists()) {
            System.out.println("Archivo XML no encontrado. No hay mensajes para cargar.");
            return mensajes;
        }

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(archivo);
            System.out.println("Archivo XML parseado correctamente.");

            NodeList nodos = doc.getElementsByTagName("mensaje");
            System.out.println("Mensajes encontrados en el XML: " + nodos.getLength());

            for (int i = 0; i < nodos.getLength(); i++) {
                Element elemento = (Element) nodos.item(i);

                String emisor = elemento.getElementsByTagName("emisor").item(0).getTextContent();
                String ip = elemento.getElementsByTagName("ip").item(0).getTextContent();
                String contenido = elemento.getElementsByTagName("contenido").item(0).getTextContent();
                String hora = elemento.getElementsByTagName("hora").item(0).getTextContent();
                String receptor = elemento.getElementsByTagName("receptor").item(0).getTextContent();

                String mensajeFormateado = emisor + ":" + ip + ";" + contenido + ";" + hora + ";" + receptor;
                System.out.println("Mensaje cargado: " + mensajeFormateado);

                mensajes.computeIfAbsent(receptor, k -> new ArrayList<>()).add(mensajeFormateado);
            }

        } catch (Exception e) {
            System.out.println("Error al cargar mensajes desde XML:");
            e.printStackTrace();
        }

        return mensajes;
    }
}