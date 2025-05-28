package gui;

import javax.swing.*;
import java.awt.*;


public class InitialPanel extends JPanel {
    private AuthFrame parentFrame;

    public InitialPanel(AuthFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout(10, 15)); // Adjusted vertical gap
        setOpaque(false); // Прозрачная панель для скругленных углов
        setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20)); // Tighter padding

        // Custom close button (X)
        JButton closeButton = new CustomCloseButton();
        closeButton.addActionListener(e -> System.exit(0));
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)); // Align to LEFT
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0,0,10,0)); // Space below close button
        topPanel.add(closeButton);
        
        // Header
        JLabel welcomeLabel = AuthFrame.createHeaderLabel("Здравствуйте");

        // Buttons Panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setOpaque(false);
        buttonsPanel.setLayout(new GridLayout(2, 1, 0, 10)); // Reduced gap for buttons

        JButton loginButton = new RoundedButton("Войти", true);
        loginButton.addActionListener(e -> parentFrame.showPanel(AuthFrame.LOGIN_PANEL));

        JButton registerButton = new RoundedButton("Зарегистрироваться", false);
        registerButton.addActionListener(e -> parentFrame.showPanel(AuthFrame.REGISTER_PANEL));

        buttonsPanel.add(loginButton);
        buttonsPanel.add(registerButton);
        
        // Center content panel
        JPanel centerContent = new JPanel(new GridBagLayout());
        centerContent.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 10, 0); // Reduced space below welcome label

        centerContent.add(welcomeLabel, gbc);
        gbc.insets = new Insets(10, 0, 0, 0); // Reduced space above buttons panel
        centerContent.add(buttonsPanel, gbc);
        
        add(topPanel, BorderLayout.NORTH);
        add(centerContent, BorderLayout.CENTER);
    }
    // Removed styleCloseButton as CustomCloseButton is used
} 