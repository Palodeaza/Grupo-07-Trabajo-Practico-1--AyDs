    package prueba;

    import controlador.GestorInterfaz;
import controlador.IGestionInterfaz;
    import modelo.Modelo;
    import vistas.Init;
    import vistas.Login;
    import vistas.newChat;
    import vistas.newContact;

    import javax.swing.*;

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

                Modelo modelo = new Modelo();
                Login login = new Login();
                Init init = new Init();
                newContact nc = new newContact();
                newChat nch = new newChat();
                IGestionInterfaz ctrl = new GestorInterfaz(login, init, nc, nch, modelo);
                modelo.setControlador(ctrl);

                login.pack(); 
                login.setLocationRelativeTo(null);
                login.setVisible(true);
            });
        }
    }