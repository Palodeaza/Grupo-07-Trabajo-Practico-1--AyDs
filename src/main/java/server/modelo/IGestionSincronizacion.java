/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server.modelo;

import cliente.modelo.Contacto;
import cliente.modelo.Mensaje;

/**
 *
 * @author felis
 */
public interface IGestionSincronizacion {
    
    public void diragrega(Contacto contacto);
    public void msjguardar(String parte1, String parte2);
    public void msjclear(String mensaje);
    
}
