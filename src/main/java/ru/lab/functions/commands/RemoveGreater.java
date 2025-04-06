package ru.lab.functions.commands;

import ru.lab.functions.Command;
import ru.lab.util.CollectionManager;
import ru.lab.model.Vehicle;
import ru.lab.builder.Console;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Команда remove_greater {element} - удалить из коллекции все элементы,
 * у которых значение поля enginePower больше, чем у введённого элемента.
 * <p>
 * Формат: команда без аргументов, затем последовательно запрашиваются данные для элемента-порога.
 */
public class RemoveGreater implements Command {
    private final CollectionManager collectionManager;
    private final Console console; // Используем Console для поддержки скриптов

    /**
     * Конструктор команды RemoveGreater.
     *
     * @param collectionManager менеджер коллекции транспортных средств.
     * @param console           объект Console для считывания данных.
     */
    public RemoveGreater(CollectionManager collectionManager, Console console) {
        this.collectionManager = collectionManager;
        this.console = console;
    }

    /**
     * Выполняет команду, удаляя из коллекции все элементы,
     * у которых значение enginePower больше, чем у нового введённого элемента.
     *
     * @param args аргументы команды (не используются).
     */
    @Override
    public void execute(String[] args) {
        float enginePowerThreshold = promptFloat("Введите порог мощности двигателя (enginePower): ", 0, Float.MAX_VALUE);
        Hashtable<Integer, Vehicle> collection = collectionManager.getCollection();
        List<Integer> keysToRemove = new ArrayList<>();
        for (Integer key : collection.keySet()) {
            Vehicle v = collection.get(key);
            if (v.getEnginePower() > enginePowerThreshold) {
                keysToRemove.add(key);
            }
        }
        for (Integer key : keysToRemove) {
            collection.remove(key);
        }
        System.out.println("Удалено " + keysToRemove.size() + " элемент(ов) с enginePower больше " + enginePowerThreshold + ".");
    }

    // Вспомогательные методы, использующие console.readLine(prompt)
    private float promptFloat(String prompt, float min, float max) {
        while (true) {
            String input = console.readLine(prompt);
            try {
                float value = Float.parseFloat(input);
                if (value <= min || value > max) {
                    System.out.println("Ошибка: значение должно быть больше " + min + ".");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите число с плавающей запятой.");
            }
        }
    }

    @Override
    public String getDescription() {
        return "remove_greater {element} - удалить из коллекции все элементы, enginePower которых больше заданного. \n(используйте \\stop_running_command если хотите прервать выполнение команды)";
    }
}
