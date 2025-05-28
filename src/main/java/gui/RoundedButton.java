package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class RoundedButton extends JButton {
    private Color normalBackgroundColor;
    private Color hoverBackgroundColor;
    private Color pressedBackgroundColor;
    // private Color foregroundColor; // Use getForeground() directly
    private int cornerRadius;
    private boolean useCustomBgColor = false; // Flag to indicate custom background

    public RoundedButton(String text, boolean isPrimary) {
        super(text);
        this.cornerRadius = AuthFrame.COMPONENT_CORNER_RADIUS;
        this.useCustomBgColor = false; // Explicitly false for this constructor

        if (isPrimary) {
            this.normalBackgroundColor = AuthFrame.BUTTON_COLOR_LIGHT;
            this.hoverBackgroundColor = AuthFrame.BUTTON_COLOR_LIGHT.brighter();
            this.pressedBackgroundColor = AuthFrame.BUTTON_COLOR_LIGHT.darker();
            setForeground(AuthFrame.BUTTON_FOREGROUND_DARK);
        } else {
            this.normalBackgroundColor = AuthFrame.BUTTON_COLOR_DARK;
            this.hoverBackgroundColor = AuthFrame.BUTTON_COLOR_DARK.brighter();
            this.pressedBackgroundColor = AuthFrame.BUTTON_COLOR_DARK.darker();
            setForeground(AuthFrame.FOREGROUND_COLOR);
        }
        
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setFont(new Font("Arial", Font.BOLD, 13));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // No need to check useCustomBgColor here, as this constructor doesn't use it for bg logic
                setBackground(hoverBackgroundColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(normalBackgroundColor);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(pressedBackgroundColor);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (getModel().isRollover()) {
                    setBackground(hoverBackgroundColor);
                } else {
                    setBackground(normalBackgroundColor);
                }
            }
        });
        setBackground(normalBackgroundColor);
    }

    public RoundedButton(String text, Color customBackgroundColor, Color customForegroundColor) {
        super(text);
        this.cornerRadius = AuthFrame.COMPONENT_CORNER_RADIUS;
        this.useCustomBgColor = true; // This constructor uses custom colors

        this.normalBackgroundColor = customBackgroundColor;
        // For custom, ensure hover/pressed are distinct but related
        this.hoverBackgroundColor = customBackgroundColor.brighter(); 
        if (this.hoverBackgroundColor.equals(customBackgroundColor)) { // If brighter() didn't change (e.g. white)
            this.hoverBackgroundColor = new Color(
                Math.max(0, customBackgroundColor.getRed() - 20),
                Math.max(0, customBackgroundColor.getGreen() - 20),
                Math.max(0, customBackgroundColor.getBlue() - 20)
            );
        }
        this.pressedBackgroundColor = customBackgroundColor.darker();
         if (this.pressedBackgroundColor.equals(customBackgroundColor)) { // If darker() didn't change (e.g. black)
            this.pressedBackgroundColor = new Color(
                Math.min(255, customBackgroundColor.getRed() + 20),
                Math.min(255, customBackgroundColor.getGreen() + 20),
                Math.min(255, customBackgroundColor.getBlue() + 20)
            );
        }

        setForeground(customForegroundColor);

        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        // For icon buttons, font style might be less critical or set specifically later
        setFont(new Font("Arial Unicode MS", Font.PLAIN, 16)); 


        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverBackgroundColor); // Always use the derived hover for custom
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(normalBackgroundColor); // Always use the derived normal for custom
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(pressedBackgroundColor); // Always use the derived pressed for custom
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (getModel().isRollover()) {
                    setBackground(hoverBackgroundColor);
                } else {
                    setBackground(normalBackgroundColor);
                }
            }
        });
        setBackground(normalBackgroundColor); // Set initial background
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setColor(getBackground()); 
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));
        
        String buttonText = getText();
        if (buttonText != null && !buttonText.isEmpty()) {
            FontMetrics fm = g2.getFontMetrics(getFont());
            Rectangle stringBounds = fm.getStringBounds(buttonText, g2).getBounds();
            
            int textX = (getWidth() - stringBounds.width) / 2;
            int textY = (getHeight() - stringBounds.height) / 2 + fm.getAscent();
            
            g2.setColor(getForeground());
            g2.setFont(getFont());
            g2.drawString(buttonText, textX, textY);
        }
        
        g2.dispose();
    }
    
    @Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize(); 
        FontMetrics fm = getFontMetrics(getFont());
        // For icon buttons, we might want a more square-like appearance or fixed size
        // For text buttons, calculate width based on text
        if (getText() != null && !getText().isEmpty() && getText().length() > 2) { // Arbitrary length to distinguish text from icon
             int textWidth = fm.stringWidth(getText());
             size.width = textWidth + 30; // Horizontal padding (15 each side)
        } else {
            // For icon-like buttons (e.g., single char like globe), make it more square
            size.width = 35; 
        }
        size.height = 35; // Fixed height
        return size;
    }
} 