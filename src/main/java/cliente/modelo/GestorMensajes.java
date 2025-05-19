package modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import persistencia.GuardadorMensaje;

public class GestorMensajes implements IGestionMensajes{
    
    @Override
    public void setMensajes(Map<String, List<String>> mensajes) {
        this.mensajes = mensajes;
    }
    
    private Map<String, List<String>> mensajes = new HashMap<>();
    private GuardadorMensaje guardador;

    public GestorMensajes() {
    }

    @Override
    public void agregaMensaje(String nombreContacto, String mensaje) {
        mensajes.computeIfAbsent(nombreContacto, k -> new ArrayList<>()).add(mensaje);
        // emisor:ip;mensaje;hora;receptor
        String[] partes = mensaje.split(";");
        if (partes.length == 4) {
            String[] emisorIp = partes[0].split(":");
            String emisor = emisorIp[0];
            String ip = emisorIp.length > 1 ? emisorIp[1] : "";
            String msg = partes[1];
            String hora = partes[2];
            String receptor = partes[3];
            guardador.guardarMensaje(emisor, ip, msg, hora, receptor);
        }
    }

    @Override
    public List<String> getMensajesDe(String nombreCliente) {
        return mensajes.get(nombreCliente);
    }

    public void setGuardador(GuardadorMensaje guardador) {
        this.guardador = guardador;
    }
}
