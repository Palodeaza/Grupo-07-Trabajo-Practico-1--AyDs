
package persistencia;

import java.util.List;
import java.util.Map;

import main.java.cliente.modelo.Mensaje;

public interface GuardadorMensaje {
    void guardarMensaje(String emisor, String ip, String mensaje, String hora, String receptor);
    Map<String, List<Mensaje>> cargarMensajes();
}