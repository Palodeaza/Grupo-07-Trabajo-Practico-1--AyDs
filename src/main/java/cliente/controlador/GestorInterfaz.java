package cliente.controlador;

import cifrado.CifradoAES;
import cifrado.ContextoCifrado;
import cliente.modelo.ConfigLoader;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import cliente.modelo.Contacto;
import cliente.modelo.FabricaMensajes;
import cliente.modelo.IGestionContactos;
import cliente.modelo.IGestionMensajes;
import cliente.modelo.IGestionRed;
import cliente.modelo.IMensaje;
import vistas.ConversacionRenderer;
import vistas.Init;
import cliente.vistas.Login;
import cliente.modelo.Mensaje;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistencia.FabricaPersistencia;
import persistencia.GuardadorMensaje;
import persistencia.GuardadorContacto;
import vistas.newChat;
import vistas.newContact;
import persistencia.IFabricaPersistencia;

public class GestorInterfaz implements IGestionInterfaz {

    private Login loginView = null;
    private Init initView = null;
    private newContact contactView = null;
    private newChat chatView = null;
    private IGestionContactos gestorcontactos;
    private IGestionMensajes gestormensajes;
    private IGestionRed gestored;
    private String usuarioActual;
    private ContextoCifrado contextocifrado;

    public GestorInterfaz(Login login, Init init, newContact contact, newChat chat, IGestionRed gestored, IGestionContactos gestorcontactos, IGestionMensajes gestormensajes) {
        this.loginView = login;
        this.initView = init;
        this.contactView = contact;
        this.chatView = chat;
        this.gestored = gestored;
        this.gestorcontactos = gestorcontactos;
        this.gestormensajes = gestormensajes;
        this.contextocifrado = new ContextoCifrado(new CifradoAES());
        
        this.loginView.getLoginButton().addActionListener(e -> autenticarUsuario());
        this.contactView.getNewContactButton().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                agregarNuevoContacto();
            }
        });
        this.chatView.getContactList().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                iniciarChatConSeleccion();
            }
        });
        this.initView.getSendMsgTxtButton().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (!getInitView().getMsgTextField().getText().equals("  Mensaje..."))
                    enviarMensaje();
            }
        });
        this.initView.getChatList().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String contactoSeleccionado = initView.getChatList().getSelectedValue();
                if (contactoSeleccionado != null) {
                    actualizaChatPanel(contactoSeleccionado);
                    ((ConversacionRenderer) initView.getChatList().getCellRenderer()).setMensajeNoLeido(contactoSeleccionado, false);
                    initView.getChatList().repaint();
                }
            }
        });
        this.initView.getNewContactButton().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Point posicionActual = initView.getLocation();
                contactView.setLocation(posicionActual);
                contactView.setVisible(true);
            }
        });
        this.initView.getNewConvButton().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Point posicionActual = initView.getLocation();
                chatView.setLocation(posicionActual);
                chatView.setVisible(true);
            }
        });
    }

    public boolean validarCredenciales(String usuario) {
        return !usuario.isEmpty();
    }
    

    @Override
    public void autenticarUsuario() {
        String usuario = loginView.getUserTxt().getText().trim();
        if (usuario.isEmpty() || usuario.equals("Ingrese su nombre de usuario...")) {
            JOptionPane.showMessageDialog(loginView, "Debes ingresar usuario.");
            return;
        }
        try {
            if (validarCredenciales(usuario)) {
                this.usuarioActual = usuario;
                gestored.usuarioOnline(usuario, "server1");
                
                String formato = loginView.getFormatoComboBox().getSelectedItem().toString().toLowerCase();
                IFabricaPersistencia fabrica = FabricaPersistencia.obtenerFabrica(formato, usuario);

                // Guardador de mensajes
                GuardadorMensaje guardador = fabrica.crearGuardadorMensaje(usuario);
                gestormensajes.setGuardador(guardador);
                Map<String, List<IMensaje>> mensajesPrevios = guardador.cargarMensajes();
                gestormensajes.setMensajes(mensajesPrevios);
                gestored.setConexionesActivas(new ArrayList<>(mensajesPrevios.keySet()));

                // Guardador de contactos 
                GuardadorContacto guardadorContacto = fabrica.crearGuardadorContacto(usuario);
                gestorcontactos.setGuardador(guardadorContacto); 
                List<Contacto> contactos = guardadorContacto.cargarContactos();
                gestorcontactos.setContactos(contactos); 

                actualizaListaContactos();
                refreshConversaciones();
                Point posicionActual = loginView.getLocation();
                loginView.setVisible(false);
                initView.setLocation(posicionActual);
                getInitView().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(loginView, "Puerto invalido o en uso.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(loginView, "El puerto debe ser un número válido.");
        }
    }

    @Override
    public void agregarNuevoContacto() {
        String nombre = contactView.getNameTxtField().getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(contactView, "El nombre de usuario es obligatorio.");
            return;
        }
        Contacto c = new Contacto(nombre,"localhost",3333); 
        gestored.checkDir(c);
    }
    
    @Override
    public void agregadoExitoso(){
        JOptionPane.showMessageDialog(contactView, "Contacto agregado exitosamente.");
        actualizaListaContactos();
        contactView.setVisible(false);
        contactView.limpiarTextFields();
    }
        
    @Override
    public void agregadoRepetido(){
        JOptionPane.showMessageDialog(contactView, "El contacto ya existe.");
        contactView.limpiarTextFields();
    }
        
    @Override
    public void actualizaListaContactos(){
        this.chatView.actualizarListaContactos(gestorcontactos.getListaContactos());
    }

    @Override
    public void iniciarChatConSeleccion() {
        if (!gestored.isSocket()){
            mostrarCartelErrorConexion();
            chatView.getContactList().clearSelection();
            return;
        }
        String contactoSeleccionado = chatView.getContactList().getSelectedValue();
        if (contactoSeleccionado != null) {
            if (!gestored.estaConectado(contactoSeleccionado)) {
                String[] datosContacto = gestorcontactos.obtenerDatosContacto(contactoSeleccionado);
                if (datosContacto != null) {
                    String ip = datosContacto[0];
                    int puerto = Integer.parseInt(datosContacto[1]);
                    gestored.iniciarConexionCliente(contactoSeleccionado, ip, puerto, usuarioActual);
                    chatView.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(chatView, "No se encontraron datos del contacto.");
                }
            } else {
                JOptionPane.showMessageDialog(chatView, "Ya tienes una conversación activa con este contacto.");
            }
        }
        chatView.getContactList().clearSelection();
    }

    @Override
    public void enviarMensaje() {
        IMensaje mensaje = FabricaMensajes.getInstancia().creaMensaje();
        String mensajeTexto = getInitView().getMsgTextField().getText().trim();
        mensaje.setReceptor(getInitView().getChatList().getSelectedValue());
        mensaje.setMensaje(mensajeTexto);

        if (mensaje.getReceptor() == null) {
            JOptionPane.showMessageDialog(getInitView(), "Seleccione un contacto antes de enviar un mensaje.");
            return;
        }

        if (mensajeTexto.isEmpty()) {
            JOptionPane.showMessageDialog(getInitView(), "El mensaje no puede estar vacío.");
            return;
        }

        mensaje.setHora(new java.text.SimpleDateFormat("HH:mm").format(new java.util.Date()));
        mensaje.setIpEmisor(gestored.obtenerIPLocal());
        mensaje.setNombreEmisor(this.usuarioActual);
        mensaje.setTipo("texto");
        gestormensajes.agregaMensaje(mensaje.getReceptor() ,mensaje);
        try {
            mensaje.setMensaje(this.getContextocifrado().cifrarMensaje(mensajeTexto, this.contextocifrado.crearClave(ConfigLoader.getProperty(usuarioActual,"clave"))));
            System.out.println("Mensaje cifrado:" + mensajeTexto);
        } catch (Exception ex) {
            Logger.getLogger(GestorInterfaz.class.getName()).log(Level.SEVERE, null, ex);
        }

        gestored.enviarMensaje(mensaje);
        
        mensaje.setMensaje(mensajeTexto);
        mostrarMensajeEnChat(mensaje);
        getInitView().getMsgTextField().setText("  Mensaje...");
        getInitView().getMsgTextField().setForeground(new Color(204, 204, 204));
        SwingUtilities.invokeLater(() -> {
            initView.getChatPanel().requestFocusInWindow();
        }); 
    }

    @Override
    public void borraChat(String contacto) {
        String receptoractual = getInitView().getChatList().getSelectedValue();
        if (!(receptoractual == null)){
            if (receptoractual.equals(contacto)){
                getInitView().getChatPanel().removeAll();
                getInitView().getChatPanel().revalidate();
                getInitView().getChatPanel().repaint();
            }
        }
    }
    
    @Override
    public void borraChat() {
        getInitView().getChatPanel().removeAll();
        getInitView().getChatPanel().revalidate();
        getInitView().getChatPanel().repaint();
    }
    
    @Override
    public void mostrarMensajeEnChat(IMensaje mensaje) {
        String receptoractual = getInitView().getChatList().getSelectedValue();
        
        boolean esMensajePropio = mensaje.getIpEmisor().equals(gestored.obtenerIPLocal()) && mensaje.getNombreEmisor().equals(usuarioActual);
        ConversacionRenderer renderer = (ConversacionRenderer) initView.getChatList().getCellRenderer();
        
        renderer.setUltimoMensaje(mensaje.getNombreEmisor(), mensaje.getMensaje(), mensaje.getHora()); // actualizo ultimo mensaje y hora

        if ((receptoractual != null && receptoractual.equals(mensaje.getNombreEmisor())) || esMensajePropio) {
            initView.addChatBubble(mensaje.getMensaje(), mensaje.getHora(), esMensajePropio);
        } else {
            renderer.setMensajeNoLeido(mensaje.getNombreEmisor(), true);
        }
        initView.getChatList().repaint();
    }

    @Override
    public void actualizaChatPanel(String nombre) {
        List<IMensaje> copiamensajes = gestormensajes.getMensajesDe(nombre);
        List<IMensaje> listamensajes = (copiamensajes != null) ? new ArrayList<>(copiamensajes) : new ArrayList<>();
        getInitView().getChatPanel().removeAll();
        if (listamensajes.isEmpty()) {
            getInitView().getChatPanel().revalidate();
            getInitView().getChatPanel().repaint();
            return;
        }
        for (IMensaje mensaje : listamensajes) {
            this.mostrarMensajeEnChat(mensaje);
        }
        getInitView().getChatPanel().revalidate();
        getInitView().getChatPanel().repaint();
    }

    @Override
    public void refreshConversaciones() {
        getInitView().actualizaChats(gestored.getListaConexiones());
    }

    @Override
    public Init getInitView() {
        return initView;
    }
        
    @Override
    public void mostrarCartelErrorConexion(){
        JOptionPane.showMessageDialog(chatView, "Servidor desconectado.");
    }
    
    @Override
    public void mostrarCartelErrorUsuarioConectado(){
        JOptionPane.showMessageDialog(chatView, "Usuario ya conectado.");
        initView.setVisible(false);
        loginView.getUserTxt().setText("Ingrese su nombre de usuario...");
        loginView.getUserTxt().setForeground(new Color(204,204,204));
        loginView.setVisible(true);
    }
    
    @Override
    public void mostrarCartelErrorDir(){
        JOptionPane.showMessageDialog(contactView, "Contacto no se encuentra en el directorio");
    }

    public ContextoCifrado getContextocifrado() {
        return contextocifrado;
    }
}
