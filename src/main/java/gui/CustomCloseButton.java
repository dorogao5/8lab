package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

public class CustomCloseButton extends JButton {
    private Color normalBackgroundColor = new Color(80, 80, 80); // Slightly darker
    private Color hoverBackgroundColor = new Color(110, 110, 110);
    private Color pressedBackgroundColor = new Color(60, 60, 60);
    private Color crossColor = new Color(220, 220, 220); // Brighter cross
    private boolean hovered = false;

    public CustomCloseButton() {
        super("X");
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setPreferredSize(new Dimension(26, 26)); // Size adjustment
        setForeground(crossColor); 
        setFont(new Font("Arial", Font.BOLD, 11)); // Font for X

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                setBackground(hoverBackgroundColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                setBackground(normalBackgroundColor);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(pressedBackgroundColor);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (hovered) {
                    setBackground(hoverBackgroundColor);
                } else {
                    setBackground(normalBackgroundColor);
                }
            }
        });
        setBackground(normalBackgroundColor);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fill(new Ellipse2D.Float(1, 1, getWidth() - 2, getHeight() - 2)); // Inset slightly for crisper edge

        g2.setColor(getForeground());
        g2.setStroke(new BasicStroke(2f)); 
        int offset = (int) (getWidth() / 3.6); // Adjusted for X drawing
        // Draw X centered
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int lineLength = getWidth() / 4; // Half-length of the X arms

        g2.drawLine(centerX - lineLength, centerY - lineLength, centerX + lineLength, centerY + lineLength);
        g2.drawLine(centerX - lineLength, centerY + lineLength, centerX + lineLength, centerY - lineLength);
        
        g2.dispose();
    }
} 