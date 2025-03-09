package ru.lab.builder;

import ru.lab.util.IFileManager;
import ru.lab.util.FileManager;
import ru.lab.model.Vehicle;
import ru.lab.util.CollectionManager;
import ru.lab.builder.Console;
import ru.lab.builder.ScriptManager;
import ru.lab.functions.Invoker;
import ru.lab.functions.commands.Help;
import ru.lab.functions.commands.Info;
import ru.lab.functions.commands.Show;
import ru.lab.functions.commands.Exit;
import ru.lab.functions.commands.History;
import ru.lab.functions.commands.Clear;
import ru.lab.functions.commands.Save;
import ru.lab.functions.commands.Insert;
import ru.lab.functions.commands.Update;
import ru.lab.functions.commands.RemoveKey;
import ru.lab.functions.commands.RemoveGreater;
import ru.lab.functions.commands.RemoveLowerKey;
import ru.lab.functions.commands.RemoveAllByType;
import ru.lab.functions.commands.PrintFieldAscendingFuelType;
import ru.lab.functions.commands.GroupCountingByEnginePower;
import ru.lab.functions.commands.ExecuteScript;

import java.util.Hashtable;
import java.util.Scanner;

/**
 * Класс для построения и начальной настройки приложения.
 * <p>
 * Приложение загружает коллекцию транспортных средств из CSV файла, имя которого передаётся
 * в качестве аргумента командной строки, и регистрирует базовые команды для интерактивного режима.
 */
public class ApplicationBuilder {
    private final Scanner scanner;
    private final Invoker invoker;
    private final CollectionManager collectionManager;
    private final IFileManager fileManager;
    private final String fileName;
    private final ScriptManager scriptManager; // менеджер строк для скриптов

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
            loadedCollection = new Hashtable<>();
        }

        collectionManager = new CollectionManager();
        for (Vehicle vehicle : loadedCollection.values()) {
            collectionManager.addVehicle(vehicle);
        }

        scanner = new Scanner(System.in);
        invoker = new Invoker();
        scriptManager = new ScriptManager();

        // Регистрация базовых команд
        invoker.register("help", new Help(invoker.getCommands()));
        invoker.register("info", new Info(collectionManager));
        invoker.register("show", new Show(collectionManager));
        invoker.register("exit", new Exit());
        invoker.register("history", new History(invoker));
        invoker.register("clear", new Clear(collectionManager));
        invoker.register("save", new Save(fileManager, collectionManager, fileName));
        invoker.register("insert", new Insert(collectionManager, createConsoleForInput()));
        invoker.register("update", new Update(collectionManager, createConsoleForInput()));
        invoker.register("remove_key", new RemoveKey(collectionManager));
        invoker.register("remove_greater", new RemoveGreater(collectionManager, createConsoleForInput()));
        invoker.register("remove_lower_key", new RemoveLowerKey(collectionManager));
        invoker.register("remove_all_by_type", new RemoveAllByType(collectionManager, createConsoleForInput()));
        invoker.register("print_field_ascending_fuel_type", new PrintFieldAscendingFuelType(collectionManager));
        invoker.register("group_counting_by_engine_power", new GroupCountingByEnginePower(collectionManager));
        invoker.register("execute_script", new ExecuteScript(invoker, scriptManager));
    }

    /**
     * Создаёт и возвращает объект Console, который использует тот же Scanner, Invoker и ScriptManager.
     * Этот Console используется как источник ввода для многострочных команд.
     *
     * @return объект Console.
     */
    private Console createConsoleForInput() {
        return new Console(scanner, invoker, scriptManager);
    }

    /**
     * Создает и возвращает экземпляр Console для основного интерактивного режима.
     *
     * @return объект Console.
     */
    public Console createConsole() {
        return new Console(scanner, invoker, scriptManager);
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
