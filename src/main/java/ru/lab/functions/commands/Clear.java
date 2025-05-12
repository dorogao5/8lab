package ru.lab.functions.commands;

import ru.lab.functions.Command;
import ru.lab.util.CollectionManager;

/**
 * Команда для очистки коллекции.
 */
public class Clear implements Command {
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды Clear.
     *
     * @param collectionManager менеджер коллекции транспортных средств.
     */
    public Clear(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду Clear, очищая коллекцию.
     *
     * @param args аргументы команды (не используются).
     */
    @Override
    public void execute(String[] args) {
        collectionManager.clear();
        System.out.println("Коллекция очищена.");
    }

    /**
     * Возвращает краткое описание команды Clear.
     *
     * @return описание команды.
     */
    @Override
    public String getDescription() {
        return "clear - очистить коллекцию";
    }
}
