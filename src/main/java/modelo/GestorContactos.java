/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author felis
 */
public class GestorContactos implements IGestionContactos {
    
    public ArrayList<Contacto> contactos = new ArrayList<>();
    
    @Override
    public List<Contacto> getListaContactos() {
        return this.contactos;
    }

    public GestorContactos() {
    }

    @Override
    public boolean agregarContacto(String nombre) {
        for (Contacto c: contactos){
            if (c.getNombre().equalsIgnoreCase(nombre)){
                return false;
            }
        }
        Contacto c = new Contacto(nombre,"localhost",3333);
        contactos.add(c);
        return true;
    }

    @Override
    public String buscaContacto(String ip, String puerto) {
        for (Contacto contacto : this.contactos) {
            if ((contacto.getIp().equals(ip) || contacto.getIp().equals("localhost")) && String.valueOf(contacto.getPuerto()).equals(puerto)) {
                return contacto.getNombre();
            }
        }
        return null;
    }

    @Override
    public String[] obtenerDatosContacto(String contactoSeleccionado) {
        for (Contacto c : contactos) {
            if (c.getNombre().equalsIgnoreCase(contactoSeleccionado))
                return new String[]{c.getIp(), String.valueOf(c.getPuerto())};
            }
        return null;
    }
    
}
