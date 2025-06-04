package modelo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.cliente.modelo.Mensaje;
import persistencia.GuardadorMensaje;

public interface IGestionMensajes {
    public void agregaMensaje(String nombre ,Mensaje mensaje);
    public List<Mensaje> getMensajesDe(String nombreCliente);
    public void setMensajes(Map<String, List<Mensaje>> mensajes);
    public void setGuardador(GuardadorMensaje guardador);
}
