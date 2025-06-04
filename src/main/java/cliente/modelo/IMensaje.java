package main.java.cliente.modelo;

public interface IMensaje {

    public void setReceptor(String texto);
    public void setMensaje(String texto);
    public void setHora(String texto);
    public void setIpEmisor(String texto);
    public void setNombreEmisor(String texto);
    public void setTipo(String texto);

    public String getReceptor();
    public String getMensaje();
    public String getHora();
    public String getIpEmisor();
    public String getNombreEmisor();
    public String getTipo();

    public String getOutputString();
    public void setConOutputString(String mensaje);
}
