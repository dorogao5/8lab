package ru.lab.functions.commands;

import ru.lab.functions.Command;
import ru.lab.util.CollectionManager;
import ru.lab.model.Vehicle;

import java.util.Hashtable;

/**
 * Команда для вывода всех элементов коллекции в строковом представлении.
 */
public class Show implements Command {
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды Show.
     *
     * @param collectionManager менеджер коллекции транспортных средств.
     */
    public Show(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду Show, выводя каждый элемент коллекции.
     *
     * @param args аргументы команды (не используются).
     */
    @Override
    public void execute(String[] args) {
        Hashtable<Integer, Vehicle> collection = collectionManager.getCollection();
        if (collection.isEmpty()) {
            System.out.println("Коллекция пуста.");
            return;
        }
        System.out.println("Элементы коллекции:");
        for (Vehicle vehicle : collection.values()) {
            System.out.println(vehicle.toString());
        }
    }

    /**
     * Возвращает краткое описание команды Show.
     *
     * @return описание команды.
     */
    @Override
    public String getDescription() {
        return "show - вывести в стандартный поток вывода все элементы коллекции в строковом представлении";
    }
}
