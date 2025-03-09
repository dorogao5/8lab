package ru.lab.builder;

import ru.lab.functions.Command;
import ru.lab.functions.Invoker;

import java.util.Map;
import java.util.Scanner;

/**
 * Класс для обработки ввода команд пользователем или из скрипта.
 */
public class Console {
    private final Scanner userScanner;       // для чтения из System.in
    private final Invoker invoker;
    private final Map<String, Command> commands;
    private final ScriptManager scriptManager;

    /**
     * Конструктор, принимающий основной Scanner (консоль),
     * Invoker (реестр команд) и ScriptManager (очередь строк для скриптов).
     *
     * @param userScanner   источник ввода.
     * @param invoker       объект Invoker, содержащий зарегистрированные команды.
     * @param scriptManager менеджер строк скрипта.
     */
    public Console(Scanner userScanner, Invoker invoker, ScriptManager scriptManager) {
        this.userScanner = userScanner;
        this.invoker = invoker;
        this.commands = invoker.getCommands();
        this.scriptManager = scriptManager;
    }

    /**
     * Запускает главный цикл интерактивного режима.
     */
    public void start() {
        System.out.println("Добро пожаловать в приложение! Введите команду:");
        while (true) {
            String line = readLine();
            if (line == null) {
                System.out.println("Ввод завершён.");
                break;
            }
            line = line.trim();
            if (line.isEmpty()) continue;
            String[] tokens = line.split("\\s+");
            String commandName = tokens[0];
            invoker.recordCommand(commandName);
            Command command = commands.get(commandName);
            if (command == null) {
                System.out.println("Неизвестная команда: " + commandName);
                continue;
            }
            String[] args = new String[tokens.length - 1];
            System.arraycopy(tokens, 1, args, 0, args.length);
            try {
                command.execute(args);
            } catch (Exception e) {
                System.out.println("Ошибка при выполнении команды '" + line + "': " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Возвращает следующую строку ввода: если в ScriptManager есть строки – возвращает их,
     * иначе читает из консоли.
     *
     * @return следующая строка ввода или null, если ввод закончился.
     */
    public String readLine() {
        if (!scriptManager.isEmpty()) {
            return scriptManager.pollLine();
        }
        if (userScanner.hasNextLine()) {
            return userScanner.nextLine();
        }
        return null;
    }

    /**
     * То же, что и readLine(), но сначала выводит подсказку.
     *
     * @param prompt сообщение-подсказка.
     * @return введённая строка.
     */
    public String readLine(String prompt) {
        System.out.print(prompt);
        return readLine();
    }
}
