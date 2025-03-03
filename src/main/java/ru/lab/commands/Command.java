package ru.lab.commands;

/**
 * Базовый интерфейс для всех команд.
 */
public interface Command {
    /**
     * Выполняет основное действие команды.
     */
    void execute(String[] args);

    /**
     * @return краткое описание команды (для help).
     */
    String getDescription();
}

