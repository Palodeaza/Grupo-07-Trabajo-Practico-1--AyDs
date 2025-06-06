/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package cliente.controlador;


import cifrado.ContextoCifrado;
import cliente.modelo.IMensaje;
import cliente.modelo.Mensaje;
import vistas.Init;

/**
 *
 * @author felis
 */
public interface IGestionInterfaz { //gestiona TODOS los eventos producidos por y para las vistas
    public void autenticarUsuario();
    public void agregarNuevoContacto();
    public void agregadoExitoso();
    public void agregadoRepetido();
    public void actualizaListaContactos();
    public void iniciarChatConSeleccion();
    public void enviarMensaje();
    public void borraChat(String contacto);
    public void borraChat();
    public void mostrarMensajeEnChat(IMensaje mensaje);
    public void actualizaChatPanel(String nombre);
    public void refreshConversaciones();
    public Init getInitView();
    public void mostrarCartelErrorConexion();
    public void mostrarCartelErrorDir();
    public void mostrarCartelErrorUsuarioConectado();
    public ContextoCifrado getContextocifrado();
}
