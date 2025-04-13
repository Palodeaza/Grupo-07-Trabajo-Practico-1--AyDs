package controlador;

import java.awt.Color;
import java.awt.Point;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import modelo.Contacto;

import modelo.Modelo;
import vistas.ConversacionRenderer;
import vistas.Init;
import vistas.Login;
import vistas.newChat;
import vistas.newContact;

public class Controlador {

    private Login loginView = null;
    private Init initView = null;
    private newContact contactView = null;
    private newChat chatView = null;
    private Modelo modelo = null;
    private String usuarioActual;
    private int puertoActual;

    public Controlador(Login login, Init init, newContact contact, newChat chat, Modelo modelo) {
        this.loginView = login;
        this.initView = init;
        this.contactView = contact;
        this.chatView = chat;
        this.modelo = modelo;
        
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

    public void autenticarUsuario() {
        String usuario = loginView.getUserTxt().getText().trim();
        String puertoStr = loginView.getPortTxt().getText().trim();

        if (usuario.isEmpty() || puertoStr.isEmpty() || usuario.equals("Ingrese su nombre de usuario...") || puertoStr.equals("Ingrese el puerto a escuchar...")) {
            JOptionPane.showMessageDialog(loginView, "Debes ingresar usuario y puerto.");
            return;
        }

        try {
            int puerto = Integer.parseInt(puertoStr);
            if (modelo.validarCredenciales(usuario, puerto)) {
                this.usuarioActual = usuario;
                this.puertoActual = puerto;

                modelo.usuarioOnline(usuario);
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
    /*
    public void agregarNuevoContacto() {
        String nombre = contactView.getNameTxtField().getText().trim();
        String ip = contactView.getIpTxtField().getText().trim();
        String puertoStr = contactView.getPortTxtField().getText().trim();

        if (nombre.isEmpty() || ip.isEmpty() || puertoStr.isEmpty()) {
            JOptionPane.showMessageDialog(contactView, "Todos los campos son obligatorios.");
            return;
        }

        try {
            int puerto = Integer.parseInt(puertoStr);
            if (puerto < 1 || puerto > 65535) {
                JOptionPane.showMessageDialog(contactView, "El puerto debe estar entre 1 y 65535.");
                return;
            }
            try {
                InetAddress.getByName(ip); // Si no lanza excepcion, es valida
            } catch (UnknownHostException e) {
                JOptionPane.showMessageDialog(contactView, "La dirección IP no es válida.");
                return;
            }
            if (modelo.agregarContacto(nombre, ip, puerto)) {
                JOptionPane.showMessageDialog(contactView, "Contacto agregado exitosamente.");
                actualizaListaContactos();
                contactView.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(contactView, "El contacto ya existe.");
            }
            contactView.limpiarTextFields();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(contactView, "El puerto debe ser un numero válido.");
        }
    }
    */
        public void agregarNuevoContacto() {
        String nombre = contactView.getNameTxtField().getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(contactView, "El nombre de usuario es obligatorio.");
            return;
        }
        /*
        if (!modelo.checkDir(nombre)){
            JOptionPane.showMessageDialog(contactView, "El contacto ingresado no existe.");
            return;
        }
        */
        /*if (modelo.agregarContacto(nombre)) {
            JOptionPane.showMessageDialog(contactView, "Contacto agregado exitosamente.");
            actualizaListaContactos();
            contactView.setVisible(false);
        } else {
            JOptionPane.showMessageDialog(contactView, "El contacto ya existe.");
        }
        contactView.limpiarTextFields();
        */
        Contacto c = new Contacto(nombre,"localhost",3333); //hardcodeado, podriamos agarrar de los txt fields igual...
        modelo.checkDir(c);
    }
        public void agregadoExitoso(){
            JOptionPane.showMessageDialog(contactView, "Contacto agregado exitosamente.");
            actualizaListaContactos();
            contactView.setVisible(false);
            contactView.limpiarTextFields();
        }
        
        public void agregadoRepetido(){
            JOptionPane.showMessageDialog(contactView, "El contacto ya existe.");
            contactView.limpiarTextFields();
        }
        
    public void actualizaListaContactos(){
        this.chatView.actualizarListaContactos(modelo.getListaContactos());
    }

    public void iniciarChatConSeleccion() {
        String contactoSeleccionado = chatView.getContactList().getSelectedValue();
        if (contactoSeleccionado != null) {
            if (!modelo.getListaConexiones().contains(contactoSeleccionado)) {
                String[] datosContacto = modelo.obtenerDatosContacto(contactoSeleccionado);
                if (datosContacto != null) {
                    String ip = datosContacto[0];
                    int puerto = Integer.parseInt(datosContacto[1]);
                    modelo.iniciarConexionCliente(contactoSeleccionado, ip, puerto, usuarioActual);
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
        String ipEmisor = modelo.obtenerIPLocal();
        String nombre = this.usuarioActual;

        String mensajeFormateado = "texto" + "/" + nombre + ":" + ipEmisor + ":" + puertoActual + ";" + mensajeTexto + ";" + horaActual + ";" + receptor;

        modelo.getMensajes().computeIfAbsent(receptor, k -> new java.util.ArrayList<>()).add(mensajeFormateado);
        modelo.enviarMensaje(receptor, mensajeFormateado);
        mostrarMensajeEnChat(mensajeFormateado);
        getInitView().getMsgTextField().setText("  Mensaje...");
        getInitView().getMsgTextField().setForeground(new Color(204, 204, 204));
        SwingUtilities.invokeLater(() -> {
            initView.getChatPanel().requestFocusInWindow();
        });

    }

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
        //String remitente = modelo.buscaContacto(datos[1], datos[2]); // método que retorna el nombre según ip y puerto que voy a buscar? si solo guardo nombre ahora
        String remitente = datos[0];
        boolean esMensajePropio = datos[1].equals(modelo.obtenerIPLocal()) && datos[2].equals(String.valueOf(puertoActual));
        ConversacionRenderer renderer = (ConversacionRenderer) initView.getChatList().getCellRenderer();

        renderer.setUltimoMensaje(remitente, mensajeTexto, horaMensaje);// actualizo ultimo mensaje y hora
        
        if ((receptoractual != null && receptoractual.equals(remitente)) || esMensajePropio) {
            initView.addChatBubble(mensajeTexto, horaMensaje, esMensajePropio);
        } else {
            renderer.setMensajeNoLeido(remitente, true);
        }
        initView.getChatList().repaint();
    }

    public void actualizaChatPanel(String nombre) {
        List<String> copiamensajes = modelo.getMensajes().get(nombre);
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

    public void refreshConversaciones() {
        getInitView().actualizaChats(modelo.getListaConexiones());
    }

    public Init getInitView() {
        return initView;
    }
        
    public void mostrarCartelErrorConexion(){
        JOptionPane.showMessageDialog(chatView, "Servidor desconectado.");
    }
    public void mostrarCartelErrorDir(){
        JOptionPane.showMessageDialog(contactView, "Contacto no se encuentra en el directorio");
    }
}
