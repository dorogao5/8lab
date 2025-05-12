package ru.lab.functions.commands;

import ru.lab.functions.AbstractCommand;
import ru.lab.util.CollectionManager;
import ru.lab.model.Vehicle;
import ru.lab.model.Coordinates;
import ru.lab.model.VehicleType;
import ru.lab.model.FuelType;
import ru.lab.builder.Console;
import ru.lab.util.DBUserManager;

/**
 * Команда для обновления значения элемента коллекции по заданному ключу.
 * Формат: update &lt;key> - затем последовательно считываются данные нового элемента.
 */
public class Update extends AbstractCommand {
    private final CollectionManager collectionManager;
    private final Console console; // Используем интерактивный ввод

    /**
     * Конструктор команды Update.
     *
     * @param collectionManager менеджер коллекции транспортных средств.
     * @param console           объект Console для считывания данных.
     */
    public Update(CollectionManager collectionManager, Console console) {
        super(console);
        this.collectionManager = collectionManager;
        this.console = console;
    }

    @Override
    public void execute(String[] args) {
        int updateKey;
        try {
            if (args.length < 1) {
                String input = console.readInteractiveLine("Ошибка: необходимо указать ключ для обновления. Введите ключ:");
                updateKey = Integer.parseInt(input.trim());
            } else {
                updateKey = Integer.parseInt(args[0].trim());
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ключ должен быть целым числом.");
            return;
        }
        if (updateKey <= 0) {
            System.out.println("Ошибка: ключ должен быть положительным числом.");
            return;
        }
        if (!collectionManager.getCollection().containsKey(updateKey)) {
            System.out.println("Ошибка: элемент с ключом " + updateKey + " не найден.");
            return;
        }
        if(DBUserManager.getInstance().getCurrentUser() == null) {
            System.out.println("Нужно авторизоваться для выполнения этой операции");
            return;
        }

        if(!collectionManager.getCollection().get(updateKey).getOwner().equals(DBUserManager.getInstance().getCurrentUser().getUsername())) {
            System.out.println("Ошибка: элемент с ключом " + updateKey + " принадлежит другому пользователю [" +
                    collectionManager.getCollection().get(updateKey).getOwner() + "]");
            return;
        }

        // Считываем данные для нового элемента через интерактивный ввод (игнорируя строки из скрипта)
        String name = promptString("Введите имя транспортного средства (не пустая строка): ", true);
        long x = promptLong("Введите координату X (0..225): ", 0, 225);
        int y = promptInt("Введите координату Y (0..493): ", 0, 493);
        float enginePower = promptFloat("Введите мощность двигателя (> 0): ", 0, Float.MAX_VALUE);
        VehicleType vtype = promptEnum("Введите тип транспортного средства (BOAT, CHOPPER, HOVERBOARD, SPACESHIP) или пустую строку для null: ", VehicleType.class);
        FuelType ftype = promptEnum("Введите тип топлива (GASOLINE, KEROSENE, NUCLEAR, PLASMA) или пустую строку для null: ", FuelType.class);


        Vehicle updatedVehicle = collectionManager.getCollection().get(updateKey);
        updatedVehicle.setName(name);
        updatedVehicle.setCoordinates(new Coordinates(x, y));
        updatedVehicle.setEnginePower(enginePower);
        updatedVehicle.setType(vtype);
        updatedVehicle.setFuelType(ftype);

        collectionManager.updateVehicle(updatedVehicle);
        System.out.println("Элемент с ключом " + updateKey + " успешно обновлен.");
    }

    @Override
    public String getDescription() {
        return "update <key> - обновить значение элемента коллекции, id которого равен заданному. \n(используйте \\stop_running_command если хотите прервать выполнение команды)";
    }
}
