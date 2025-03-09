package ru.lab.builder;

import ru.lab.functions.Command;
import ru.lab.functions.Invoker;

import java.util.Map;
import java.util.Scanner;

/**
 * Класс для обработки ввода команд пользователем.
 */
public class Console {
    private final Scanner scanner;
    private final Invoker invoker;
    private final Map<String, Command> commands;

    /**
     * Конструктор, принимающий источник ввода и объект Invoker.
     *
     * @param scanner источник ввода.
     * @param invoker объект Invoker, содержащий зарегистрированные команды.
     */
    public Console(Scanner scanner, Invoker invoker) {
        this.scanner = scanner;
        this.invoker = invoker;
        this.commands = invoker.getCommands();
    }

    /**
     * Запускает интерактивный режим работы приложения.
     */
    public void start() {
        System.out.println("Добро пожаловать в приложение! Введите команду:");
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;
            String[] tokens = input.split("\\s+");
            String commandName = tokens[0];
            // Регистрируем команду в истории
            invoker.recordCommand(commandName);
            Command command = commands.get(commandName);
            if (command != null) {
                String[] args = new String[tokens.length - 1];
                System.arraycopy(tokens, 1, args, 0, args.length);
                command.execute(args);
            } else {
                System.out.println("Неизвестная команда: " + commandName);
            }
        }
    }
}
