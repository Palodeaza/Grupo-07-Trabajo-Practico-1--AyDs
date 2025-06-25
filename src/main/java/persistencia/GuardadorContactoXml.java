package persistencia;

import cliente.modelo.Contacto;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuardadorContactoXml implements GuardadorContacto {
    private final String ARCHIVO;

    public GuardadorContactoXml(String usuario) {
        this.ARCHIVO = "persistencia/contactos" + usuario + ".xml";
    }

    @Override
    public void guardarContacto(Contacto contacto) {
        File archivo = new File(ARCHIVO);
        archivo.getParentFile().mkdirs();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document doc;
            Element root;

            if (archivo.exists()) {
                doc = builder.parse(archivo);
                root = doc.getDocumentElement();
            } else {
                doc = builder.newDocument();
                root = doc.createElement("contactos");
                doc.appendChild(root);
            }

            Element contactoElem = doc.createElement("contacto");

            Element nombre = doc.createElement("nombre");
            nombre.appendChild(doc.createTextNode(contacto.getNombre()));
            contactoElem.appendChild(nombre);

            Element ip = doc.createElement("ip");
            ip.appendChild(doc.createTextNode(contacto.getIp()));
            contactoElem.appendChild(ip);

            Element puerto = doc.createElement("puerto");
            puerto.appendChild(doc.createTextNode(String.valueOf(contacto.getPuerto())));
            contactoElem.appendChild(puerto);

            root.appendChild(contactoElem);

            // Guardar archivo
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(doc), new StreamResult(archivo));

        } catch (Exception e) {
            System.out.println("[XML] Error al guardar contacto:");
            e.printStackTrace();
        }
    }

    @Override
    public List<Contacto> cargarContactos() {
        List<Contacto> contactos = new ArrayList<>();
        File archivo = new File(ARCHIVO);

        if (!archivo.exists()) return contactos;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(archivo);

            NodeList nodos = doc.getElementsByTagName("contacto");

            for (int i = 0; i < nodos.getLength(); i++) {
                Element elem = (Element) nodos.item(i);
                String nombre = elem.getElementsByTagName("nombre").item(0).getTextContent();
                String ip = elem.getElementsByTagName("ip").item(0).getTextContent();
                int puerto = Integer.parseInt(elem.getElementsByTagName("puerto").item(0).getTextContent());

                contactos.add(new Contacto(nombre, ip, puerto));
            }

        } catch (Exception e) {
            System.out.println("[XML] Error al cargar contactos:");
            e.printStackTrace();
        }

        return contactos;
    }
}