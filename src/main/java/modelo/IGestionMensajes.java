/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package modelo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author felis
 */
public interface IGestionMensajes {
    public void agregaMensaje(String nombreCliente, String mensaje);
    public List<String> getMensajesDe(String nombreCliente);
}
