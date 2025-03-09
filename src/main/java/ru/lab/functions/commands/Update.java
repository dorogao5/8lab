package ru.lab.functions.commands;

import ru.lab.functions.Command;
import ru.lab.util.CollectionManager;
import ru.lab.model.Vehicle;
import ru.lab.model.Coordinates;
import ru.lab.model.VehicleType;
import ru.lab.model.FuelType;

import java.util.Scanner;

/**
 * Команда для обновления значения элемента коллекции по заданному ключу.
 * Формат: update &lt;ключ&gt; - затем осуществляется запрос данных для нового элемента.
 */
public class Update implements Command {
    private final CollectionManager collectionManager;
    private final Scanner scanner;

    /**
     * Конструктор команды Update.
     *
     * @param collectionManager менеджер коллекции транспортных средств.
     * @param scanner источник ввода для запроса данных.
     */
    public Update(CollectionManager collectionManager, Scanner scanner) {
        this.collectionManager = collectionManager;
        this.scanner = scanner;
    }

    /**
     * Выполняет команду Update, обновляя элемент коллекции с заданным ключом.
     *
     * @param args аргументы команды; первый аргумент должен содержать ключ элемента.
     */
    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            System.out.println("Ошибка: необходимо указать ключ для обновления.");
            return;
        }
        int updateKey;
        try {
            updateKey = Integer.parseInt(args[0]);
            if (updateKey <= 0) {
                System.out.println("Ошибка: ключ должен быть положительным числом.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ключ должен быть числом.");
            return;
        }
        if (!collectionManager.getCollection().containsKey(updateKey)) {
            System.out.println("Ошибка: элемент с ключом " + updateKey + " не найден.");
            return;
        }

        // Запрос данных для нового элемента (id и creationDate генерируются автоматически)
        String name = promptString("Введите имя транспортного средства (не пустая строка): ", true);
        long x = promptLong("Введите координату X (максимум 225): ", 0, 225);
        int y = promptInt("Введите координату Y (целое число, максимум 493): ", 0, 493);
        float enginePower = promptFloat("Введите мощность двигателя (больше 0): ", 0, Float.MAX_VALUE);
        VehicleType type = promptEnum("Введите тип транспортного средства (BOAT, CHOPPER, HOVERBOARD, SPACESHIP) или пустую строку для null: ", VehicleType.class);
        FuelType fuelType = promptEnum("Введите тип топлива (GASOLINE, KEROSENE, NUCLEAR, PLASMA) или пустую строку для null: ", FuelType.class);

        // Создаем новый объект Vehicle с введенными данными
        Vehicle newVehicle = new Vehicle(0, name, new Coordinates(x, y), enginePower, type, fuelType);
        // Присваиваем обновляемому элементу ключ updateKey
        newVehicle.setId(updateKey);
        collectionManager.getCollection().put(updateKey, newVehicle);
        System.out.println("Элемент с ключом " + updateKey + " успешно обновлен.");
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
     * Возвращает краткое описание команды Update.
     *
     * @return описание команды.
     */
    @Override
    public String getDescription() {
        return "update {element} - обновить значение элемента коллекции, id которого равен заданному";
    }
}
