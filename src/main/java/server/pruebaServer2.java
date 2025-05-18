package server;

import server.Server;
import vistas.ServerView;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import vistas.AnimatedLabel;

public class pruebaServer2 {
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
                    if (puerto2>0){
                        lbl.setText("Servidor iniciado");
                        lbl.setEnabled(false);
                        lbl.removeMouseListener(this);

                        Server server2 = new Server(puerto2, puerto1);
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
