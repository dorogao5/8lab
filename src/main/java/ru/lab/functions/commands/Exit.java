package ru.lab.functions.commands;

import ru.lab.functions.Command;

/**
 * Команда для завершения работы приложения без сохранения коллекции.
 */
public class Exit implements Command {

    /**
     * Выполняет команду Exit, завершая работу приложения.
     *
     * @param args аргументы команды (не используются).
     */
    @Override
    public void execute(String[] args) {
        System.out.println("Завершение работы приложения.");
        System.exit(0);
    }

    /**
     * Возвращает краткое описание команды Exit.
     *
     * @return описание команды.
     */
    @Override
    public String getDescription() {
        return "exit - завершить программу (без сохранения в файл)";
    }
}
