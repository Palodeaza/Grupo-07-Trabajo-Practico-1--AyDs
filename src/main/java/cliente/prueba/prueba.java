package prueba;

import controlador.GestorInterfaz;
import controlador.IGestionInterfaz;
import modelo.IGestionContactos;
import modelo.IGestionMensajes;
import vistas.Init;
import cliente.vistas.Login;
import vistas.newChat;
import vistas.newContact;

import javax.swing.*;

import modelo.GestorContactos;
import modelo.GestorMensajes;
import modelo.GestorRed;
import modelo.IGestionRed;

    public class prueba {
        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
                try {
                    for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                        if ("Nimbus".equals(info.getName())) {
                            UIManager.setLookAndFeel(info.getClassName());
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                IGestionContactos gestorcontactos = new GestorContactos();
                IGestionMensajes gestormensajes = new GestorMensajes();
                IGestionRed gestored = new GestorRed(gestorcontactos, gestormensajes);

                Login login = new Login();
                Init init = new Init();
                newContact nc = new newContact();
                newChat nch = new newChat();
                IGestionInterfaz ctrl = new GestorInterfaz(login, init, nc, nch, gestored, gestorcontactos, gestormensajes);
                gestored.setControlador(ctrl);
                login.pack(); 
                login.setLocationRelativeTo(null);
                login.setVisible(true);
            });
        }
    }