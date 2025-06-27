
package persistencia;

import java.util.List;
import java.util.Map;

import cliente.modelo.IMensaje;

public interface GuardadorMensaje {
    void guardarMensaje(String emisor, String mensaje, String hora, String receptor);
    Map<String, List<IMensaje>> cargarMensajes();
}