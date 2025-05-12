package ru.lab.functions.commands;

import ru.lab.functions.Command;
import ru.lab.util.CollectionManager;
import ru.lab.util.DBUserManager;

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
        if(DBUserManager.getInstance().getCurrentUser() == null) {
            System.out.println("Нужно авторизоваться для выполнения этой операции");
            return;
        }
        String username = DBUserManager.getInstance().getCurrentUser().getUsername();
        collectionManager.clearByUser(username);
        System.out.println("Ваши записи в коллекции очищены.");
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
