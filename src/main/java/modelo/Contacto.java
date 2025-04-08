package modelo;

public class Contacto {
    private String nombre;
    private String ip;
    private int puerto;

    public Contacto(String nombre, String ip, int puerto){
        this.nombre = nombre;
        this.ip = ip;
        this.puerto = puerto;
    }
    
    public String getNombre(){
        return this.nombre;
    }

    public int getPuerto(){
        return this.puerto;
    }

    public String getIp(){
        return this.ip;
    }
}
