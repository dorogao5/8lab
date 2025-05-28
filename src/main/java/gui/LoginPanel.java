package gui;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private AuthFrame parentFrame;
    private JTextField loginField;
    private JPasswordField passwordField;

    public LoginPanel(AuthFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout(10, 5)); 
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 20, 15, 20)); // Tighter padding

        JButton closeButton = new CustomCloseButton();
        closeButton.addActionListener(e -> parentFrame.showPanel(AuthFrame.INITIAL_PANEL)); 
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0,0)); // Align to LEFT
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0,0,10,0)); // Space below close button
        topPanel.add(closeButton);

        JLabel titleLabel = AuthFrame.createHeaderLabel("С возвращением");

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel loginLabel = new JLabel("Логин");
        loginLabel.setForeground(Color.LIGHT_GRAY);
        loginLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST; gbc.insets = new Insets(2,0,1,0); // Compact
        formPanel.add(loginLabel, gbc);
        gbc.gridy++; gbc.insets = new Insets(1,0,5,0); // Compact space after field
        loginField = AuthFrame.createStyledTextField();
        formPanel.add(loginField, gbc);

        JLabel passwordLabel = new JLabel("Пароль");
        passwordLabel.setForeground(Color.LIGHT_GRAY);
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridy++; gbc.insets = new Insets(2,0,1,0); // Compact
        formPanel.add(passwordLabel, gbc);
        gbc.gridy++; gbc.insets = new Insets(1,0,5,0); // Compact space after field
        passwordField = AuthFrame.createStyledPasswordField();
        formPanel.add(passwordField, gbc);
        
        JLabel noAccountLabel = new JLabel("<html><a href=\"\" style=\"color: #AAAAAA; text-decoration: none;\">Нет аккаунта?</a></html>");
        noAccountLabel.setForeground(Color.LIGHT_GRAY);
        noAccountLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        noAccountLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        noAccountLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                parentFrame.showPanel(AuthFrame.REGISTER_PANEL);
            }
        });
        gbc.gridy++; gbc.anchor = GridBagConstraints.EAST; gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0; gbc.insets = new Insets(2, 0, 8, 0); // Compact insets
        formPanel.add(noAccountLabel, gbc);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.gridwidth = GridBagConstraints.REMAINDER;
        mainGbc.fill = GridBagConstraints.HORIZONTAL;
        mainGbc.weightx = 1.0;

        mainGbc.insets = new Insets(0,0,8,0); // Space after title
        contentPanel.add(titleLabel, mainGbc);

        mainGbc.gridy = 1;
        mainGbc.insets = new Insets(0,0,8,0); // Space after form
        contentPanel.add(formPanel, mainGbc);
        
        JButton continueButton = new RoundedButton("Продолжить", true);
        continueButton.addActionListener(e -> {
            String login = loginField.getText();
            String password = new String(passwordField.getPassword());
            if (login.trim().isEmpty() || password.isEmpty()) {
                parentFrame.showErrorDialog("Ошибка", "Все поля должны быть заполнены.");
                return;
            }
            boolean success = ru.lab.util.DBUserManager.getInstance().loginUser(login, password);
            if (success) {
                // Открыть главное окно
                new MainAppFrame();
                SwingUtilities.getWindowAncestor(this).dispose();
            } else {
                parentFrame.showErrorDialog("Ошибка", "Пользователя с таким логином/паролем не существует");
            }
        });
        mainGbc.gridy = 2;
        mainGbc.fill = GridBagConstraints.HORIZONTAL;
        mainGbc.anchor = GridBagConstraints.CENTER;
        mainGbc.insets = new Insets(5,0,0,0); 
        contentPanel.add(continueButton, mainGbc);

        add(topPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }
    // Removed styleCloseButton and showErrorDialog, now using AuthFrame methods or custom components
} 