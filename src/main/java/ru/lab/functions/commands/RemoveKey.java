package ru.lab.functions.commands;

import ru.lab.functions.Command;
import ru.lab.util.CollectionManager;
import ru.lab.model.Vehicle;
import ru.lab.util.DBUserManager;

import java.util.Hashtable;

/**
 * Команда для удаления элемента коллекции по заданному ключу.
 * При удалении элемента все элементы с ключами больше удаляемого смещаются на 1 вниз.
 */
public class RemoveKey implements Command {
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды RemoveKey.
     *
     * @param collectionManager менеджер коллекции транспортных средств.
     */
    public RemoveKey(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду RemoveKey, удаляя элемент коллекции с заданным ключом
     * и перенумеровывая оставшиеся элементы.
     *
     * @param args аргументы команды; первый аргумент должен содержать ключ для удаления.
     */
    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            System.out.println("Ошибка: необходимо указать ключ для удаления.");
            return;
        }
        int removeKey;
        try {
            removeKey = Integer.parseInt(args[0]);
            if (removeKey <= 0) {
                System.out.println("Ошибка: ключ должен быть положительным числом.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ключ должен быть числом.");
            return;
        }

        if(DBUserManager.getInstance().getCurrentUser() == null) {
            System.out.println("Нужно авторизоваться для выполнения этой операции");
            return;
        }

        Hashtable<Integer, Vehicle> collection = collectionManager.getCollection();
        if (!collection.containsKey(removeKey)) {
            System.out.println("Ошибка: элемент с ключом " + removeKey + " не найден.");
            return;
        }

        if(!collectionManager.getCollection().get(removeKey).getOwner().equals(DBUserManager.getInstance().getCurrentUser().getUsername())) {
            System.out.println("Ошибка: элемент с ключом " + removeKey + " принадлежит другому пользователю [" +
                    collectionManager.getCollection().get(removeKey).getOwner() + "]");
            return;
        }


        /*
        // Удаляем элемент с заданным ключом
        collection.remove(removeKey);

        // Сдвигаем ключи для всех элементов с ключами > removeKey
        List<Integer> keysToShift = new ArrayList<>();
        for (Integer key : collection.keySet()) {
            if (key > removeKey) {
                keysToShift.add(key);
            }
        }
        // Сортировка по возрастанию
        Collections.sort(keysToShift);
        for (Integer key : keysToShift) {
            Vehicle v = collection.remove(key);
            int newKey = key - 1;
            v.setId(newKey);
            collection.put(newKey, v);
        }
        */

        this.collectionManager.removeVehicleWithID(removeKey, false);
    }

    /**
     * Возвращает краткое описание команды RemoveKey.
     *
     * @return описание команды.
     */
    @Override
    public String getDescription() {
        return "remove_key key_value - удалить элемент из коллекции по его ключу";
    }
}
