package server.modelo;

import server.modelo.IGestionMensajesGuardados;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestorMensajesGuardados implements IGestionMensajesGuardados {
    
    private Map<String, ArrayList<String>> mensajesGuardados = new HashMap<>();

    @Override
    public Map<String, ArrayList<String>> getMensajesGuardados(){
        return this.mensajesGuardados;
    }

    @Override
    public void guardaMensaje(String receptor, String mensaje){
        mensajesGuardados.putIfAbsent(receptor, new ArrayList<>());
        mensajesGuardados.get(receptor).add(mensaje);
        System.out.println("Mensaje guardado para " + receptor + ": " + mensaje);
        System.out.println(mensajesGuardados);
    }

    @Override
    public void enviaMensajesGuardados(String user, PrintWriter outputStream){
        ArrayList<String> nuevosMensajes = mensajesGuardados.get(user);
        for (String mensaje : nuevosMensajes)
            outputStream.println("texto/" + mensaje);
        mensajesGuardados.remove(user);
    }
    
    @Override
    public boolean tieneMensajePendiente(String user){
        return mensajesGuardados.containsKey(user);
    }
}
