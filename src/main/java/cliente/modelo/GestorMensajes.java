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
        for (Map.Entry<String, List<Mensaje>> entry : mensajes.entrySet()) {
        String clave = entry.getKey();
        List<Mensaje> listaMensajes = entry.getValue();

        System.out.println("Mensajes de " + clave + ":");
        for (Mensaje mensaje : listaMensajes) {
            System.out.println(" - " + mensaje.getMensaje());
        }
    }
    }
    
    private Map<String, List<Mensaje>> mensajes = new HashMap<>();
    private GuardadorMensaje guardador;

    public GestorMensajes() {
    }

    @Override
    public void agregaMensaje(String nombre, Mensaje mensaje) { //String nombreContacto, String mensaje
        mensajes.computeIfAbsent(nombre, k -> new ArrayList<>()).add(mensaje);
        System.out.println("Voy a guardar el mensaje: " + mensaje.getMensaje());
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
