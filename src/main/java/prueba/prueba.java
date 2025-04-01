package prueba;
import controlador.Controlador;
import modelo.Modelo;
import modelo.Mensaje;
import vistas.ConversacionRenderer;
import vistas.Init;
import vistas.Login;
import vistas.newChat;
import vistas.newContact;
public class prueba {
    
    
    
    public static void main(String args[]){
        
    Modelo modelo = new Modelo();
    Login login = new Login();
    Init init = new Init();
    newContact newContact = new newContact(); 
    newChat newChat = new newChat();
    Controlador controlador = new Controlador(login,init ,newContact, newChat , modelo);
    modelo.setControlador(controlador);
    login.setVisible(true);
    }
}