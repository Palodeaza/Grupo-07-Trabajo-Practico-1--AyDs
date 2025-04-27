
package modelo;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IGestionMensajesGuardados {
  
    public Map<String, ArrayList<String>> getMensajesGuardados();
    public void guardaMensaje(String receptor, String mensaje);
    public void enviaMensajesGuardados(String user, PrintWriter outputStream);
    public boolean tieneMensajePendiente(String user);
    
}
