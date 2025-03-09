package ru.lab.functions.commands;

import ru.lab.functions.Command;
import ru.lab.util.CollectionManager;
import ru.lab.model.Vehicle;

import java.util.Hashtable;
import java.util.Map;
import java.util.TreeMap;

/**
 * Команда group_counting_by_engine_power - сгруппировать элементы коллекции по значению поля enginePower,
 * вывести количество элементов в каждой группе.
 */
public class GroupCountingByEnginePower implements Command {
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды GroupCountingByEnginePower.
     *
     * @param collectionManager менеджер коллекции транспортных средств.
     */
    public GroupCountingByEnginePower(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду, группируя элементы коллекции по значению enginePower и выводя количество элементов в каждой группе.
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

        // Группировка с использованием TreeMap для сортировки по возрастанию значения enginePower
        Map<Float, Integer> groups = new TreeMap<>();

        for (Vehicle vehicle : collection.values()) {
            float enginePower = vehicle.getEnginePower();
            groups.put(enginePower, groups.getOrDefault(enginePower, 0) + 1);
        }

        System.out.println("Группировка по enginePower:");
        for (Map.Entry<Float, Integer> entry : groups.entrySet()) {
            System.out.printf("enginePower = %.2f : %d элемент(ов)%n", entry.getKey(), entry.getValue());
        }
    }

    /**
     * Возвращает краткое описание команды GroupCountingByEnginePower.
     *
     * @return описание команды.
     */
    @Override
    public String getDescription() {
        return "group_counting_by_engine_power - сгруппировать элементы коллекции по значению поля enginePower, вывести количество элементов в каждой группе";
    }
}
