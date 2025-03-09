package ru.lab.functions.commands;

import ru.lab.functions.Command;
import ru.lab.util.CollectionManager;
import ru.lab.model.Vehicle;
import ru.lab.model.VehicleType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

/**
 * Команда remove_all_by_type type - удалить из коллекции все элементы, значение поля type которых эквивалентно заданному,
 * с перенумерацией оставшихся элементов.
 */
public class RemoveAllByType implements Command {
    private final CollectionManager collectionManager;
    private final Scanner scanner;

    /**
     * Конструктор команды RemoveAllByType.
     *
     * @param collectionManager менеджер коллекции транспортных средств.
     * @param scanner источник ввода для запроса типа.
     */
    public RemoveAllByType(CollectionManager collectionManager, Scanner scanner) {
        this.collectionManager = collectionManager;
        this.scanner = scanner;
    }

    /**
     * Выполняет команду, удаляя все элементы с типом, эквивалентным заданному.
     * Формат: remove_all_by_type &lt;тип&gt;
     *
     * @param args аргументы команды.
     */
    @Override
    public void execute(String[] args) {
        String inputType;
        if (args.length < 1) {
            System.out.print("Введите тип транспортного средства для удаления (BOAT, CHOPPER, HOVERBOARD, SPACESHIP): ");
            inputType = scanner.nextLine().trim();
        } else {
            inputType = args[0];
        }

        VehicleType targetType;
        try {
            targetType = VehicleType.valueOf(inputType.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: некорректное значение типа. Доступные типы: ");
            for (VehicleType vt : VehicleType.values()) {
                System.out.print(vt.name() + " ");
            }
            System.out.println();
            return;
        }

        Hashtable<Integer, Vehicle> collection = collectionManager.getCollection();
        List<Integer> keysToRemove = new ArrayList<>();
        for (Integer key : collection.keySet()) {
            Vehicle v = collection.get(key);
            if (targetType.equals(v.getType())) {
                keysToRemove.add(key);
            }
        }

        for (Integer key : keysToRemove) {
            collection.remove(key);
        }

        // Перенумеровываем оставшиеся элементы
        List<Integer> remainingKeys = new ArrayList<>(collection.keySet());
        Collections.sort(remainingKeys);
        Hashtable<Integer, Vehicle> newCollection = new Hashtable<>();
        int newKey = 1;
        for (Integer oldKey : remainingKeys) {
            Vehicle v = collection.get(oldKey);
            v.setId(newKey);
            newCollection.put(newKey, v);
            newKey++;
        }
        collection.clear();
        collection.putAll(newCollection);

        System.out.println("Удалено " + keysToRemove.size() + " элемент(ов) с типом " + targetType.name() + ".");
    }

    /**
     * Возвращает краткое описание команды RemoveAllByType.
     *
     * @return описание команды.
     */
    @Override
    public String getDescription() {
        return "remove_all_by_type type - удалить из коллекции все элементы, значение поля type которых эквивалентно заданному";
    }
}
