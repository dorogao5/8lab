package ru.lab.functions.commands;

import ru.lab.functions.Command;
import java.util.Map;

/**
 * Команда для вывода справки по доступным командам.
 */
public class Help implements Command {
    private final Map<String, Command> commands;

    /**
     * Конструктор команды Help.
     *
     * @param commands мапа зарегистрированных команд.
     */
    public Help(Map<String, Command> commands) {
        this.commands = commands;
    }

    /**
     * Выполняет команду Help, выводя описание всех доступных команд.
     *
     * @param args аргументы команды (не используются).
     */
    @Override
    public void execute(String[] args) {
        System.out.println("Доступные команды:");
        for (Map.Entry<String, Command> entry : commands.entrySet()) {
            System.out.printf("%s : %s%n", entry.getKey(), entry.getValue().getDescription());
        }
    }

    /**
     * Возвращает краткое описание команды Help.
     *
     * @return описание команды.
     */
    @Override
    public String getDescription() {
        return "help - вывести справку по доступным командам";
    }
}
