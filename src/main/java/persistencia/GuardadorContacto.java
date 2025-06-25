package persistencia;

import cliente.modelo.Contacto;
import java.util.List;

public interface GuardadorContacto {
    void guardarContacto(Contacto contacto);
    List<Contacto> cargarContactos();
}