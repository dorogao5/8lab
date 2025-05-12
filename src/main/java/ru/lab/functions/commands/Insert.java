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
 * Команда для добавления нового элемента с заданным ключом.
 * Формат: insert &lt;key> - затем последовательно считываются поля нового элемента.
 * При добавлении все элементы с ключами >= заданному сдвигаются (их ключ увеличивается на 1).
 */
public class Insert extends AbstractCommand {
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды Insert.
     *
     * @param collectionManager менеджер коллекции транспортных средств.
     * @param console           объект Console, предоставляющий метод readInteractiveLine.
     */
    public Insert(CollectionManager collectionManager, Console console) {
        super(console);
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String[] args) {
        if(DBUserManager.getInstance().getCurrentUser() == null) {
            System.out.println("Нужно авторизоваться для выполнения этой операции");
            return;
        }

        /*
        int insertKey;
        if (args.length < 1) {
            insertKey  = Integer.parseInt(console.readInteractiveLine("Ошибка: необходимо указать ключ для вставки. Введите ключ: "));
        }
        else {
            insertKey = Integer.parseInt(args[0]);
        }
        Hashtable<Integer, Vehicle> collection = collectionManager.getCollection();
        if (insertKey <= 0) {
            System.out.println("Ошибка: ключ должен быть положительным числом.");
            return;
        }

        int maxKey = collection.isEmpty() ? 0 : collection.size();
        if (insertKey > maxKey + 1) {System.out.println("Ошибка: Невозможно вставить элемент с ключом " + insertKey +
                        ". Максимальный допустимый ключ: " + (maxKey + 1));
            return;
        }
        */

        // Считываем данные для нового элемента через интерактивный ввод (игнорируя строки из скрипта)
        String name = promptString("Введите имя транспортного средства (не пустая строка): ", true);
        long x = promptLong("Введите координату X (0..225): ", 0, 225);
        int y = promptInt("Введите координату Y (0..493): ", 0, 493);
        float enginePower = promptFloat("Введите мощность двигателя (> 0): ", 0, Float.MAX_VALUE);
        VehicleType vtype = promptEnum("Введите тип транспортного средства (BOAT, CHOPPER, HOVERBOARD, SPACESHIP, AUTO) или пустую строку для null: ", VehicleType.class);
        FuelType ftype = promptEnum("Введите тип топлива (GASOLINE, KEROSENE, NUCLEAR, PLASMA) или пустую строку для null: ", FuelType.class);

        // Создаем новый объект Vehicle
        Vehicle newVehicle = new Vehicle(0, name, new Coordinates(x, y), enginePower, vtype, ftype);

        collectionManager.addVehicle(newVehicle);
        /*
        newVehicle.setId(insertKey);

        // Сдвигаем элементы с ключами >= insertKey
        List<Integer> keysToShift = new ArrayList<>();
        for (Integer key : collection.keySet()) {
            if (key >= insertKey) {
                keysToShift.add(key);
            }
        }
        Collections.sort(keysToShift, Collections.reverseOrder());
        for (Integer key : keysToShift) {
            Vehicle v = collection.remove(key);
            int newKey = key + 1;
            v.setId(newKey);
            collection.put(newKey, v);
        }
        // Вставляем новый элемент
        collection.put(insertKey, newVehicle);
            */
        System.out.println("Элемент успешно добавлен с ключом " + newVehicle.getId() + ".");
    }

    @Override
    public String getDescription() {
        return "insert - добавить новый элемент с заданным ключом. \n(используйте \\stop_running_command если хотите прервать выполнение команды)";
    }
}
