package vistas;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class AnimatedLabel extends JLabel {
    private float progress = 0f;
    private Timer timer = null;
    private boolean hovered = false;

    public AnimatedLabel(String text) {
        super(text, SwingConstants.CENTER);
        setOpaque(false);                
        setForeground(Color.WHITE);
        setFont(new Font("Roboto", Font.PLAIN, 14));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setVerticalAlignment(SwingConstants.CENTER);

        timer = new Timer(15, e -> {
            if (hovered) {
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
                hovered = true;
                timer.start();
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
        Color end   = new Color(0, 102, 204);
        int r  = (int)(start.getRed()   + (end.getRed()   - start.getRed())   * progress);
        int gr = (int)(start.getGreen() + (end.getGreen() - start.getGreen()) * progress);
        int b  = (int)(start.getBlue()  + (end.getBlue()  - start.getBlue())  * progress);

        g2.setColor(new Color(r, gr, b));
        g2.fillRect(0, 0, w, h);
        g2.dispose();

        super.paintComponent(g);
    }
}