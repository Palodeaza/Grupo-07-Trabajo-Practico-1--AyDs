
package server.modelo;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cliente.modelo.Contacto;

public interface IValidador {
  
    public void validacion(Socket clientSocket) throws IOException;
}
