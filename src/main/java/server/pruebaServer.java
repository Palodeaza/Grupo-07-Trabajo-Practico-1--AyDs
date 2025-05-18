package server;

import server.Server;
import vistas.ServerView;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.ServerSocket;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import vistas.AnimatedLabel;

public class pruebaServer {
    public static void main(String[] args) {
        ServerView serverView = new ServerView();
        serverView.setVisible(true);

        serverView.getNewContactButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AnimatedLabel lbl = serverView.getNewContactButton();
                try{
                    int puerto1 = 1111; //HARDCODEADO
                    int puerto2 = 2222; //HARDCODEADO
                    if (puerto1>0){
                        lbl.setText("Servidor iniciado");
                        lbl.setEnabled(false);
                        lbl.removeMouseListener(this);

                        Server server1 = new Server(puerto1, puerto2);
                        server1.iniciarServidor();
                    }
                    else{
                        JOptionPane.showMessageDialog(serverView, "Puerto invalido.");
                    }
                } catch (NumberFormatException i) {
                JOptionPane.showMessageDialog(serverView, "El puerto debe ser un número válido.");
                }
            }
        });

    }

/*    PARA IMPLEMENTAR
    public boolean serverPrimario(int puerto) {
        ServerSocket testSocket = null;
        try {
            testSocket = new ServerSocket(puerto);
            return true; // Puerto disponible
        } catch (IOException e) {
            return false; // Puerto en uso
        } finally {
            if (testSocket != null) {
                try {
                    testSocket.close();
                } catch (IOException ignored) {}
            }
        }
    }*/
}
