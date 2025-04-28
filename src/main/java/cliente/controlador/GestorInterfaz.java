package controlador;

import java.awt.Color;
import java.awt.Point;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import modelo.Contacto;
import modelo.GestorContactos;
import modelo.GestorMensajes;
import modelo.IGestionContactos;
import modelo.IGestionMensajes;
import modelo.IGestionRed;
import vistas.ConversacionRenderer;
import vistas.Init;
import vistas.Login;
import vistas.newChat;
import vistas.newContact;

public class GestorInterfaz implements IGestionInterfaz {

    private Login loginView = null;
    private Init initView = null;
    private newContact contactView = null;
    private newChat chatView = null;
    private IGestionContactos gestorcontactos;
    private IGestionMensajes gestormensajes;
    private IGestionRed gestored;
    private String usuarioActual;
    private int puertoActual;

    public GestorInterfaz(Login login, Init init, newContact contact, newChat chat, IGestionRed gestored, IGestionContactos gestorcontactos, IGestionMensajes gestormensajes) {
        this.loginView = login;
        this.initView = init;
        this.contactView = contact;
        this.chatView = chat;
        this.gestored = gestored;
        this.gestorcontactos = gestorcontactos;
        this.gestormensajes = gestormensajes;
        
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

    public boolean validarCredenciales(String usuario, int puerto) {
        if (usuario.isEmpty() || puerto <= 0) {
            return false;
        }
        ServerSocket testSocket = null;
        try {
            testSocket = new ServerSocket(puerto);
            return true; // Puerto disponible
        } catch (IOException e) {
            return false; // Puerto en uso
        } finally {
            if (testSocket != null) {
                try {
                    testSocket.close();
                } catch (IOException ignored) {}
            }
        }
    }

    @Override
    public void autenticarUsuario() {
        String usuario = loginView.getUserTxt().getText().trim();
        String puertoStr = loginView.getPortTxt().getText().trim();

        if (usuario.isEmpty() || puertoStr.isEmpty() || usuario.equals("Ingrese su nombre de usuario...") || puertoStr.equals("Ingrese el puerto a escuchar...")) {
            JOptionPane.showMessageDialog(loginView, "Debes ingresar usuario y puerto.");
            return;
        }

        try {
            int puerto = Integer.parseInt(puertoStr);
            if (validarCredenciales(usuario, puerto)) {
                this.usuarioActual = usuario;
                this.puertoActual = puerto;

                gestored.usuarioOnline(usuario);
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
        String mensajeTexto = getInitView().getMsgTextField().getText().trim();
        String receptor = getInitView().getChatList().getSelectedValue();

        if (receptor == null) {
            JOptionPane.showMessageDialog(getInitView(), "Seleccione un contacto antes de enviar un mensaje.");
            return;
        }

        if (mensajeTexto.isEmpty()) {
            JOptionPane.showMessageDialog(getInitView(), "El mensaje no puede estar vacío.");
            return;
        }

        String horaActual = new java.text.SimpleDateFormat("HH:mm").format(new java.util.Date());
        String ipEmisor = gestored.obtenerIPLocal();
        String nombre = this.usuarioActual;

        String mensajeFormateado = "texto" + "/" + nombre + ":" + ipEmisor + ":" + puertoActual + ";" + mensajeTexto + ";" + horaActual + ";" + receptor;

       gestormensajes.agregaMensaje(receptor, mensajeFormateado);
       gestored.enviarMensaje(receptor, mensajeFormateado);
        mostrarMensajeEnChat(mensajeFormateado);
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
    public void mostrarMensajeEnChat(String mensaje) {
        String receptoractual = getInitView().getChatList().getSelectedValue();
        String[] partes = mensaje.split(";", 4);
        if (partes.length < 3) {
            System.err.println("Error: Formato de mensaje incorrecto.");
            return;
        }
        String mensajeTexto = partes[1];
        String horaMensaje = partes[2];
        String[] datos = partes[0].split(":", 3); // datos[0]=nombre, datos[1]=ip, datos[2]=puerto
        if (datos.length < 2) {
            System.err.println("Error: Datos de remitente incompletos.");
            return;
        }
        
        String remitente = datos[0];
        boolean esMensajePropio = datos[1].equals(gestored.obtenerIPLocal()) && datos[2].equals(String.valueOf(puertoActual));
        ConversacionRenderer renderer = (ConversacionRenderer) initView.getChatList().getCellRenderer();

        renderer.setUltimoMensaje(remitente, mensajeTexto, horaMensaje);// actualizo ultimo mensaje y hora
        
        if ((receptoractual != null && receptoractual.equals(remitente)) || esMensajePropio) {
            initView.addChatBubble(mensajeTexto, horaMensaje, esMensajePropio);
        } else {
            renderer.setMensajeNoLeido(remitente, true);
        }
        initView.getChatList().repaint();
    }

    @Override
    public void actualizaChatPanel(String nombre) {
        List<String> copiamensajes = gestormensajes.getMensajesDe(nombre);
        List<String> listamensajes = (copiamensajes != null) ? new ArrayList<>(copiamensajes) : new ArrayList<>();
        getInitView().getChatPanel().removeAll();
        if (listamensajes.isEmpty()) {
            getInitView().getChatPanel().revalidate();
            getInitView().getChatPanel().repaint();
            return;
        }
        for (String mensaje : listamensajes) {
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
        loginView.getPortTxt().setText("Ingrese el puerto a escuchar...");
        loginView.getPortTxt().setForeground(new Color(204,204,204));
        loginView.getUserTxt().setText("Ingrese su nombre de usuario...");
        loginView.getUserTxt().setForeground(new Color(204,204,204));
        loginView.setVisible(true);
    }
    
    @Override
    public void mostrarCartelErrorDir(){
        JOptionPane.showMessageDialog(contactView, "Contacto no se encuentra en el directorio");
    }
}
