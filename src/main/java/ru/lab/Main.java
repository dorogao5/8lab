package ru.lab;

import ru.lab.model.Vehicle;
import ru.lab.util.*;


import java.util.Hashtable;

/**
 * Точка входа в приложение.
 * Имя CSV файла передается через аргумент командной строки.
 */
public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Необходимо указать имя CSV файла в качестве аргумента командной строки.");
            return;
        }

        String fileName = args[0];
        IFileManager fileManager = new FileManager();
        Hashtable<Integer, Vehicle> collection;

        try {
            collection = fileManager.load(fileName);
            System.out.println("Коллекция успешно загружена из файла: " + fileName);
        } catch (Exception e) {
            System.err.println("Ошибка при загрузке коллекции: " + e.getMessage());
            e.printStackTrace();
            // Если загрузка не удалась, начинаем с пустой коллекции
            collection = new Hashtable<>();
        }

        // Создаем менеджер коллекции
        CollectionManager collectionManager = new CollectionManager(collection);

        // Демонстрация: выводим количество элементов в коллекции
        System.out.println("Количество элементов в коллекции: " + collectionManager.getCollection().size());

        // Далее можно запустить интерактивный режим (например, через Invoker) и работать с коллекцией
    }
}
