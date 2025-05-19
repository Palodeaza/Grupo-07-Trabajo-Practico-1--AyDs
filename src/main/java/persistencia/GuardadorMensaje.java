
package persistencia;

import java.util.List;
import java.util.Map;

public interface GuardadorMensaje {
    void guardarMensaje(String emisor, String ip, String mensaje, String hora, String receptor);
    Map<String, List<String>> cargarMensajes();
}