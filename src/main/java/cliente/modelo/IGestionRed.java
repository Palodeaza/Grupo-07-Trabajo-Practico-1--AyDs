/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package cliente.modelo;

import java.util.List;

import cliente.controlador.IGestionInterfaz;
import cliente.modelo.IGestionReconexion;
import cliente.modelo.IMensaje;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author felis
 */
public interface IGestionRed {
    public void setControlador(IGestionInterfaz controlador);
    public void iniciarConexionCliente(String nombre, String ip, int puerto, String emisor);
    public void cerrarConexion();
    public boolean isSocket();
    public void checkDir(Contacto contacto);
    public List<String> getListaConexiones();
    public boolean estaConectado(String conexion);
    public String obtenerIPLocal();
    public void enviarMensaje(IMensaje mensaje);
    public void iniciarMessageHandler(Socket socket, String usuario);

    public void setConexionesActivas(ArrayList<String> arrayList);

    public int getIntentos() ;

    public void setIntentos(int intentos) ;

    public Socket getSocket() ;

    public void setSocket(Socket socket) ;

    public PrintWriter getOutputStream() ;

    public void setOutputStream(PrintWriter outputStream) ;

    public IGestionInterfaz getControlador() ;

    public IGestionContactos getGestorcontactos() ;

    public void setGestorcontactos(IGestionContactos gestorcontactos) ;

    public IGestionMensajes getGestormensajes() ;

    public void setGestormensajes(IGestionMensajes gestormensajes) ;

    public IGestionReconexion getGestorReconexion() ;

    public void setGestorReconexion(IGestionReconexion gestorReconexion) ;

    public ArrayList<String> getConexionesActivas();

    public String getUsuario() ;

    public void setUsuario(String usuario);

    public String getServidorActivo() ;

    public void setServidorActivo(String servidorActivo);

}
