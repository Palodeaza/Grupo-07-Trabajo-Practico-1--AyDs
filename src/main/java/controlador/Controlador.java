package controlador;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import modelo.Modelo;
import vistas.ConversacionRenderer;
import vistas.Init;
import vistas.Login;
import vistas.RoundedPanel;
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
        this.modelo.setMensajeListener(mensaje -> mostrarMensajeEnChat2(mensaje));
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

    private void agregarNuevoContacto() {
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
                this.chatView.actualizarListaContactos(modelo.getListaContactos());
                contactView.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(contactView, "El contacto ya existe.");
            }
            contactView.limpiarTextFields();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(contactView, "El puerto debe ser un numero válido.");
        }
    }
    
    public void mostrarCartelErrorConexion(){
        JOptionPane.showMessageDialog(chatView, "Contacto no conectado.");
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
                JOptionPane.showMessageDialog(chatView, "Ya tienes una conversación activa con este contacto.");
            }
        }
    }

    private void enviarMensaje() {
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

        String mensajeFormateado = nombre + ":" + ipEmisor + ":" + puertoActual + ";" + mensajeTexto + ";" + horaActual;

        modelo.getMensajes().computeIfAbsent(receptor, k -> new java.util.ArrayList<>()).add(mensajeFormateado);
        modelo.enviarMensaje(receptor, mensajeFormateado);
        mostrarMensajeEnChat2(mensajeFormateado);
        getInitView().getMsgTextField().setText("  Mensaje...");
        getInitView().getMsgTextField().setForeground(new Color(204, 204, 204));
    }

    /*private void mostrarMensajeEnChat(String mensaje) {
        String receptoractual = getInitView().getChatList().getSelectedValue();
        modelo.getMensajes().computeIfAbsent(receptoractual, k -> new java.util.ArrayList<>()).add(mensaje);
        String[] partes = mensaje.split(";", 3);
        if (partes.length < 3) {
            System.out.println("Error: Formato de mensaje incorrecto.");
            return;
        }
        String mensajeTexto = partes[1];
        String horaMensaje = partes[2];
        String[] datos = partes[0].split(":", 3); // datos[0]=nombre, datos[1]=ip, datos[2]=puerto
        if (datos.length < 2) {
            System.out.println("Error: Datos de remitente incompletos.");
            return;
        }
        String remitente = modelo.buscaContacto(datos[1], datos[2]); // método que retorna el nombre según ip y puerto
        boolean esMensajePropio = datos[1].equals(modelo.obtenerIPLocal()) && datos[2].equals(String.valueOf(puertoActual));
        if ((receptoractual != null && receptoractual.equals(remitente) )|| esMensajePropio) {

        javax.swing.SwingUtilities.invokeLater(() -> {
            javax.swing.JLabel mensajeLabel = new javax.swing.JLabel("<html><body style='width:200px; margin: 2px; padding: 2px;'>" + mensajeTexto + "</body></html>");
            mensajeLabel.setForeground(java.awt.Color.WHITE);
            mensajeLabel.setOpaque(false);
            mensajeLabel.setFont(new Font("Roboto", Font.PLAIN, 14));

            javax.swing.JLabel horaLabel = new javax.swing.JLabel(horaMensaje);
            horaLabel.setForeground(java.awt.Color.GRAY);
            horaLabel.setOpaque(false);
            horaLabel.setFont(new Font("Roboto", Font.ITALIC, 10));
            horaLabel.setVisible(false);

            javax.swing.JPanel messagePanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 2));
            messagePanel.setOpaque(false);
            messagePanel.add(mensajeLabel);
            messagePanel.add(horaLabel);

            messagePanel.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    horaLabel.setVisible(true);
                    messagePanel.revalidate();
                    messagePanel.repaint();
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    horaLabel.setVisible(false);
                    messagePanel.revalidate();
                    messagePanel.repaint();
                }
            });

            RoundedPanel bubblePanel = new RoundedPanel(15, new java.awt.Color(0, 0, 102));
            bubblePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 2));
            bubblePanel.add(messagePanel);

            javax.swing.JPanel wrapper = new javax.swing.JPanel(new java.awt.FlowLayout(esMensajePropio ? java.awt.FlowLayout.RIGHT : java.awt.FlowLayout.LEFT, 5, 2));
            wrapper.setOpaque(false);
            wrapper.add(bubblePanel);
            
            getInitView().getChatPanel().add(wrapper);
            getInitView().getChatPanel().revalidate();
            getInitView().getChatPanel().repaint();
        });
        }else{
            ((ConversacionRenderer) initView.getChatList().getCellRenderer()).setMensajeNoLeido(remitente, true);//pone el puntito d msj sin leer
            initView.getChatList().repaint();
        }
    }*/
    private void mostrarMensajeEnChat2(String mensaje) {
        String receptoractual = getInitView().getChatList().getSelectedValue();
        String[] partes = mensaje.split(";", 3);
        if (partes.length < 3) {
            System.out.println("Error: Formato de mensaje incorrecto.");
            return;
        }
        String mensajeTexto = partes[1];
        String horaMensaje = partes[2];
        String[] datos = partes[0].split(":", 3); // datos[0]=nombre, datos[1]=ip, datos[2]=puerto
        if (datos.length < 2) {
            System.out.println("Error: Datos de remitente incompletos.");
            return;
        }
        String remitente = modelo.buscaContacto(datos[1], datos[2]); // método que retorna el nombre según ip y puerto
        boolean esMensajePropio = datos[1].equals(modelo.obtenerIPLocal()) && datos[2].equals(String.valueOf(puertoActual));
        if ((receptoractual != null && receptoractual.equals(remitente) )|| esMensajePropio) {

        javax.swing.SwingUtilities.invokeLater(() -> {
            JLabel mensajeLabel = new JLabel("<html><body style='width:200px; margin:2px; padding:2px;'>" 
                                             + mensajeTexto + "</body></html>");
            mensajeLabel.setForeground(Color.WHITE);
            mensajeLabel.setFont(new Font("Roboto", Font.PLAIN, 14));

            JLabel horaLabel = new JLabel(horaMensaje);
            horaLabel.setForeground(Color.GRAY);
            horaLabel.setFont(new Font("Roboto", Font.ITALIC, 10));
            horaLabel.setVisible(false);

            RoundedPanel bubblePanel = new RoundedPanel(15, new Color(0, 0, 102));
            bubblePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
            bubblePanel.add(mensajeLabel);
            bubblePanel.setOpaque(false);

            bubblePanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    horaLabel.setVisible(true);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    horaLabel.setVisible(false);
                }
            });

            JPanel wrapper = new JPanel(new FlowLayout(
                    esMensajePropio ? FlowLayout.RIGHT : FlowLayout.LEFT, 
                    5, 2));
            wrapper.setOpaque(false);

            if (esMensajePropio) {
                wrapper.add(horaLabel);
                wrapper.add(bubblePanel);
            } else {
                wrapper.add(bubblePanel);
                wrapper.add(horaLabel);
            }

            getInitView().getChatPanel().add(wrapper);
            getInitView().getChatPanel().revalidate();
            getInitView().getChatPanel().repaint();
        });
        }else{
            ((ConversacionRenderer) initView.getChatList().getCellRenderer()).setMensajeNoLeido(remitente, true);//pone el puntito d msj sin leer
            initView.getChatList().repaint();
        }
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
            this.mostrarMensajeEnChat2(mensaje);
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
}
