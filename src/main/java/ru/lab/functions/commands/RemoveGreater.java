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

        this.collectionManager.removeVehicleWithEnginePowerGreaterThen(enginePowerThreshold);
    }


    @Override
    public String getDescription() {
        return "remove_greater {element} - удалить из коллекции все элементы, enginePower которых больше заданного. \n(используйте \\stop_running_command если хотите прервать выполнение команды)";
    }
}
