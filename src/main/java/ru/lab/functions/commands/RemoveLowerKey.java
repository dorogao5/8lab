package ru.lab.functions.commands;

import ru.lab.functions.Command;
import ru.lab.util.CollectionManager;
import ru.lab.model.Vehicle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

/**
 * Команда remove_lower_key null - удалить из коллекции все элементы, ключ которых меньше заданного,
 * с перенумерацией оставшихся элементов.
 */
public class RemoveLowerKey implements Command {
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды RemoveLowerKey.
     *
     * @param collectionManager менеджер коллекции транспортных средств.
     */
    public RemoveLowerKey(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду, удаляя все элементы с ключом меньше заданного.
     * Формат: remove_lower_key &lt;ключ&gt;
     *
     * @param args аргументы команды, первый аргумент - ключ.
     */
    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            System.out.println("Ошибка: необходимо указать ключ для удаления элементов с меньшим значением.");
            return;
        }
        int givenKey;
        try {
            givenKey = Integer.parseInt(args[0]);
            if (givenKey <= 0) {
                System.out.println("Ошибка: ключ должен быть положительным числом.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ключ должен быть числом.");
            return;
        }

        Hashtable<Integer, Vehicle> collection = collectionManager.getCollection();
        List<Integer> keysToRemove = new ArrayList<>();
        for (Integer key : collection.keySet()) {
            if (key < givenKey) {
                keysToRemove.add(key);
            }
        }

        for (Integer key : keysToRemove) {
            collection.remove(key);
        }

        // Перенумеровываем оставшиеся элементы, чтобы ключи шли подряд начиная с 1
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

        System.out.println("Удалено " + keysToRemove.size() + " элемент(ов) с ключом меньше " + givenKey + ".");
    }

    /**
     * Возвращает краткое описание команды RemoveLowerKey.
     *
     * @return описание команды.
     */
    @Override
    public String getDescription() {
        return "remove_lower_key null - удалить из коллекции все элементы, ключ которых меньше заданного";
    }
}
