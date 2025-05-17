package modelo;

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
        System.out.println("bien 3");
        mensajesGuardados.putIfAbsent(receptor, new ArrayList<>());
        mensajesGuardados.get(receptor).add("texto/" + mensaje);
        System.out.println("Mensaje guardado para " + receptor + ": " + mensaje);
        System.out.println(mensajesGuardados);
    }

    @Override
    public void enviaMensajesGuardados(String user, PrintWriter outputStream){
        ArrayList<String> nuevosMensajes = mensajesGuardados.get(user);
        for (String mensaje : nuevosMensajes)
            outputStream.println(mensaje);
        mensajesGuardados.remove(user);
    }
    
    @Override
    public boolean tieneMensajePendiente(String user){
        return mensajesGuardados.containsKey(user);
    }
}
