/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author felis
 */
public interface IGestionContactos {
  
    public List<Contacto> getListaContactos();
    public boolean agregarContacto(String nombre);
    public String buscaContacto(String nombre);
    public String[] obtenerDatosContacto(String contactoSeleccionado);
    
    
}
