package vistas;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ConversacionRenderer extends JPanel implements ListCellRenderer<String> {
    private JLabel imagenPerfil;
    private JLabel nombreContacto;
    private JLabel contadorMensajes;
    private int hoveredIndex = -1;

    private Map<String, Integer> mensajesNoLeidos = new HashMap<>();

    public ConversacionRenderer() {
        setLayout(new BorderLayout(10, 0)); // Separación entre imagen y texto
        setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        setBackground(new Color(0, 0, 102));

        imagenPerfil = new JLabel();
        imagenPerfil.setPreferredSize(new Dimension(32, 32));
        imagenPerfil.setIcon(redimensionarIcono(new ImageIcon("src\\main\\java\\vistas\\images-removebg-preview.png"), 32, 32));

        nombreContacto = new JLabel();
        nombreContacto.setFont(new Font("Roboto", Font.BOLD, 14));
        nombreContacto.setForeground(Color.WHITE);

        contadorMensajes = new JLabel();
        contadorMensajes.setFont(new Font("Roboto", Font.BOLD, 14));
        contadorMensajes.setForeground(Color.WHITE);
        contadorMensajes.setVisible(false);

        JPanel centroPanel = new JPanel(new BorderLayout());
        centroPanel.setOpaque(false);
        centroPanel.add(nombreContacto, BorderLayout.WEST);
        centroPanel.add(contadorMensajes, BorderLayout.EAST);

        add(imagenPerfil, BorderLayout.WEST);
        add(centroPanel, BorderLayout.CENTER);
        setPreferredSize(new Dimension(0, 50));

    }

    private Icon redimensionarIcono(ImageIcon icono, int ancho, int alto) {
        Image imagen = icono.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new ImageIcon(imagen);
    }

    public void setHoveredIndex(int index) {
        hoveredIndex = index;
    }

    public void setMensajeNoLeido(String contacto, boolean estado) {
        int cantidad;
        if (estado) {
            cantidad = mensajesNoLeidos.getOrDefault(contacto, 0) + 1;
        } else {
            cantidad = 0;
        }
        mensajesNoLeidos.put(contacto, cantidad);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
        nombreContacto.setText(value);

        int cantidadNoLeidos = mensajesNoLeidos.getOrDefault(value, 0);
        if (isSelected) {
            mensajesNoLeidos.put(value, 0); // Marcar como leídos
            cantidadNoLeidos = 0;
        }

        if (cantidadNoLeidos > 0) {
            contadorMensajes.setText(String.valueOf(cantidadNoLeidos));
            contadorMensajes.setVisible(true);
        } else {
            contadorMensajes.setVisible(false);
        }

        // Colores de fondo
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