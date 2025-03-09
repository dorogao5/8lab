package ru.lab.functions.commands;

import ru.lab.functions.Command;
import ru.lab.util.CollectionManager;
import ru.lab.model.Vehicle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

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
     * Выполняет команду Show, выводя каждый элемент коллекции в порядке возрастания id.
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
        List<Integer> sortedKeys = new ArrayList<>(collection.keySet());
        Collections.sort(sortedKeys);
        for (Integer key : sortedKeys) {
            System.out.println(collection.get(key));
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
