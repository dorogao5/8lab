package ru.lab.functions;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Класс для регистрации и хранения команд, а также ведения истории выполненных команд.
 */
public class Invoker {
    private final Map<String, Command> commands;
    private final LinkedList<String> history;

    /**
     * Конструктор, инициализирующий пустую мапу команд и историю.
     */
    public Invoker() {
        commands = new HashMap<>();
        history = new LinkedList<>();
    }

    /**
     * Регистрирует команду с заданным именем.
     *
     * @param name имя команды.
     * @param command объект команды, реализующий интерфейс Command.
     */
    public void register(String name, Command command) {
        commands.put(name, command);
    }

    /**
     * Возвращает зарегистрированные команды.
     *
     * @return мапа команд.
     */
    public Map<String, Command> getCommands() {
        return commands;
    }

    /**
     * Регистрирует имя выполненной команды в истории.
     *
     * @param commandName имя команды.
     */
    public void recordCommand(String commandName) {
        if (history.size() >= 8) {
            history.removeFirst();
        }
        history.add(commandName);
    }

    /**
     * Возвращает копию истории выполненных команд.
     *
     * @return история команд.
     */
    public LinkedList<String> getHistory() {
        return new LinkedList<>(history);
    }
}
