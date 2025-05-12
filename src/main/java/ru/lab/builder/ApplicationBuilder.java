package ru.lab.builder;

import ru.lab.functions.commands.*;
import ru.lab.util.DBCollectionManager;
import ru.lab.util.IFileManager;
import ru.lab.util.FileManager;
import ru.lab.model.Vehicle;
import ru.lab.util.CollectionManager;
import ru.lab.functions.Invoker;

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

    private final DBCollectionManager dbManager;

    /**
     * Конструктор, выполняющий начальную настройку приложения.
     *
     * @param args аргументы командной строки; первый аргумент должен содержать имя CSV файла.
     * @throws IllegalArgumentException если имя файла не указано.
     */
    public ApplicationBuilder(String[] args) throws IllegalArgumentException {

        fileName = null;
        fileManager = new FileManager();

        dbManager = new DBCollectionManager();
        //коллекция будет загружаться из базы данных
        Hashtable<Integer, Vehicle> loadedCollection;
        try {
            loadedCollection = dbManager.getCollection();
            // System.out.println("1.0 коллекция успешно загружена из базы данных.");
            // System.out.println("loaded collection with [" + loadedCollection.size() + "] elements");
        } catch (Exception e) {
            // System.err.println("2.0 ошибка при загрузке коллекции: " + e.getMessage());
            loadedCollection = new Hashtable<>();
            System.exit(-1);
        }

        //System.exit(-1);

        collectionManager = new CollectionManager(loadedCollection, dbManager);


        scanner = new Scanner(System.in);
        invoker = new Invoker();
        scriptManager = new ScriptManager();

        // Регистрация базовых команд
        invoker.register("help", new Help(invoker.getCommands()));//ok
        invoker.register("register", new RegisterUser(createConsoleForInput()));
        invoker.register("login", new LoginUser(createConsoleForInput()));
        invoker.register("logout", new LogoutUser(createConsoleForInput()));
        invoker.register("info", new Info(collectionManager)); //ok
        invoker.register("show", new Show(collectionManager)); //ok
        invoker.register("exit", new Exit()); //ok
        invoker.register("history", new History(invoker)); //ok
        invoker.register("clear", new Clear(collectionManager)); //ok
        invoker.register("save", new Save(collectionManager));
        invoker.register("insert", new Insert(collectionManager, createConsoleForInput())); //ok
        invoker.register("update", new Update(collectionManager, createConsoleForInput())); //ok
        invoker.register("remove_key", new RemoveKey(collectionManager)); //ok
        invoker.register("remove_lower_key", new RemoveLowerKey(collectionManager)); //ok
        invoker.register("remove_greater", new RemoveGreater(collectionManager, createConsoleForInput())); //ok
        invoker.register("remove_all_by_type", new RemoveAllByType(collectionManager, createConsoleForInput())); //ok
        invoker.register("print_field_ascending_fuel_type", new PrintFieldAscendingFuelType(collectionManager)); //not needed
        invoker.register("group_counting_by_engine_power", new GroupCountingByEnginePower(collectionManager)); //not needed
        invoker.register("execute_script", new ExecuteScript(invoker, scriptManager)); //not needed

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
