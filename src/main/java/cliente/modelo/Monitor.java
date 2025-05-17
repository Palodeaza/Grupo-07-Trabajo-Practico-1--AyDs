package cliente.modelo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Monitor {

    private int serverPrimario = 1; // 1 = primario, 2 = secundario
    private String ip1;
    private int puerto1;
    private String ip2;
    private int puerto2;

    private static final Logger logger = Logger.getLogger(Monitor.class.getName());

    public Monitor() {
        this.ip1 = ConfigLoader.getProperty("server.ip");
        this.puerto1 = Integer.parseInt(ConfigLoader.getProperty("server1.port"));
        this.ip2 = ConfigLoader.getProperty("server.ip");
        this.puerto2 = Integer.parseInt(ConfigLoader.getProperty("server2.port"));
        iniciarMonitor();
    }

    private void iniciarMonitor() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                
                boolean primarioActivo = estaActivo(ip1, puerto1);
                boolean secundarioActivo = estaActivo(ip2, puerto2);

                if (primarioActivo) {
                    serverPrimario = 1;
                    logger.info("✅ Servidor primario activo.");
                } else {
                    logger.warning("❌ Servidor primario caído.");
                    if (secundarioActivo) {
                        serverPrimario = 2;
                        logger.info("🔁 Usando servidor secundario.");
                    } else {
                        logger.severe("🚨 Ambos servidores están caídos.");
                    }
                }
            }
        }, 0, 5000); // cada 5 segundos
    }
    private boolean estaActivo(String ip, int puerto) {
        try (Socket socket = new Socket(ip, puerto);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             OutputStream out = socket.getOutputStream()) {

            // Enviar "ping" + salto de línea
            out.write("ping\n".getBytes());
            out.flush();

            // Leer respuesta
            String respuesta = in.readLine();

            // Comprobar si la respuesta es "pong"
            return "pong".equalsIgnoreCase(respuesta);

        } catch (IOException e) {
            return false;
        }
    
    }
    public int getServidorActivo() {
        return serverPrimario;
    }
}
