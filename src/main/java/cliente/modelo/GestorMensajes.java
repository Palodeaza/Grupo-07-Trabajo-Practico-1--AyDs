package modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.cliente.modelo.Mensaje;
import persistencia.GuardadorMensaje;

public class GestorMensajes implements IGestionMensajes{
    
    @Override
    public void setMensajes(Map<String, List<Mensaje>> mensajes) {
        this.mensajes = mensajes;
    }
    
    private Map<String, List<Mensaje>> mensajes = new HashMap<>();
    private GuardadorMensaje guardador;

    public GestorMensajes() {
    }

    @Override
    public void agregaMensaje(String nombre, Mensaje mensaje) { //String nombreContacto, String mensaje
        mensajes.computeIfAbsent(nombre, k -> new ArrayList<>()).add(mensaje);
        guardador.guardarMensaje(mensaje.getNombreEmisor(), mensaje.getIpEmisor(), mensaje.getMensaje(), mensaje.getHora(), mensaje.getReceptor());

    }

    @Override
    public List<Mensaje> getMensajesDe(String nombreCliente) {
        return mensajes.get(nombreCliente);
    }

    public void setGuardador(GuardadorMensaje guardador) {
        this.guardador = guardador;
    }
}
