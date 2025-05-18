
package server.modelo;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modelo.Contacto;

public interface IGestionDir {
  
    public ArrayList<Contacto> getDir();
    public boolean tieneEnElDir(String user);
    public void agregaAlDir(Contacto contacto);
    public Contacto buscaDir(String nombre);
}
