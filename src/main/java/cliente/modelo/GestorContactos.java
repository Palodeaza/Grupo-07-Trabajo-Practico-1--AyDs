package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import main.java.cliente.modelo.Mensaje;

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
    public String buscaContacto(String nombre) {
        for (Contacto contacto : this.contactos) {
            if ((contacto.getNombre().equals(nombre))) {
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
    
    public void cargaContactos(Map<String, List<Mensaje>> mensajes) {
    for (String nombreContacto : mensajes.keySet()) {
        agregarContacto(nombreContacto); 
    }
}
}
