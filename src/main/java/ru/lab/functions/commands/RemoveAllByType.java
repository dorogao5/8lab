package ru.lab.functions.commands;

import ru.lab.functions.Command;
import ru.lab.util.CollectionManager;
import ru.lab.model.VehicleType;
import ru.lab.builder.Console;
import ru.lab.util.DBUserManager;

/**
 * Команда remove_all_by_type type - удалить из коллекции все элементы, значение поля type которых эквивалентно заданному,
 * с перенумерацией оставшихся элементов.
 */
public class RemoveAllByType implements Command {
    private final CollectionManager collectionManager;
    private final Console console; // Используем Console для поддержки скриптов

    /**
     * Конструктор команды RemoveAllByType.
     *
     * @param collectionManager менеджер коллекции транспортных средств.
     * @param console           объект Console для считывания данных.
     */
    public RemoveAllByType(CollectionManager collectionManager, Console console) {
        this.collectionManager = collectionManager;
        this.console = console;
    }

    /**
     * Выполняет команду, удаляя все элементы с типом, эквивалентным заданному.
     * Формат: remove_all_by_type (тип)
     *
     * @param args аргументы команды.
     */
    @Override
    public void execute(String[] args) {

        String inputType;
        if (args.length < 1) {
            inputType = console.readInteractiveLine("Введите тип транспортного средства для удаления (BOAT, CHOPPER, HOVERBOARD, SPACESHIP, AUTO), для null нажмите Enter или введите NULL : ");
        } else {
            inputType = args[0].trim();
        }

        if(DBUserManager.getInstance().getCurrentUser() == null) {
            System.out.println("Нужно авторизоваться для выполнения этой операции");
            return;
        }

        VehicleType targetType = null;
        if (!inputType.isEmpty() & !inputType.equals("NULL")) {
            try {
                targetType = VehicleType.valueOf(inputType.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: некорректное значение типа. Доступные типы:");
                for (VehicleType vt : VehicleType.values()) {
                    System.out.print(vt.name() + " ");
                }
                System.out.println("\nИли введите пустую строку для удаления элементов с type == null.");
                return;
            }
        }

        /*
        Hashtable<Integer, Vehicle> collection = collectionManager.getCollection();
        List<Integer> keysToRemove = new ArrayList<>();
        for (Integer key : collection.keySet()) {
            Vehicle v = collection.get(key);
            if (targetType == null) {
                if (v.getType() == null && v.getOwner().equals(UserManager.getInstance().getCurrentUser().getUsername())) {
                    keysToRemove.add(key);
                }
            } else {
                if (targetType.equals(v.getType()) && v.getOwner().equals(UserManager.getInstance().getCurrentUser().getUsername())) {
                    keysToRemove.add(key);
                }
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
        */

        this.collectionManager.removeVehicleWithType(targetType);
    }

    @Override
    public String getDescription() {
        return "remove_all_by_type type - удалить из коллекции все элементы, значение поля type которых эквивалентно заданному. \n(используйте \\stop_running_command если хотите прервать выполнение команды)";
    }
}
