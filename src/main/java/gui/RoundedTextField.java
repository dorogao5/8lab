package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedTextField extends JTextField {
    private Shape shape;
    private Color borderColor;
    private int cornerRadius;

    public RoundedTextField(int columns) {
        super(columns);
        // Use a slightly smaller radius for text fields if COMPONENT_CORNER_RADIUS is for pill buttons
        this.cornerRadius = AuthFrame.COMPONENT_CORNER_RADIUS - 2; // e.g., 17 - 2 = 15
        setBackground(AuthFrame.TEXT_FIELD_BACKGROUND);
        this.borderColor = AuthFrame.TEXT_FIELD_BORDER_COLOR;
        setOpaque(false); 
        setForeground(AuthFrame.FOREGROUND_COLOR);
        setCaretColor(AuthFrame.FOREGROUND_COLOR);
        setFont(new Font("Arial", Font.PLAIN, 13));
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Padding for text
        // Preferred height is managed by getPreferredSize or layout
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        size.height = 35; // Fixed height
        return size;
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, cornerRadius, cornerRadius));
        super.paintComponent(g2);
        g2.dispose();
    }

    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(1));
        g2.draw(new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, cornerRadius, cornerRadius));
        g2.dispose();
    }

    public boolean contains(int x, int y) {
        if (shape == null || !shape.getBounds().equals(getBounds())) {
            shape = new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, cornerRadius, cornerRadius);
        }
        return shape.contains(x, y);
    }
} 