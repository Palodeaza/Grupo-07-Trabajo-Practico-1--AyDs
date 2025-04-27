package vistas;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class AnimatedLabel extends JLabel {
    private float progress = 0f;
    private Timer timer;
    private boolean hovered = false;
    private boolean isActive = true; 

    public AnimatedLabel(String text) {
        super(text, SwingConstants.CENTER);
        setOpaque(false);
        setForeground(Color.WHITE);
        setFont(new Font("Roboto", Font.PLAIN, 14));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setVerticalAlignment(SwingConstants.CENTER);

        timer = new Timer(15, e -> {
            if (hovered && isActive) { // Solo animar si está activo
                progress = Math.min(1f, progress + 0.05f);
            } else {
                progress = Math.max(0f, progress - 0.05f);
            }
            repaint();
            if (progress == 0f || progress == 1f) {
                timer.stop();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isActive) {
                    hovered = true;
                    timer.start();
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                timer.start();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        int w = getWidth(), h = getHeight();
        Graphics2D g2 = (Graphics2D) g.create();

        Color start = new Color(0, 0, 102);
        Color end = new Color(0, 102, 204);
        int r = (int)(start.getRed() + (end.getRed() - start.getRed()) * progress);
        int gr = (int)(start.getGreen() + (end.getGreen() - start.getGreen()) * progress);
        int b = (int)(start.getBlue() + (end.getBlue() - start.getBlue()) * progress);

        // Fondo
        g2.setColor(new Color(r, gr, b));
        g2.fillRect(0, 0, w, h);

        // Seteo la fuente personalizada
        g2.setFont(this.getFont()); // Esto sí respeta Roboto
        g2.setColor(Color.WHITE);

        // Centrado del texto
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(getText());
        int textHeight = fm.getAscent();

        int x = (w - textWidth) / 2;
        int y = (h + textHeight) / 2 - 4;

        // Dibujar el texto
        g2.drawString(getText(), x, y);

        g2.dispose();
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.isActive = enabled;
        if (!enabled) {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)); 
            hovered = false;
            progress = 0f;
            timer.stop();
            repaint();
        } else {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
        super.setEnabled(enabled);
    }
}