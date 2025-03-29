package vistas;
import javax.swing.*;
import java.awt.*;

public class ConversacionRenderer extends JPanel implements ListCellRenderer<String> {
    private JLabel nombreContacto;
    private JLabel mensajeIndicador;
    private int hoveredIndex = -1; // Índice del elemento resaltado

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
        this.hoveredIndex = index;
    }

    @Override
    public Component getListCellRendererComponent(
            JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {

        nombreContacto.setText(value);
        mensajeIndicador.setVisible(index % 2 == 0);

        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE));

        if (isSelected) {
            setBackground(new Color(0, 102, 204)); // Azul al seleccionar
        } else if (index == hoveredIndex) {
            setBackground(new Color(50, 50, 150)); // Azul más claro al pasar el mouse
        } else {
            setBackground(new Color(0, 0, 102)); // Azul oscuro por defecto
        }

        return this;
    }
}