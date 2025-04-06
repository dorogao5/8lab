package ru.lab.builder;

import ru.lab.functions.Command;
import ru.lab.functions.Invoker;
import ru.lab.util.CommandInterruptedException;

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
     * Конструктор класса Console.
     *
     * @param userScanner  сканер для чтения пользовательского ввода.
     * @param invoker      объект Invoker для обработки команд.
     * @param scriptManager менеджер для поддержки выполнения скриптов.
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
            String line;
            try {
                line = readLine();
            } catch (CommandInterruptedException e) {
                System.out.println("Прерывание ввода. Вернитесь в меню.");
                continue;
            }
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
            } catch (CommandInterruptedException e) {
                System.out.println("Команда прервана: " + e.getMessage());
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
     * Читает строку с подсказкой, но всегда из интерактивного ввода пользователя,
     * игнорируя строки из скрипта.
     * Если пользователь вводит "\stop_running_command", выбрасывается исключение.
     *
     * @param prompt сообщение-подсказка.
     * @return введённая строка.
     */
    public String readInteractiveLine(String prompt) {
        System.out.print(prompt);
        String line = null;
        if (userScanner.hasNextLine()) {
            line = userScanner.nextLine();
        }
        if (line != null && line.trim().equalsIgnoreCase("\\stop_running_command")) {
            throw new CommandInterruptedException("Пользователь прервал выполнение команды.");
        }
        return line;
    }

    /**
     * Читает строку с подсказкой. Сначала проверяет очередь скриптов,
     * если она не пуста – возвращает строку из скрипта, иначе читает из консоли.
     *
     * @param prompt сообщение-подсказка.
     * @return введённая строка.
     */
    public String readLine(String prompt) {
        System.out.print(prompt);
        String line = readLine();
        if (line != null && line.trim().equalsIgnoreCase("\\stop_running_command")) {
            throw new CommandInterruptedException("Пользователь прервал выполнение команды.");
        }
        return line;
    }
}
