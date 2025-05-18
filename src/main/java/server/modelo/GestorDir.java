package server.modelo;

import server.modelo.IGestionDir;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modelo.Contacto;

public class GestorDir implements IGestionDir {
    
    private ArrayList<Contacto> dir = new ArrayList<>();

    @Override
    public ArrayList<Contacto> getDir(){
        return this.dir;
    }

    @Override
    public boolean tieneEnElDir(String user){
        return dir.contains(user);
    }

    @Override
    public void agregaAlDir(Contacto contacto){
        dir.add(contacto);
    }

    @Override
    public Contacto buscaDir(String nombre){
        Contacto found = null;
        for (Contacto c : dir){
            if (c.getNombre().equals(nombre)){
                found = new Contacto(nombre,c.getIp(),c.getPuerto());
                return found;
            }
        }
        return found;
    }
}
