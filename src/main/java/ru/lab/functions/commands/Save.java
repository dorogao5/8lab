package ru.lab.functions.commands;

import ru.lab.functions.Command;
import ru.lab.util.CollectionManager;
import ru.lab.util.DBUserManager;

/**
 * Команда для сохранения коллекции в файл.
 */
public class Save implements Command {
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды Save.
     *
     * @param collectionManager менеджер коллекции транспортных средств.
     */
    public Save(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду Save, сохраняя коллекцию в файл.
     *
     * @param args аргументы команды (не используются).
     */
    @Override
    public void execute(String[] args) {
        if(DBUserManager.getInstance().getCurrentUser() == null) {
            System.out.println("Нужно авторизоваться для выполнения этой операции");
            return;
        }

        collectionManager.save();
        System.out.println("Коллекция успешно сохранена в БД");
    }

    /**
     * Возвращает краткое описание команды Save.
     *
     * @return описание команды.
     */
    @Override
    public String getDescription() {
        return "save - сохранить коллекцию в БД";
    }
}
