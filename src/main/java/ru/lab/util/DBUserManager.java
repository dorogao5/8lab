package ru.lab.util;

import ru.lab.Main;
import ru.lab.model.User;

import java.sql.*;

public class DBUserManager {
    private final String CHECK_USERNAME = "select * from users where username = ?";
    private final String LOGIN_USERNAME = "select username, password from users where username = ? and password = ?";
    private final String SAVE_USER = "insert into users (username, password) values (?, ?)";

    private static DBUserManager instance;

    private User currentUser = null;

    private Connection conn = null;


    public static DBUserManager getInstance() {
        if (instance == null) {
            instance = new DBUserManager();
        }
        return instance;
    }

    private DBUserManager() {
        try {
            // System.out.println("11.0 loading postgresql driver");
            this.conn = DriverManager.getConnection(Main.getConnectionString());
            // System.out.println("11.1 postgresql driver successfully loaded");
        } catch (SQLException e) {
            // System.out.println("11.2 failed to load postgresql driver");
            throw new RuntimeException(e);
        }

    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean checkUsername(String username) {
        try {
            PreparedStatement ps1 = conn.prepareStatement(CHECK_USERNAME);
            ps1.setString(1, username);
            ResultSet rs1 = ps1.executeQuery();
            return rs1.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String username, String password) {
        try {
            PreparedStatement ps1 = conn.prepareStatement(SAVE_USER);
            ps1.setString(1, username);
            ps1.setString(2, User.encryptPassword(password));
            ps1.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean loginUser(String username, String password) {
        try {
            PreparedStatement ps1 = conn.prepareStatement(LOGIN_USERNAME);
            ps1.setString(1, username);
            ps1.setString(2, User.encryptPassword(password));
            ResultSet rs1 = ps1.executeQuery();
            while (rs1.next()) {
                currentUser = new User(rs1.getString(1), rs1.getString(2));
            }
            return (currentUser != null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void logoutUser() {
        currentUser = null;
    }
}
