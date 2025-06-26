package cliente.modelo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

public class ConfigLoader {
    

    private static HashMap<String,Properties> props = new HashMap<>();

    static {
        try {
            // Obtener todos los recursos del classpath que terminan con .properties
            Enumeration<URL> resources = ConfigLoader.class.getClassLoader().getResources("");
            while (resources.hasMoreElements()) {
                URL dirURL = resources.nextElement();

                if (dirURL.getProtocol().equals("file")) {
                    try {
                        File dir = new File(dirURL.toURI());
                        File[] files = dir.listFiles((d, name) -> name.startsWith("config") && name.endsWith(".properties"));

                        if (files != null) {
                            for (File file : files) {
                                try (InputStream input = new FileInputStream(file)) {
                                    Properties tempProps = new Properties();
                                    tempProps.load(input);

                                    String fileName = file.getName();
                                    String user = fileName.substring("config".length(), fileName.length() - ".properties".length());
                                    //String user = tempProps.getProperty("user");
                                    if (user != null && !user.isEmpty()) {
                                        props.put(user, tempProps);
                                    } else {
                                        System.err.println("Archivo " + file.getName() + " no tiene clave 'user'");
                                    }
                                } catch (IOException e) {
                                    System.err.println("Error al leer " + file.getName() + ": " + e.getMessage());
                                }
                            }
                        }
                    } catch (URISyntaxException e) {
                        System.err.println("Ruta inválida para recursos locales: " + e.getMessage());
                    }
                }
            }
        } catch (IOException ex) {
            System.err.println("Error al cargar archivo de configuracion: " + ex.getMessage());
        }
    }

    public static String getProperty(String usuario,String prop) {
        if (props.get(usuario) != null)
            return props.get(usuario).getProperty(prop);
        else{
            Properties p = new Properties();
            //p.setProperty("user", usuario);
            p.setProperty("server.ip", "localhost");
            p.setProperty("server1.puerto", "1111");
            p.setProperty("server2.puerto", "2222");
            p.setProperty("clave", "achupupupuchupupu");

            props.put(usuario, p); // se agrega al mapa

            String ARCHIVO = "src/main/resources/config"+usuario+".properties";
            File archivo = new File(ARCHIVO);
            archivo.getParentFile().mkdirs();

            try (FileOutputStream out = new FileOutputStream(archivo)) {
                p.store(out, "Archivo de configuración generado automáticamente para el usuario: " + usuario);
            } catch (IOException e) {
                System.err.println("No se pudo guardar el archivo de configuración para el user: " + usuario + ": " + e.getMessage());
            }


            return p.getProperty(prop); // devuelve la propiedad pedida
        }

    }
}