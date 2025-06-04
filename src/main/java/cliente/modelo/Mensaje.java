package main.java.cliente.modelo;

import java.util.Date;

import cliente.modelo.UsuarioDuplicadoException;

public class Mensaje implements IMensaje {
    String hora;
    String ipEmisor;
    String mensaje;
    String nombre;
    String receptor;
    String tipo = "texto";
    
    @Override
    public String getHora() {
        return hora;
    }
    
    @Override
    public String getIpEmisor() {
        return ipEmisor;
    }
    
    @Override
    public String getMensaje() {
        return mensaje;
    }

    @Override
    public String getNombreEmisor() {
        return nombre;
    }

    @Override
    public String getOutputString() {
        return tipo + "/" + nombre + ":" + ipEmisor + ";" + mensaje + ";" + hora + ";" + receptor;
    }

    @Override
    public String getReceptor() {
        return receptor;
    }

    @Override
    public String getTipo() {
        return tipo;
    }

    @Override
    public void setHora(String date){
        hora = date;
    }

    @Override
    public void setIpEmisor(String ip) {
        ipEmisor = ip;
    }

    @Override
    public void setMensaje(String msj) {
        mensaje = msj;
    }

    @Override
    public void setNombreEmisor(String name) {
        nombre = name;
    }

    @Override
    public void setReceptor(String receptor) {
        this.receptor = receptor;
    }

    @Override
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public void setConOutputString(String mensaje) {
        String[] partes;
        String[] datos;
        this.tipo = mensaje.split("/",2)[0];
        
        if (this.tipo.equals("dupe")){
        } 
        if (this.tipo.equals("dir")){ 
            datos = mensaje.split("/",2)[1].split(":",3);
            this.nombre = datos[0];
        }
        else { //me mandaron mensaje de texto

            mensaje = mensaje.split("/", 2)[1]; // me quedo con todo menos operación
            partes = mensaje.split(";", 4);

            if (partes.length == 4) { 
                datos = partes[0].split(":", 3);
                System.out.println("me llegó mensaje de: " + datos[0] + " " + datos[1]);
                this.nombre = datos[0];
                this.ipEmisor = datos[1];
                this.mensaje = partes[1];
                this.hora = partes[2];
                this.receptor = partes[3];

            }else{
                System.err.println("Error en el formato del mensajeOutput");
            }
        }
    }
}
