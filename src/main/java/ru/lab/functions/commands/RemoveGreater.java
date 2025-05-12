package ru.lab.functions.commands;

import ru.lab.functions.AbstractCommand;
import ru.lab.util.CollectionManager;
import ru.lab.builder.Console;
import ru.lab.util.DBUserManager;

/**
 * Команда remove_greater {element} - удалить из коллекции все элементы,
 * у которых значение поля enginePower больше, чем у введённого элемента.
 * <p>
 * Формат: команда без аргументов, затем последовательно запрашиваются данные для элемента-порога.
 */
public class RemoveGreater extends AbstractCommand {
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды RemoveGreater.
     *
     * @param collectionManager менеджер коллекции транспортных средств.
     * @param console           объект Console для считывания данных.
     */
    public RemoveGreater(CollectionManager collectionManager, Console console) {
        super(console);
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду, удаляя из коллекции все элементы,
     * у которых значение enginePower больше, чем у нового введённого элемента.
     *
     * @param args аргументы команды (не используются).
     */
    @Override
    public void execute(String[] args) {
        if(DBUserManager.getInstance().getCurrentUser() == null) {
            System.out.println("Нужно авторизоваться для выполнения этой операции");
            return;
        }

        float enginePowerThreshold = promptFloat("Введите порог мощности двигателя (enginePower): ", 0, Float.MAX_VALUE);

        /*
        Hashtable<Integer, Vehicle> collection = collectionManager.getCollection();
        List<Integer> keysToRemove = new ArrayList<>();
        for (Integer key : collection.keySet()) {
            Vehicle v = collection.get(key);
            if (v.getEnginePower() > enginePowerThreshold &&
                    v.getOwner().equals(UserManager.getInstance().getCurrentUser().getUsername())) {
                keysToRemove.add(key);
            }
        }
        for (Integer key : keysToRemove) {
            collection.remove(key);
        }

        // Перенумеровываем элементы
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

        this.collectionManager.removeVehicleWithEnginePowerGreaterThen(enginePowerThreshold);
    }


    @Override
    public String getDescription() {
        return "remove_greater {element} - удалить из коллекции все элементы, enginePower которых больше заданного. \n(используйте \\stop_running_command если хотите прервать выполнение команды)";
    }
}
