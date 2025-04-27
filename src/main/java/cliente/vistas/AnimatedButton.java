package vistas;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class AnimatedButton extends JButton {
    private float animationProgress = 0f;
    private Timer timer = null;
    private boolean animating = false;
    private boolean hovered = false;

    public AnimatedButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setForeground(Color.WHITE);
        setFont(new Font("Roboto", Font.PLAIN, 14));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        timer = new Timer(15, e -> {
            if (hovered) {
                animationProgress += 0.05f;
                if (animationProgress >= 1f) {
                    animationProgress = 1f;
                    timer.stop();
                }
            } else {
                animationProgress -= 0.05f;
                if (animationProgress <= 0f) {
                    animationProgress = 0f;
                    timer.stop();
                }
            }
            repaint();
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

        // Interpolación de colores
        Color startColor = new Color(0, 0, 102);
        Color endColor = new Color(0, 102, 204);
        int r = (int) (startColor.getRed()   + (endColor.getRed()   - startColor.getRed())   * animationProgress);
        int gVal = (int) (startColor.getGreen() + (endColor.getGreen() - startColor.getGreen()) * animationProgress);
        int b = (int) (startColor.getBlue()  + (endColor.getBlue()  - startColor.getBlue())  * animationProgress);
        Color currentColor = new Color(r, gVal, b);

        g2.setColor(currentColor);
        g2.fillRect(0, 0, w, h);
        g2.dispose();

        super.paintComponent(g);
    }
}