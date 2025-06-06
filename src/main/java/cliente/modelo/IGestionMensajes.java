package cliente.modelo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cliente.modelo.IMensaje;
import persistencia.GuardadorMensaje;

public interface IGestionMensajes {
    public void agregaMensaje(String nombre ,IMensaje mensaje);
    public List<IMensaje> getMensajesDe(String nombreCliente);
    public void setMensajes(Map<String, List<IMensaje>> mensajes);
    public void setGuardador(GuardadorMensaje guardador);
}
