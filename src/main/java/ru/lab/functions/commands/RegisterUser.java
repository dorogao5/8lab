package ru.lab.functions.commands;

import ru.lab.builder.Console;
import ru.lab.functions.AbstractCommand;
import ru.lab.util.DBUserManager;

/**
 * Команда для добавления нового пользователя с заданным именем и паролем.
 * Формат: register_user - затем последовательно считываются поля нового пользователя (имя, пароль, пароль ещё раз).
 */
public class RegisterUser extends AbstractCommand {

    /**
     * Конструктор команды RegisterUser.
     *
     * @param console           объект Console, предоставляющий метод readInteractiveLine.
     */
    public RegisterUser(Console console) {
        super(console);
    }

    @Override
    public void execute(String[] args) {
        // Считываем данные для нового элемента через интерактивный ввод (игнорируя строки из скрипта)
        String name = null;
        do {
            name = promptString("Введите yникальное имя нового пользователя: ", true);
        } while (DBUserManager.getInstance().checkUsername(name));

        String password = promptString("Введите пароль нового пользователя: ", true);
        String password1 = null;
        do {
            password1 = promptString("Повторите пароль нового пользователя: ", true);
        } while (!password.equals(password1));

        DBUserManager.getInstance().saveUser(name, password);
        System.out.println("Пользователь успешно добавлен с именем: " + name + ".");
    }

    @Override
    public String getDescription() {
        return "register_user - добавить новый элемент с заданным ключом. \n(используйте \\stop_running_command если хотите прервать выполнение команды)";
    }
}
