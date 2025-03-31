package vistas;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ConversacionRenderer extends JPanel implements ListCellRenderer<String> {
    private JLabel nombreContacto;
    private JLabel mensajeIndicador;
    private int hoveredIndex = -1;
    private Map<String, Boolean> mensajesNoLeidos = new HashMap<>();

    public ConversacionRenderer() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        nombreContacto = new JLabel();
        nombreContacto.setFont(new Font("Roboto", Font.BOLD, 14));
        nombreContacto.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        nombreContacto.setForeground(Color.WHITE);

        mensajeIndicador = new JLabel("•");
        mensajeIndicador.setFont(new Font("Arial", Font.BOLD, 16));
        mensajeIndicador.setForeground(Color.WHITE);
        mensajeIndicador.setVisible(false);
        mensajeIndicador.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));

        add(nombreContacto, BorderLayout.WEST);
        add(mensajeIndicador, BorderLayout.EAST);
    }

    public void setHoveredIndex(int index) {
        hoveredIndex = index;
    }

    public void setMensajeNoLeido(String contacto, boolean noLeido) {
        mensajesNoLeidos.put(contacto, noLeido);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
        nombreContacto.setText(value);
        if (isSelected) {
            mensajesNoLeidos.put(value, false);
        }
        boolean tieneMensajesNoLeidos = mensajesNoLeidos.getOrDefault(value, false);
        mensajeIndicador.setVisible(tieneMensajesNoLeidos);
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE));
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