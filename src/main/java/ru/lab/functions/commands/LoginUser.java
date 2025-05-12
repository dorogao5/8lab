package ru.lab.functions.commands;

import ru.lab.builder.Console;
import ru.lab.functions.AbstractCommand;
import ru.lab.util.DBUserManager;

/**
 * Команда для входа пользователя с заданным именем и паролем.
 * Формат: login_user - затем последовательно считываются поля пользователя (имя, пароль).
 */
public class LoginUser extends AbstractCommand {
    /**
     * Конструктор команды RegisterUser.
     *
     * @param console           объект Console, предоставляющий метод readInteractiveLine.
     */
    public LoginUser(Console console) {
        super(console);
    }

    @Override
    public void execute(String[] args) {
        if(DBUserManager.getInstance().getCurrentUser() != null) {
            System.out.println("Пользователь с именем: " + DBUserManager.getInstance().getCurrentUser().getUsername() + " должен сначала выйти из системы!");
            return;
        }

        // Считываем данные для нового элемента через интерактивный ввод (игнорируя строки из скрипта)
        String name = promptString("Введите имя пользователя: ", true);
        String password = promptString("Введите пароль пользователя: ", true);

        boolean result = DBUserManager.getInstance().loginUser(name, password);
            if (result) {
                System.out.println("Пользователь с именем: " + name + " успешно вошёл в систему");
            } else {
                    System.out.println("Пользователь с именем: " + name + " не смог войти в систему");
            }
    }

    @Override
    public String getDescription() {
        return "login_user - войти в систему указанным пользователем. \n(используйте \\stop_running_command если хотите прервать выполнение команды)";
    }
}
