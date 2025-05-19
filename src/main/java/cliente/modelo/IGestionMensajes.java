package modelo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import persistencia.GuardadorMensaje;

public interface IGestionMensajes {
    public void agregaMensaje(String nombreCliente, String mensaje);
    public List<String> getMensajesDe(String nombreCliente);
    public void setMensajes(Map<String, List<String>> mensajes);
    public void setGuardador(GuardadorMensaje guardador);
}
