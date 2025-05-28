package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;


public class AuthFrame extends JFrame {

    private CardLayout cardLayout;
    private RoundedPanel cardPanelContainer; // Changed from JPanel to RoundedPanel
    private InitialPanel initialPanel;
    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    private int mouseX, mouseY;

    // Panel names
    public static final String INITIAL_PANEL = "InitialPanel";
    public static final String LOGIN_PANEL = "LoginPanel";
    public static final String REGISTER_PANEL = "RegisterPanel";

    public static final Color MAIN_BACKGROUND_COLOR = new Color(50, 50, 50); // Dark gray
    public static final Color FOREGROUND_COLOR = Color.WHITE;
    public static final Color BUTTON_COLOR_LIGHT = new Color(220, 220, 220); // Light gray for main buttons
    public static final Color BUTTON_FOREGROUND_DARK = Color.BLACK;
    public static final Color BUTTON_COLOR_DARK = new Color(70, 70, 70); // Darker gray for secondary buttons
    public static final Color TEXT_FIELD_BACKGROUND = new Color(70, 70, 70);
    public static final Color TEXT_FIELD_BORDER_COLOR = new Color(100, 100, 100);
    public static final int PANEL_CORNER_RADIUS = 20; // Main window rounding
    public static final int COMPONENT_CORNER_RADIUS = 17; // For pill-shaped components (e.g., height 35)

    public AuthFrame() {
        setTitle("Аутентификация");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(340, 310); // Adjusted size: Width 340, Height 310
        setResizable(true); 
        setLocationRelativeTo(null);
        setUndecorated(true); 
        setBackground(new Color(0, 0, 0, 0)); // Прозрачный фон для скругленных углов
        getRootPane().putClientProperty("apple.awt.draggableWindowBackground", Boolean.FALSE);

        // Add mouse listeners for dragging
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int newX = getLocation().x + (e.getX() - mouseX);
                int newY = getLocation().y + (e.getY() - mouseY);
                setLocation(newX, newY);
            }
        });

        // Add component listener for window resizing
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), PANEL_CORNER_RADIUS, PANEL_CORNER_RADIUS));
            }
        });

        cardLayout = new CardLayout();
        cardPanelContainer = new RoundedPanel(cardLayout, PANEL_CORNER_RADIUS, MAIN_BACKGROUND_COLOR);
        initialPanel = new InitialPanel(this);
        loginPanel = new LoginPanel(this);
        registerPanel = new RegisterPanel(this);
        cardPanelContainer.add(initialPanel, INITIAL_PANEL);
        cardPanelContainer.add(loginPanel, LOGIN_PANEL);
        cardPanelContainer.add(registerPanel, REGISTER_PANEL);
        setContentPane(cardPanelContainer);
        showPanel(INITIAL_PANEL);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public void showPanel(String panelName) {
        cardLayout.show(cardPanelContainer, panelName);
    }
    
    // Centralized styling methods
    public static void styleDarkButton(JButton button, boolean primary) {
        // This method is largely obsolete due to RoundedButton
        if (primary) {
            button.setBackground(BUTTON_COLOR_LIGHT);
            button.setForeground(BUTTON_FOREGROUND_DARK);
        } else {
            button.setBackground(BUTTON_COLOR_DARK);
            button.setForeground(FOREGROUND_COLOR);
        }
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(button.getPreferredSize().width, 35)); // Standard height 35px
    }

    public static JLabel createHeaderLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20)); // Header font size
        label.setForeground(FOREGROUND_COLOR);
        label.setOpaque(false);
        return label;
    }
    
    public static JTextField createStyledTextField() {
        return new RoundedTextField(20); // Default columns
    }

    public static JPasswordField createStyledPasswordField() {
        return new RoundedPasswordField(20); // Default columns
    }

    // Method to show styled error dialogs
    public void showErrorDialog(String title, String message) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setUndecorated(true);
        dialog.setBackground(new Color(0, 0, 0, 0)); // Прозрачный фон для скругленных углов
        dialog.setSize(300, 160); // Slightly smaller dialog size
        dialog.setLocationRelativeTo(this);
        RoundedPanel dialogPanel = new RoundedPanel(new BorderLayout(10, 10), PANEL_CORNER_RADIUS, MAIN_BACKGROUND_COLOR);
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        dialog.setContentPane(dialogPanel); // Set the content pane to our rounded panel
        dialog.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                dialog.setShape(new RoundRectangle2D.Double(0, 0, dialog.getWidth(), dialog.getHeight(), PANEL_CORNER_RADIUS, PANEL_CORNER_RADIUS));
            }
        });
        JLabel errorTitleLabel = createHeaderLabel(title);
        errorTitleLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Dialog title font
        dialogPanel.add(errorTitleLabel, BorderLayout.NORTH);
        JLabel messageLabel = new JLabel("<html><div style='text-align: center;'>" + message.replace("\n", "<br>") + "</div></html>", SwingConstants.CENTER);
        messageLabel.setForeground(FOREGROUND_COLOR);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        dialogPanel.add(messageLabel, BorderLayout.CENTER);
        JButton okButton = new RoundedButton("Ок", true); 
        okButton.setFont(new Font("Arial", Font.BOLD, 13));
        okButton.addActionListener(e -> dialog.dispose());
        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPane.setOpaque(false); // So RoundedPanel background shows
        buttonPane.add(okButton);
        dialogPanel.add(buttonPane, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception e) {
                // If Nimbus is not available, use the default L&F
                 try {
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            AuthFrame frame = new AuthFrame();
            frame.setVisible(true);
        });
    }
} 