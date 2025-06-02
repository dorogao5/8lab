package ru.lab;

import gui.AuthFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Главный класс для запуска приложения.
 */
public class Main {
    private static String connectionString = "jdbc:postgresql://188.213.0.226:5432/pgdb?user=doroga&password=OlzhasAlia2011";
    private static String username = "user=doroga";
    private static String password = "password=OlzhasAlia2011";
    private static String db = "pgdb";
    private static int port = 5432;
    private static String host = "188.213.0.226";

    /**
     * Точка входа в приложение.
     *
     * @param args аргументы командной строки, которыми можно переопределить строку подключения.
     * user=XXX - имя пользователя БД
     * password=YYY - пароль пользователя БД
     * db=ZZZ - название БД
     * host=ABC - IP адрес или имя хоста на котором расположена база
     * port=5432 - порт БД
     * можно указывать в любом порядке и комбинации
     * для пропущенных параметров будут использоваться значения по умолчанию
     */
    public static void main(String[] args) {
        try {
            for(String arg : args) {
                String parameterName = arg.substring(0, arg.indexOf("="));
                if(parameterName.equals("host")) {
                    host = arg.substring(arg.indexOf("=") + 1);
                }
                if(parameterName.equals("port")) {
                    port = Integer.parseInt(arg.substring(arg.indexOf("=") + 1));
                }
                if(parameterName.equals("db")) {
                    db = arg.substring(arg.indexOf("=") + 1);;
                }
                if(parameterName.equals("user")) {
                   username = arg;
                }
                if(parameterName.equals("password")) {
                    password = arg;
                }
            }

            connectionString = "jdbc:postgresql://" + host + ":" + port + "/" + db + "?" + username + "&" + password;

            // Launch the GUI
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
                        System.err.println("Error setting LookAndFeel: " + ex.getMessage());
                    }
                }
                AuthFrame authFrame = new AuthFrame();
                authFrame.setVisible(true);
            });
        } catch (Exception e) {
            System.err.println("Ошибка запуска приложения: " + e.getMessage());
        }
    }

    public static String getConnectionString() {
        return connectionString;
    }
}
