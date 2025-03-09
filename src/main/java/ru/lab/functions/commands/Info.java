package ru.lab.functions.commands;

import ru.lab.functions.Command;
import ru.lab.util.CollectionManager;
import ru.lab.model.Vehicle;

import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Date;

/**
 * Команда для вывода информации о коллекции.
 */
public class Info implements Command {
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды Info.
     *
     * @param collectionManager менеджер коллекции транспортных средств.
     */
    public Info(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду Info, выводя информацию о коллекции.
     *
     * @param args аргументы команды (не используются).
     */
    @Override
    public void execute(String[] args) {
        Hashtable<Integer, Vehicle> collection = collectionManager.getCollection();
        Date initDate = collectionManager.getInitializationDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("Информация о коллекции:");
        System.out.println("Тип коллекции: " + collection.getClass().getName());
        System.out.println("Дата инициализации: " + sdf.format(initDate));
        System.out.println("Количество элементов: " + collection.size());
    }

    /**
     * Возвращает краткое описание команды Info.
     *
     * @return описание команды.
     */
    @Override
    public String getDescription() {
        return "info - вывести информацию о коллекции (тип, дата инициализации, количество элементов)";
    }
}
