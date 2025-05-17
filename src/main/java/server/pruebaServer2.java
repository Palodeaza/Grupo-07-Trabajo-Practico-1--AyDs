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
                AnimatedLabel lbl = serverView1.getNewContactButton();
                try{
                    int puerto1 = 1111; //HARDCODEADO
                    int puerto2 = 2222; //HARDCODEADO
                    if (puerto2>0){
                        txt.setEnabled(false);
                        lbl.setText("Servidor iniciado");
                        lbl.setEnabled(false);
                        lbl.removeMouseListener(this);

                        Server server2 = new Server(puerto2, puerto1);
                        server2.iniciarServidor();
                    }
                    else{
                        JOptionPane.showMessageDialog(serverView1, "Puerto invalido.");
                    }
                } catch (NumberFormatException i) {
                JOptionPane.showMessageDialog(serverView1, "El puerto debe ser un número válido.");
                }
            }
        });
        

    }
}
