package cliente.modelo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    
    private static Properties props = new Properties();

    static {
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("config.properties")) {
            props.load(input);
        } catch (IOException ex) {
            System.err.println("Error al cargar archivo de configuracion: " + ex.getMessage());
        }
    }

    public static String getProperty(String server) {
        return props.getProperty(server);
    }
}