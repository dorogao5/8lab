package gui;

import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedBorder extends AbstractBorder {
    private Color color;
    private int thickness;
    private int radius;
    private Insets insets;

    public RoundedBorder(Color color, int thickness, int radius, Insets insets) {
        this.color = color;
        this.thickness = thickness;
        this.radius = radius;
        this.insets = insets;
    }

    public RoundedBorder(Color color, int thickness, int radius) {
        this(color, thickness, radius, new Insets(thickness + radius / 2, thickness + radius / 2, thickness + radius / 2, thickness + radius / 2));
        // Adjust insets to provide some padding for the text inside the rounded rect
    }


    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(this.color);
        g2.setStroke(new BasicStroke(this.thickness));
        // The RoundRectangle needs to be drawn inset by half the thickness for the stroke to be centered on the edge.
        // However, for a border, we usually draw it on the outer edge.
        g2.draw(new RoundRectangle2D.Float(x + thickness / 2.0f, y + thickness / 2.0f, 
                                           width - thickness, height - thickness, 
                                           radius, radius));
        g2.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return insets;
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = this.insets.left;
        insets.right = this.insets.right;
        insets.top = this.insets.top;
        insets.bottom = this.insets.bottom;
        return insets;
    }

    @Override
    public boolean isBorderOpaque() {
        return false; // True if the border paints every pixel in its area
    }
} 