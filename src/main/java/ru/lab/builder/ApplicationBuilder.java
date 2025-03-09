package ru.lab.builder;

import ru.lab.util.IFileManager;
import ru.lab.util.FileManager;
import ru.lab.model.Vehicle;
import ru.lab.util.CollectionManager;
import ru.lab.builder.Console;
import ru.lab.functions.Command;
import ru.lab.functions.Invoker;
import ru.lab.functions.commands.Help;
import ru.lab.functions.commands.Info;
import ru.lab.functions.commands.Show;
import ru.lab.functions.commands.Exit;
import ru.lab.functions.commands.History;
import ru.lab.functions.commands.Clear;
import ru.lab.functions.commands.Save;

import java.util.Hashtable;
import java.util.Scanner;

/**
 * Класс для построения и начальной настройки приложения.
 * &lt;p&gt;
 * Приложение загружает коллекцию транспортных средств из CSV файла, имя которого передаётся
 * в качестве аргумента командной строки, и регистрирует базовые команды для интерактивного режима.
 */
public class ApplicationBuilder {
    private final Scanner scanner;
    private final Invoker invoker;
    private final CollectionManager collectionManager;
    private final IFileManager fileManager;
    private final String fileName;

    /**
     * Конструктор, выполняющий начальную настройку приложения.
     *
     * @param args аргументы командной строки; первый аргумент должен содержать имя CSV файла.
     * @throws IllegalArgumentException если имя файла не указано.
     */
    public ApplicationBuilder(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Необходимо указать имя CSV файла в качестве аргумента командной строки.");
        }
        fileName = args[0];
        fileManager = new FileManager();

        Hashtable<Integer, Vehicle> loadedCollection;
        try {
            loadedCollection = fileManager.load(fileName);
            System.out.println("Коллекция успешно загружена из файла: " + fileName);
        } catch (Exception e) {
            System.err.println("Ошибка при загрузке коллекции: " + e.getMessage());
            e.printStackTrace();
            // Если загрузка не удалась, начинаем с пустой коллекции
            loadedCollection = new Hashtable<>();
        }

        // Создаем менеджер коллекции и добавляем все загруженные объекты
        collectionManager = new CollectionManager();
        for (Vehicle vehicle : loadedCollection.values()) {
            collectionManager.addVehicle(vehicle);
        }

        scanner = new Scanner(System.in);
        invoker = new Invoker();
        // Регистрация базовых команд
        invoker.register("help", new Help(invoker.getCommands()));
        invoker.register("info", new Info(collectionManager));
        invoker.register("show", new Show(collectionManager));
        invoker.register("exit", new Exit());
        invoker.register("history", new History(invoker));
        invoker.register("clear", new Clear(collectionManager));
        invoker.register("save", new Save(fileManager, collectionManager, fileName));
        // Дополнительная регистрация команд может быть выполнена здесь
    }

    /**
     * Создает и возвращает экземпляр Console для интерактивного режима.
     *
     * @return объект Console.
     */
    public Console createConsole() {
        return new Console(scanner, invoker);
    }

    /**
     * Возвращает менеджер коллекции.
     *
     * @return объект CollectionManager.
     */
    public CollectionManager getCollectionManager() {
        return collectionManager;
    }

    /**
     * Возвращает файловый менеджер.
     *
     * @return объект IFileManager.
     */
    public IFileManager getFileManager() {
        return fileManager;
    }

    /**
     * Возвращает инвокер команд.
     *
     * @return объект Invoker.
     */
    public Invoker getInvoker() {
        return invoker;
    }
}
