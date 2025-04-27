package vistas;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ConversacionRenderer extends JPanel implements ListCellRenderer<String> {
    private JLabel imagenPerfil;
    private JLabel nombreContacto;
    private JLabel previewLabel;
    private JLabel timeLabel;
    private JLabel contadorMensajes;
    private int hoveredIndex = -1;

    // Datos que mantenemos por contacto
    private Map<String, Integer> mensajesNoLeidos = new HashMap<>();
    private Map<String, String> ultimosMensajes   = new HashMap<>();
    private Map<String, String> horasUltimoMensaje = new HashMap<>();

    public ConversacionRenderer() {
        setLayout(new BorderLayout(10, 0));
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        setBackground(new Color(0, 0, 102));

        imagenPerfil = new JLabel();
        imagenPerfil.setPreferredSize(new Dimension(32, 32));
        imagenPerfil.setIcon(redimensionarIcono(
            new ImageIcon("src\\main\\java\\vistas\\images-removebg-preview.png"),
            32, 32));

        nombreContacto = new JLabel();
        nombreContacto.setFont(new Font("Roboto", Font.BOLD, 14));
        nombreContacto.setForeground(Color.WHITE);

        previewLabel = new JLabel();
        previewLabel.setFont(new Font("Roboto", Font.PLAIN, 12));
        previewLabel.setForeground(Color.LIGHT_GRAY);
        previewLabel.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.add(nombreContacto);
        textPanel.add(previewLabel);

        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Roboto", Font.PLAIN, 10));
        timeLabel.setForeground(Color.LIGHT_GRAY);
        timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        contadorMensajes = new JLabel();
        contadorMensajes.setFont(new Font("Roboto", Font.BOLD, 12));
        contadorMensajes.setForeground(Color.WHITE);
        contadorMensajes.setHorizontalAlignment(SwingConstants.RIGHT);
        contadorMensajes.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));

        JPanel eastPanel = new JPanel(new BorderLayout());
        eastPanel.setOpaque(false);
        eastPanel.add(timeLabel, BorderLayout.NORTH);
        eastPanel.add(contadorMensajes, BorderLayout.SOUTH);
        eastPanel.setPreferredSize(new Dimension(60, 0));

        add(imagenPerfil, BorderLayout.WEST);
        add(textPanel,   BorderLayout.CENTER);
        add(eastPanel,   BorderLayout.EAST);

        setPreferredSize(new Dimension(0, 50));
    }

    private Icon redimensionarIcono(ImageIcon icono, int ancho, int alto) {
        Image img = icono.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    public void setMensajeNoLeido(String contacto, boolean estado) {
        int cant = mensajesNoLeidos.getOrDefault(contacto, 0) + (estado ? 1 : 0);
        mensajesNoLeidos.put(contacto, estado ? cant : 0);
    }
    public void setUltimoMensaje(String contacto, String mensaje, String hora) {
        ultimosMensajes.put(contacto, mensaje);
        horasUltimoMensaje.put(contacto, hora);
    }
    public void setHoveredIndex(int index) {
        hoveredIndex = index;
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends String> list,
            String value, int index, boolean isSelected, boolean cellHasFocus) {

        nombreContacto.setText(value);

        String msg = ultimosMensajes.getOrDefault(value, "");
        if (msg.length() > 25) msg = msg.substring(0, 25) + "…";
        previewLabel.setText(msg);

        timeLabel.setText(horasUltimoMensaje.getOrDefault(value, ""));

        int cant = mensajesNoLeidos.getOrDefault(value, 0);
        if (isSelected) {
            cant = 0;
            mensajesNoLeidos.put(value, 0);
        }
        if (cant > 0) {
            contadorMensajes.setText(String.valueOf(cant));
            contadorMensajes.setVisible(true);
        } else {
            contadorMensajes.setVisible(false);
        }

        if (isSelected) {
            setBackground(new Color(0, 102, 204));
        } else if (index == hoveredIndex) {
            setBackground(new Color(50, 50, 150));
        } else {
            setBackground(new Color(0, 0, 102));
        }

        return this;
    }
}