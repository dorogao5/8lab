package ru.lab.functions.commands;

import ru.lab.functions.Command;
import ru.lab.util.CollectionManager;
import ru.lab.model.Vehicle;
import ru.lab.model.Coordinates;
import ru.lab.model.VehicleType;
import ru.lab.model.FuelType;

import java.util.Scanner;
import java.util.Hashtable;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Команда для добавления нового элемента с заданным ключом.
 * Формат: insert &lt;ключ&gt; - затем осуществляется запрос данных для нового элемента.
 * При добавлении, все элементы с ключами &gt;= заданному смещаются (их ключ увеличивается на 1).
 */
public class Insert implements Command {
    private final CollectionManager collectionManager;
    private final Scanner scanner;

    /**
     * Конструктор команды Insert.
     *
     * @param collectionManager менеджер коллекции транспортных средств.
     * @param scanner источник ввода для запроса данных.
     */
    public Insert(CollectionManager collectionManager, Scanner scanner) {
        this.collectionManager = collectionManager;
        this.scanner = scanner;
    }

    /**
     * Выполняет команду Insert.
     * Ожидается, что первый аргумент команды - это ключ для нового элемента.
     *
     * @param args аргументы команды.
     */
    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            System.out.println("Ошибка: необходимо указать ключ для вставки.");
            return;
        }
        int insertKey;
        try {
            insertKey = Integer.parseInt(args[0]);
            if (insertKey <= 0) {
                System.out.println("Ошибка: ключ должен быть положительным числом.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ключ должен быть числом.");
            return;
        }

        // Запрос данных для нового элемента (поля id и creationDate генерируются автоматически)
        String name = promptString("Введите имя транспортного средства (не пустая строка): ", true);
        long x = promptLong("Введите координату X (максимум 225): ", 0, 225);
        int y = promptInt("Введите координату Y (целое число, максимум 493): ", 0, 493);
        float enginePower = promptFloat("Введите мощность двигателя (больше 0): ", 0, Float.MAX_VALUE);
        VehicleType type = promptEnum("Введите тип транспортного средства (BOAT, CHOPPER, HOVERBOARD, SPACESHIP) или пустую строку для null: ", VehicleType.class);
        FuelType fuelType = promptEnum("Введите тип топлива (GASOLINE, KEROSENE, NUCLEAR, PLASMA) или пустую строку для null: ", FuelType.class);

        // Создаем новый объект Vehicle (id и creationDate генерируются автоматически)
        Vehicle newVehicle = new Vehicle(0, name, new Coordinates(x, y), enginePower, type, fuelType);
        newVehicle.setId(insertKey);

        Hashtable<Integer, Vehicle> collection = collectionManager.getCollection();
        // Сдвигаем все элементы с ключом >= insertKey
        List<Integer> keysToShift = new ArrayList<>();
        for (Integer key : collection.keySet()) {
            if (key >= insertKey) {
                keysToShift.add(key);
            }
        }
        // Сортируем ключи по убыванию для безопасного сдвига
        Collections.sort(keysToShift, Collections.reverseOrder());
        for (Integer key : keysToShift) {
            Vehicle v = collection.remove(key);
            int newKey = key + 1;
            v.setId(newKey);
            collection.put(newKey, v);
        }
        // Вставляем новый элемент
        collection.put(insertKey, newVehicle);
        System.out.println("Элемент успешно добавлен с ключом " + insertKey + ".");
    }

    // Вспомогательные методы для запроса ввода

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
     * Возвращает краткое описание команды Insert.
     *
     * @return описание команды.
     */
    @Override
    public String getDescription() {
        return "insert null {element} - добавить новый элемент с заданным ключом, сдвигая элементы с ключами >= заданного";
    }
}
