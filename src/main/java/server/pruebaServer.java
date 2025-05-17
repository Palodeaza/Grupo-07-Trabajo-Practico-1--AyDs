package server;

import modelo.Server;
import vistas.ServerView;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
                JTextField txt1 = serverView.getPortTextField1();
                JTextField txt2 = serverView.getPortTextField2();
                try{
                    int puerto1 = 3000;
                    int puerto2 = 3001;
                    if (puerto1>0 && puerto2>0){ //no testea q sean same puerto
                        txt1.setEnabled(false);
                        txt2.setEnabled(false);
                        lbl.setText("Servidores iniciado");
                        lbl.setEnabled(false);
                        lbl.removeMouseListener(this);

                        Server server1 = new Server(puerto1);
                        Server server2 = new Server(puerto2);
                        server1.iniciarServidor();
                        server2.iniciarServidor();
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
}
