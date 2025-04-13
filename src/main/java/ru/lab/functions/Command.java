package ru.lab.functions;

/**
 * Базовый интерфейс для всех команд.
 */
public interface Command {
    /**
     * Выполняет основное действие команды.
     *
     * @param args аргументы команды.
     *
     */
    void execute(String[] args) throws Exception;

    /**
     * Возвращает краткое описание команды (для справки).
     *
     * @return описание команды.
     */
    String getDescription();
}
