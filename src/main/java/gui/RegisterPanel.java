package gui;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class RegisterPanel extends JPanel {
    private AuthFrame parentFrame;
    private JTextField loginField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    public RegisterPanel(AuthFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout(10, 5));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 20, 15, 20));

        JButton closeButton = new CustomCloseButton();
        closeButton.addActionListener(e -> parentFrame.showPanel(AuthFrame.INITIAL_PANEL));
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0,0));
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
        topPanel.add(closeButton);

        JLabel titleLabel = AuthFrame.createHeaderLabel("Приятно познакомиться");

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel loginLabel = new JLabel("Логин");
        loginLabel.setForeground(Color.LIGHT_GRAY);
        loginLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST; gbc.insets = new Insets(2,0,1,0);
        formPanel.add(loginLabel, gbc);
        gbc.gridy++; gbc.insets = new Insets(1,0,5,0);
        loginField = AuthFrame.createStyledTextField();
        formPanel.add(loginField, gbc);

        JLabel passwordLabel = new JLabel("Пароль");
        passwordLabel.setForeground(Color.LIGHT_GRAY);
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridy++; gbc.insets = new Insets(2,0,1,0);
        formPanel.add(passwordLabel, gbc);
        gbc.gridy++; gbc.insets = new Insets(1,0,5,0);
        passwordField = AuthFrame.createStyledPasswordField();
        formPanel.add(passwordField, gbc);

        JLabel confirmPasswordLabel = new JLabel("Подтвердите пароль");
        confirmPasswordLabel.setForeground(Color.LIGHT_GRAY);
        confirmPasswordLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridy++; gbc.insets = new Insets(2,0,1,0);
        formPanel.add(confirmPasswordLabel, gbc);
        gbc.gridy++; gbc.insets = new Insets(1,0,5,0);
        confirmPasswordField = AuthFrame.createStyledPasswordField();
        formPanel.add(confirmPasswordField, gbc);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.gridwidth = GridBagConstraints.REMAINDER;
        mainGbc.fill = GridBagConstraints.HORIZONTAL;
        mainGbc.weightx = 1.0;

        mainGbc.insets = new Insets(0,0,8,0);
        contentPanel.add(titleLabel, mainGbc);

        mainGbc.gridy = 1;
        mainGbc.insets = new Insets(0,0,8,0);
        contentPanel.add(formPanel, mainGbc);
        
        JButton continueButton = new RoundedButton("Продолжить", true);
        continueButton.addActionListener(e -> {
            String login = loginField.getText();
            char[] pass = passwordField.getPassword();
            char[] confirmPass = confirmPasswordField.getPassword();

            if (login.trim().isEmpty() || pass.length == 0 || confirmPass.length == 0) {
                parentFrame.showErrorDialog("Ошибка", "Все поля должны быть заполнены.");
                return;
            }
            if (!java.util.Arrays.equals(pass, confirmPass)) {
                parentFrame.showErrorDialog("Ошибка", "Пароли не совпадают<br>Повторите ввод");
                return;
            }
            if (ru.lab.util.DBUserManager.getInstance().checkUsername(login)) {
                parentFrame.showErrorDialog("Ошибка", "Пользователь с таким логином уже существует");
                return;
            }
            ru.lab.util.DBUserManager.getInstance().saveUser(login, new String(pass));
            parentFrame.showErrorDialog("Успех", "Регистрация прошла успешно! Теперь войдите в систему.");
            parentFrame.showPanel(AuthFrame.LOGIN_PANEL);
        });
        mainGbc.gridy = 2;
        mainGbc.fill = GridBagConstraints.HORIZONTAL;
        mainGbc.anchor = GridBagConstraints.CENTER;
        mainGbc.insets = new Insets(5,0,0,0);
        contentPanel.add(continueButton, mainGbc);

        add(topPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }
} 