package ru.lab.functions.commands;

import ru.lab.functions.Command;
import ru.lab.util.CollectionManager;
import ru.lab.model.Vehicle;
import ru.lab.model.FuelType;

import java.util.Hashtable;
import java.util.Map;
import java.util.TreeMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Команда print_field_ascending_fuel_type - выводит значения поля fuelType всех элементов
 * коллекции в порядке возрастания по частоте появления, с отображением количества для каждого типа.
 */
public class PrintFieldAscendingFuelType implements Command {
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды.
     *
     * @param collectionManager менеджер коллекции транспортных средств.
     */
    public PrintFieldAscendingFuelType(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду, группируя элементы коллекции по fuelType, подсчитывая их частоту,
     * сортируя группы по возрастанию количества и выводя результат.
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

        // Подсчет частоты появления каждого fuelType.
        // Если значение fuelType равно null, используем строку "null" для группировки.
        Map<String, Integer> frequencyMap = new TreeMap<>();
        for (Vehicle vehicle : collection.values()) {
            FuelType ft = vehicle.getFuelType();
            String key = (ft == null) ? "null" : ft.name();
            frequencyMap.put(key, frequencyMap.getOrDefault(key, 0) + 1);
        }

        // Сортируем группы по возрастанию частоты появления.
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(frequencyMap.entrySet());
        sortedEntries.sort(Comparator.comparingInt(Map.Entry::getValue));

        // Вывод результата.
        System.out.println("Значения поля fuelType по возрастанию частоты появления:");
        for (Map.Entry<String, Integer> entry : sortedEntries) {
            System.out.printf("%s : %d%n", entry.getKey(), entry.getValue());
        }
    }

    /**
     * Возвращает краткое описание команды.
     *
     * @return описание команды.
     */
    @Override
    public String getDescription() {
        return "print_field_ascending_fuel_type - вывести значения fuelType всех элементов в порядке возрастания по частоте появления с отображением количества для каждого типа";
    }
}
