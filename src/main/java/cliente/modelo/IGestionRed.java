/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package modelo;

import java.util.List;

import controlador.IGestionInterfaz;

/**
 *
 * @author felis
 */
public interface IGestionRed {
    public void usuarioOnline(String emisor);
    public void setControlador(IGestionInterfaz controlador);
    public void iniciarConexionCliente(String nombre, String ip, int puerto, String emisor);
    public void cerrarConexion(String contacto);
    public void checkDir(Contacto contacto);
    public List<String> getListaConexiones();
    public boolean estaConectado(String conexion);
    public String obtenerIPLocal();
    public void enviarMensaje(String contacto, String mensaje);
}
