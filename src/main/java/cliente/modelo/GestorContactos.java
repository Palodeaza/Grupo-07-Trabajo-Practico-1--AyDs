package cliente.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cliente.modelo.IMensaje;
import persistencia.GuardadorContacto;

public class GestorContactos implements IGestionContactos {
    
    private List<Contacto> contactos = new ArrayList<>();
    private GuardadorContacto guardadorContacto;
    
    @Override
    public List<Contacto> getListaContactos() {
        return this.contactos;
    }

    public GestorContactos() {
    }
    
    public void setContactos(List<Contacto> contactos) {
        this.contactos = contactos;
    }
    
    public void setGuardador(GuardadorContacto guardador) {
        this.guardadorContacto = guardador;
    }
    
    @Override
    public boolean agregarContacto(String nombre) {
        for (Contacto c : contactos) {
            if (c.getNombre().equalsIgnoreCase(nombre)) {
                return false;
            }
        }
        Contacto c = new Contacto(nombre, "localhost", 3333);
        contactos.add(c);
        if (guardadorContacto != null) {
            guardadorContacto.guardarContacto(c);
        }
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
    
    public void cargaContactos(Map<String, List<IMensaje>> mensajes) {
    for (String nombreContacto : mensajes.keySet()) {
        agregarContacto(nombreContacto); 
    }
}
}
