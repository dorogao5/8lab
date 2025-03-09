package ru.lab.functions.commands;

import ru.lab.functions.Command;
import ru.lab.util.CollectionManager;
import ru.lab.model.Vehicle;
import ru.lab.model.Coordinates;
import ru.lab.model.VehicleType;
import ru.lab.model.FuelType;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

/**
 * Команда remove_greater {element} - удалить из коллекции все элементы,
 * значение поля enginePower которых больше, чем у введённого элемента.
 * &lt;p&gt;
 * Формат: имя_команды ключ отсутствует, далее осуществляется запрос на ввод данных
 * для нового элемента.
 * При некорректном вводе повторяются запросы.
 */
public class RemoveGreater implements Command {
    private final CollectionManager collectionManager;
    private final Scanner scanner;

    /**
     * Конструктор команды RemoveGreater.
     *
     * @param collectionManager менеджер коллекции транспортных средств.
     * @param scanner источник ввода для запроса данных.
     */
    public RemoveGreater(CollectionManager collectionManager, Scanner scanner) {
        this.collectionManager = collectionManager;
        this.scanner = scanner;
    }

    /**
     * Выполняет команду remove_greater, удаляя из коллекции все элементы,
     * у которых значение enginePower больше, чем у нового элемента.
     *
     * @param args аргументы команды (не используются).
     */
    @Override
    public void execute(String[] args) {
        System.out.println("Введите данные для элемента-порог (элементы с большим enginePower будут удалены):");
        String name = promptString("Введите имя транспортного средства (не пустая строка): ", true);
        long x = promptLong("Введите координату X (максимум 225): ", 0, 225);
        int y = promptInt("Введите координату Y (целое число, максимум 493): ", 0, 493);
        float enginePowerThreshold = promptFloat("Введите мощность двигателя (больше 0): ", 0, Float.MAX_VALUE);
        VehicleType type = promptEnum("Введите тип транспортного средства (BOAT, CHOPPER, HOVERBOARD, SPACESHIP) или пустую строку для null: ", VehicleType.class);
        FuelType fuelType = promptEnum("Введите тип топлива (GASOLINE, KEROSENE, NUCLEAR, PLASMA) или пустую строку для null: ", FuelType.class);

        // Создаем временный объект, из которого берется пороговое значение
        Vehicle thresholdVehicle = new Vehicle(0, name, new Coordinates(x, y), enginePowerThreshold, type, fuelType);

        Hashtable<Integer, Vehicle> collection = collectionManager.getCollection();
        List<Integer> keysToRemove = new ArrayList<>();
        for (Integer key : collection.keySet()) {
            Vehicle v = collection.get(key);
            if (v.getEnginePower() > thresholdVehicle.getEnginePower()) {
                keysToRemove.add(key);
            }
        }
        for (Integer key : keysToRemove) {
            collection.remove(key);
        }
        System.out.println("Удалено " + keysToRemove.size() + " элемент(ов) с enginePower больше " + enginePowerThreshold + ".");
    }

    private String promptString(String prompt, boolean nonEmpty) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (nonEmpty && input.isEmpty()) {
                System.out.println("Ошибка: строка не может быть пустой.");
            } else {
                break;
            }
        }
        return input;
    }

    private long promptLong(String prompt, long min, long max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                long value = Long.parseLong(input);
                if (value < min || value > max) {
                    System.out.println("Ошибка: значение должно быть в диапазоне [" + min + ", " + max + "].");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите корректное число.");
            }
        }
    }

    private int promptInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value < min || value > max) {
                    System.out.println("Ошибка: значение должно быть в диапазоне [" + min + ", " + max + "].");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите целое число.");
            }
        }
    }

    private float promptFloat(String prompt, float min, float max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                float value = Float.parseFloat(input);
                if (value <= min || value > max) {
                    System.out.println("Ошибка: значение должно быть больше " + min + " и не превышать " + max + ".");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите число с плавающей запятой.");
            }
        }
    }

    private <E extends Enum<E>> E promptEnum(String prompt, Class<E> enumClass) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return null;
            }
            try {
                return Enum.valueOf(enumClass, input.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.print("Доступные значения: ");
                for (E constant : enumClass.getEnumConstants()) {
                    System.out.print(constant.name() + " ");
                }
                System.out.println();
                System.out.println("Ошибка: введите одно из указанных значений.");
            }
        }
    }

    /**
     * Возвращает краткое описание команды RemoveGreater.
     *
     * @return описание команды.
     */
    @Override
    public String getDescription() {
        return "remove_greater {element} - удалить из коллекции все элементы, enginePower которых больше заданного";
    }
}
