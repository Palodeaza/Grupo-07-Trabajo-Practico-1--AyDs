package persistencia;

import cliente.modelo.FabricaMensajes;
import cliente.modelo.IMensaje;
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
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.NodeList;

import cliente.modelo.Mensaje;

public class GuardadorMensajeXml implements GuardadorMensaje {

    private  String ARCHIVO;
    private final String  usuario;
    
    public GuardadorMensajeXml(String usuario){
        this.usuario = usuario;
        this.ARCHIVO = "persistencia/mensajes"+usuario+".xml";
    }
 /*   @Override
    public void guardarMensaje(String emisor, String ip, String mensaje, String hora, String receptor) {
        System.out.println("Intentando guardar mensaje en XML...");
        
        StringBuilder xml = new StringBuilder();
        xml.append(" <mensaje>\n");
        xml.append("   <emisor>").append(emisor).append("</emisor>\n");
        xml.append("   <ip>").append(ip).append("</ip>\n");
        xml.append("   <contenido>").append(mensaje).append("</contenido>\n");
        xml.append("   <hora>").append(hora).append("</hora>\n");
        xml.append("   <receptor>").append(receptor).append("</receptor>\n");
        xml.append(" </mensaje>\n");
        
        File archivo = new File(ARCHIVO);
        archivo.getParentFile().mkdirs();

        try (FileWriter escritor = new FileWriter(archivo, true)) {
            escritor.write(xml.toString());
            System.out.println("Mensaje guardado exitosamente en mensajes.xml");
        } catch (IOException e) {
            System.out.println("Error al guardar el mensaje en XML:");
            e.printStackTrace();
        }
    }*/
    @Override
public void guardarMensaje(String emisor, String mensaje, String hora, String receptor) {
    System.out.println("Intentando guardar mensaje en XML con raíz...");

    try {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc;
        Element root;

        File archivo = new File(ARCHIVO);
        archivo.getParentFile().mkdirs();

        if (archivo.exists()) {
            doc = db.parse(archivo);
            root = (Element) doc.getDocumentElement(); // <mensajes>
        } else {
            doc = db.newDocument();
            root = doc.createElement("mensajes");
            doc.appendChild(root);
        }

        Element mensajeElem = doc.createElement("mensaje");

        Element emisorElem = doc.createElement("emisor");
        emisorElem.setTextContent(emisor);
        mensajeElem.appendChild(emisorElem);

        Element contenidoElem = doc.createElement("contenido");
        contenidoElem.setTextContent(mensaje);
        mensajeElem.appendChild(contenidoElem);

        Element horaElem = doc.createElement("hora");
        horaElem.setTextContent(hora);
        mensajeElem.appendChild(horaElem);

        Element receptorElem = doc.createElement("receptor");
        receptorElem.setTextContent(receptor);
        mensajeElem.appendChild(receptorElem);

        root.appendChild(mensajeElem);

        // Guardar el XML actualizado
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(archivo);
        transformer.transform(source, result);

        System.out.println("Mensaje guardado exitosamente en XML con raíz.");
    } catch (Exception e) {
        System.out.println("Error al guardar el mensaje en XML:");
        e.printStackTrace();
    }
}

    @Override
    public Map<String, List<IMensaje>> cargarMensajes() {
        System.out.println("Cargando mensajes desde XML...");

        Map<String, List<IMensaje>> mensajes = new HashMap<>();

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
                IMensaje mensaje = FabricaMensajes.getInstancia().creaMensaje();
                mensaje.setNombreEmisor(elemento.getElementsByTagName("emisor").item(0).getTextContent());
                mensaje.setIpEmisor("127.0.1.1");
                mensaje.setMensaje(elemento.getElementsByTagName("contenido").item(0).getTextContent());
                mensaje.setHora(elemento.getElementsByTagName("hora").item(0).getTextContent());
                mensaje.setReceptor(elemento.getElementsByTagName("receptor").item(0).getTextContent());

                System.out.println("Mensaje cargado: " + mensaje.getOutputString());
                String chat = mensaje.getReceptor();
                
                if (chat.equals(usuario))
                    chat = mensaje.getNombreEmisor();
                
                mensajes.computeIfAbsent(chat, k -> new ArrayList<>()).add(mensaje);
            }

        } catch (Exception e) {
            System.out.println("Error al cargar mensajes desde XML:");
            e.printStackTrace();
        }

        return mensajes;
    }
}