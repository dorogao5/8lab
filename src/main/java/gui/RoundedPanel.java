package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedPanel extends JPanel {
    private Color backgroundColor;
    private int cornerRadius;
    private Color borderColor;
    private int borderThickness;

    public RoundedPanel(LayoutManager layout, int radius, Color bgColor) {
        this(layout, radius, bgColor, null, 0);
    }

    public RoundedPanel(int radius, Color bgColor) {
        this(null, radius, bgColor, null, 0);
    }

    public RoundedPanel(LayoutManager layout, int radius, Color bgColor, Color borderColor, int borderThickness) {
        super(layout);
        this.cornerRadius = radius;
        this.backgroundColor = bgColor;
        this.borderColor = borderColor;
        this.borderThickness = borderThickness;
        setOpaque(false);
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
        repaint();
    }

    public void setBorderColor(Color color) {
        this.borderColor = color;
        repaint();
    }

    public void setBorderThickness(int thickness) {
        this.borderThickness = thickness;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics = (Graphics2D) g.create();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        float outerArc = this.cornerRadius;

        Color currentBgColor = this.backgroundColor != null ? this.backgroundColor : getBackground();

        if (borderColor != null && borderThickness > 0 && borderThickness < Math.min(width, height) / 2.0f) {
            graphics.setColor(borderColor);
            graphics.fill(new RoundRectangle2D.Float(0, 0, width, height, outerArc, outerArc));

            float innerArc = Math.max(0.0f, outerArc - 2.0f * borderThickness);

            graphics.setColor(currentBgColor);
            graphics.fill(new RoundRectangle2D.Float(
                borderThickness,
                borderThickness,
                width - 2.0f * borderThickness,
                height - 2.0f * borderThickness,
                innerArc,
                innerArc
            ));
        } else {
            graphics.setColor(currentBgColor);
            graphics.fill(new RoundRectangle2D.Float(0, 0, width, height, outerArc, outerArc));
        }
        
        graphics.dispose();
    }
} 