package cliente.modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cliente.modelo.IMensaje;
import persistencia.GuardadorMensaje;

public class GestorMensajes implements IGestionMensajes{
    
    private Map<String, List<IMensaje>> mensajes = new HashMap<>();
    private GuardadorMensaje guardador;
    
    @Override
    public void setMensajes(Map<String, List<IMensaje>> mensajes) {
        this.mensajes = mensajes;
        for (Map.Entry<String, List<IMensaje>> entry : mensajes.entrySet()) {
        String clave = entry.getKey();
        List<IMensaje> listaMensajes = entry.getValue();

        System.out.println("Mensajes de " + clave + ":");
        for (IMensaje mensaje : listaMensajes) {
            System.out.println(" - " + mensaje.getMensaje());
        }
    }
    }
    


    public GestorMensajes() {
    }

    @Override
    public void agregaMensaje(String nombre, IMensaje mensaje) { //String nombreContacto, String mensaje
        mensajes.computeIfAbsent(nombre, k -> new ArrayList<>()).add(mensaje);
        System.out.println("Voy a guardar el mensaje: " + mensaje.getMensaje());
        guardador.guardarMensaje(mensaje.getNombreEmisor(), mensaje.getIpEmisor(), mensaje.getMensaje(), mensaje.getHora(), mensaje.getReceptor());
    }

    @Override
    public List<IMensaje> getMensajesDe(String nombreCliente) {
        return mensajes.get(nombreCliente);
    }

    public void setGuardador(GuardadorMensaje guardador) {
        this.guardador = guardador;
    }
}
