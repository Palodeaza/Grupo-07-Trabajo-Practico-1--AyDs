/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author felis
 */
public class GestorMensajes implements IGestionMensajes{
    
    private Map<String, List<String>> mensajes = new HashMap<>();
    
    
    public GestorMensajes() {
    }
    
    
    @Override
    public void agregaMensaje(String nombreCliente, String mensaje) {
        mensajes.computeIfAbsent(nombreCliente, k -> new ArrayList<>()).add(mensaje);
    }

    @Override
    public List<String> getMensajesDe(String nombreCliente) {
        return mensajes.get(nombreCliente);
    }
}
