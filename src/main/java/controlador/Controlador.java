package controlador;

import java.util.List;
import javax.swing.JOptionPane;
import modelo.Mensaje;
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
        this.modelo.setMensajeListener(mensaje -> mostrarMensajeEnChat(mensaje));
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
    }

    private void autenticarUsuario() {
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

                modelo.iniciarServidor(puerto);

                loginView.setVisible(false);
                initView.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(loginView, "Usuario o contraseña incorrectos.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(loginView, "El puerto debe ser un número válido.");
        }
    }
    
    private void agregarNuevoContacto() {
        String nombre = contactView.getNameTxtField().getText().trim();
        String ip = contactView.getIpTxtField().getText().trim();
        String puertoStr = contactView.getPortTxtField().getText().trim();

        if (nombre.isEmpty() || ip.isEmpty() || puertoStr.isEmpty()) { //falta agregar los mensajes base... maldito vago...
            JOptionPane.showMessageDialog(contactView, "Todos los campos son obligatorios.");
            return;
        }

        try {
            int puerto = Integer.parseInt(puertoStr);
            if (modelo.agregarContacto(nombre, ip, puerto)) {
                JOptionPane.showMessageDialog(contactView, "Contacto agregado exitosamente.");
                contactView.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(contactView, "El contacto ya existe.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(contactView, "El puerto debe ser un numero vlido.");
        }
    }

    private void iniciarChatConSeleccion() {
        String contactoSeleccionado = chatView.getContactList().getSelectedValue();

        if (contactoSeleccionado != null) {
            if (!modelo.conversacionActiva(contactoSeleccionado)) {
                String[] datosContacto = modelo.obtenerDatosContacto(contactoSeleccionado);
                if (datosContacto != null) {
                    String ip = datosContacto[0];
                    int puerto = Integer.parseInt(datosContacto[1]);

                    modelo.iniciarConexionCliente(contactoSeleccionado, ip, puerto);

                    chatView.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(chatView, "No se encontraron datos del contacto.");
                }
            } else {
                JOptionPane.showMessageDialog(chatView, "Ya tienes una conversacion activa con este contacto.");
            }
        }
    }
    
    private void enviarMensaje() {
        String mensajeTexto = initView.getMsgTextField().getText().trim();
        String receptor = initView.getChatList().getSelectedValue();

        if (receptor == null) {
            JOptionPane.showMessageDialog(initView, "Seleccione un contacto antes de enviar un mensaje.");
            return;
        }

        if (mensajeTexto.isEmpty()) {
            JOptionPane.showMessageDialog(initView, "El mensaje no puede estar vacio.");
            return;
        }

        Mensaje mensaje = new Mensaje(usuarioActual, receptor, mensajeTexto, new java.util.Date().toString());
        modelo.getMensajes().computeIfAbsent(receptor, k -> new java.util.ArrayList<>()).add(mensaje); //crea lista si no existe
        modelo.enviarMensaje(receptor, mensaje);
        mostrarMensajeEnChat(mensaje);
        initView.getMsgTextField().setText("  Mensaje...");
    }
    
    private void mostrarMensajeEnChat(Mensaje mensaje) {
        String receptoractual = initView.getChatList().getSelectedValue();
        modelo.getMensajes().computeIfAbsent(mensaje.getEmisor(), k -> new java.util.ArrayList<>()).add(mensaje);
        if (receptoractual != null && receptoractual.equals(mensaje.getEmisor())) {
            javax.swing.SwingUtilities.invokeLater(() -> {
                javax.swing.JLabel mensajeLabel = new javax.swing.JLabel(mensaje.getContenido());
                mensajeLabel.setOpaque(true);
                mensajeLabel.setBackground(java.awt.Color.LIGHT_GRAY);
                mensajeLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10));

                initView.getChatPanel().add(mensajeLabel);
                initView.getChatPanel().revalidate();
                initView.getChatPanel().repaint();
            });
        }
        else {
            ((ConversacionRenderer) initView.getChatList().getCellRenderer()).setMensajeNoLeido(mensaje.getEmisor(), true);//pone el puntito d msj sin leer
            initView.getChatList().repaint();
        }
    }

    public void actualizaChatPanel(String nombre) {
        List<Mensaje> listamensajes = modelo.getMensajes().get(nombre);
        initView.getChatPanel().removeAll();
        if (listamensajes == null || listamensajes.isEmpty()) {
            initView.getChatPanel().revalidate();
            initView.getChatPanel().repaint();
            return;
        }
        for (Mensaje mensaje : listamensajes) {
            this.mostrarMensajeEnChat(mensaje);
        }
        initView.getChatPanel().revalidate();
        initView.getChatPanel().repaint();
    }
}

