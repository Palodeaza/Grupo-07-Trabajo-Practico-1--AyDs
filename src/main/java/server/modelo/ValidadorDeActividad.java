package server.modelo;

import server.modelo.IGestionDir;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cliente.modelo.Contacto;

public class ValidadorDeActividad implements IValidador {
    
    public ValidadorDeActividad(){
    }

    @Override
    public void validacion(Socket clientSocket) throws IOException{
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        out.println("pong");
    }
}
