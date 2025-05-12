package ru.lab.functions.commands;

import ru.lab.functions.Command;
import ru.lab.util.CollectionManager;
import ru.lab.util.DBUserManager;

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
        if(DBUserManager.getInstance().getCurrentUser() == null) {
            System.out.println("Нужно авторизоваться для выполнения этой операции");
            return;
        }
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




        this.collectionManager.removeVehicleWithID(givenKey, true);
        //System.out.println("Удалено " + keysToRemove.size() + " элемент(ов) с ключом меньше " + givenKey + ".");
    }

    /**
     * Возвращает краткое описание команды RemoveLowerKey.
     *
     * @return описание команды.
     */
    @Override
    public String getDescription() {
        return "remove_lower_key key_value - удалить из коллекции все элементы, ключ которых меньше заданного";
    }
}
