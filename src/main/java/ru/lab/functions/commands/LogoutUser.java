package ru.lab.functions.commands;

import ru.lab.builder.Console;
import ru.lab.functions.Command;
import ru.lab.util.DBUserManager;

/**
 * Команда для входа пользователя с заданным именем и паролем.
 * Формат: login_user - затем последовательно считываются поля пользователя (имя, пароль).
 */
public class LogoutUser implements Command {
    private final Console console; // Используем интерактивный ввод

    /**
     * Конструктор команды RegisterUser.
     *
     * @param console           объект Console, предоставляющий метод readInteractiveLine.
     */
    public LogoutUser(Console console) {
        this.console = console;
    }

    @Override
    public void execute(String[] args) {
        if(DBUserManager.getInstance().getCurrentUser() == null) {
            System.out.println("В системе нет сейчас активных пользователей!");
            return;
        }

        String username = DBUserManager.getInstance().getCurrentUser().getUsername();

        DBUserManager.getInstance().logoutUser();
        System.out.println("Пользователь с именем: " + username + " успешно покинул систему");
    }

    private String promptString(String prompt, boolean nonEmpty) {
        String input;
        while (true) {
            input = console.readInteractiveLine(prompt);
            if (nonEmpty && (input == null || input.trim().isEmpty())) {
                System.out.println("Ошибка: строка не может быть пустой.");
            } else {
                break;
            }
        }
        return input;
    }

    @Override
    public String getDescription() {
        return "logout_user - текущий пользователь выходит из системы. \n(используйте \\stop_running_command если хотите прервать выполнение команды)";
    }
}
