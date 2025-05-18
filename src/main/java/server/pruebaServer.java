package server;

import vistas.ServerView;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.ServerSocket;

import javax.swing.JOptionPane;

import vistas.AnimatedLabel;
import cliente.modelo.ConfigLoader;

public class pruebaServer {

    public static void main(String[] args) {
        ServerView serverView = new ServerView();
        serverView.setVisible(true);

        serverView.getNewContactButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AnimatedLabel lbl = serverView.getNewContactButton();
                try {
                    int puerto1 = Integer.parseInt(ConfigLoader.getProperty("server1.puerto"));
                    int puerto2 = Integer.parseInt(ConfigLoader.getProperty("server2.puerto"));

                    int puertoActivo;
                    int puertoAlternativo;

                    if (puertoDisponible(puerto1)) {
                        puertoActivo = puerto1;
                        puertoAlternativo = puerto2;
                    } else if (puertoDisponible(puerto2)) {
                        puertoActivo = puerto2;
                        puertoAlternativo = puerto1;
                    } else {
                        JOptionPane.showMessageDialog(serverView, "Ningún puerto disponible. Verifica los puertos en config.properties.");
                        return;
                    }

                    lbl.setText("Puerto " + puertoActivo);
                    lbl.setEnabled(false);
                    lbl.removeMouseListener(this);
                    Server server = new Server(puertoActivo, puertoAlternativo);
                    server.iniciarServidor();

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(serverView, "Los puertos deben ser validos.");
                }
            }
        });
    }

    private static boolean puertoDisponible(int puerto) {
        try (ServerSocket testSocket = new ServerSocket(puerto)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}