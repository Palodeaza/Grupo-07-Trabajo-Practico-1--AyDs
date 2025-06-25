/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package cliente.modelo;

import java.util.List;

import cliente.controlador.IGestionInterfaz;
import cliente.modelo.IMensaje;
import java.util.ArrayList;

/**
 *
 * @author felis
 */
public interface IGestionRed {
    public void usuarioOnline(String emisor, String serverN);
    public void setControlador(IGestionInterfaz controlador);
    public void iniciarConexionCliente(String nombre, String ip, int puerto, String emisor);
    public void cerrarConexion();
    public boolean isSocket();
    public void checkDir(Contacto contacto);
    public List<String> getListaConexiones();
    public boolean estaConectado(String conexion);
    public String obtenerIPLocal();
    public void enviarMensaje(IMensaje mensaje);

    public void setConexionesActivas(ArrayList<String> arrayList);
}
