package cliente.prueba;

import cliente.controlador.GestorInterfaz;
import cliente.controlador.IGestionInterfaz;
import cliente.modelo.IGestionContactos;
import cliente.modelo.IGestionMensajes;
import cliente.modelo.IGestionReconexion;
import vistas.Init;
import cliente.vistas.Login;
import vistas.newChat;
import vistas.newContact;

import javax.swing.*;

import cliente.modelo.GestorContactos;
import cliente.modelo.GestorMensajes;
import cliente.modelo.GestorReconexion;
import cliente.modelo.GestorRed;
import cliente.modelo.IGestionRed;

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
                IGestionReconexion gestorReconexion = new GestorReconexion();
                IGestionRed gestored = new GestorRed(gestorcontactos, gestormensajes, gestorReconexion);

                Login login = new Login();
                Init init = new Init();
                newContact nc = new newContact();
                newChat nch = new newChat();
                IGestionInterfaz ctrl = new GestorInterfaz(login, init, nc, nch, gestored, gestorcontactos, gestormensajes, gestorReconexion);
                gestored.setControlador(ctrl);
                login.pack(); 
                login.setLocationRelativeTo(null);
                login.setVisible(true);
            });
        }
    }