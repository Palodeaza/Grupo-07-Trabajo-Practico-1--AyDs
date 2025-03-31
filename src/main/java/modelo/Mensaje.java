package modelo;

import java.io.Serializable;

/**
 *
 * @author tomas
 */
public class Mensaje implements Serializable {
    private static final long serialVersionUID = 1L; // Asegura compatibilidad en la serialización
    private String emisor;
    private String receptor;
    private String contenido;
    private String fecha;
    
    public Mensaje(String emisor, String receptor, String contenido, String fecha){
        this.emisor = emisor;
        this.receptor = receptor;
        this.contenido = contenido;
        this.fecha = fecha;
    }

    /**
     * @return the emisor
     */
    public String getEmisor() {
        return emisor;
    }

    /**
     * @return the receptor
     */
    public String getReceptor() {
        return receptor;
    }

    /**
     * @return the contenido
     */
    public String getContenido() {
        return contenido;
    }

    /**
     * @return the fecha
     */
    public String getFecha() {
        return fecha;
    }
}
