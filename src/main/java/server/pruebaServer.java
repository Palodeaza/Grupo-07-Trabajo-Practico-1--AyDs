package server;

import modelo.Server;
import vistas.ServerView;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import vistas.AnimatedLabel;

public class pruebaServer {
    public static void main(String[] args) {
        ServerView serverView = new ServerView();
        serverView.setVisible(true);

        Server server = new Server();

        serverView.getNewContactButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AnimatedLabel lbl = serverView.getNewContactButton();
                lbl.setText("Servidor iniciado");
                lbl.setEnabled(false);
                lbl.removeMouseListener(this);
                server.iniciarServidor();
            }
        });
    }
}
