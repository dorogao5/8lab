package ru.lab.util;

import ru.lab.model.Vehicle;
import java.util.Date;
import java.util.Hashtable;

/**
 * Класс для управления коллекцией транспортных средств.
 * Коллекция хранится в Hashtable, где ключ &amp;lt;= id транспортного средства.
 */
public class CollectionManager implements ICollectionManager {
    private Hashtable<Integer, Vehicle> collection;
    private final Date initializationDate;
    private int nextId;

    /**
     * Конструктор без параметров.
     * Инициализирует пустую коллекцию и устанавливает дату инициализации.
     */
    public CollectionManager() {
        this.collection = new Hashtable<>();
        this.initializationDate = new Date();
        this.nextId = 1;
    }

    /**
     * Конструктор с передачей существующей коллекции.
     *
     * @param collection Hashtable с транспортными средствами.
     */
    public CollectionManager(Hashtable<Integer, Vehicle> collection) {
        this.collection = collection;
        this.initializationDate = new Date();
        this.nextId = computeNextId();
    }

    /**
     * Вычисляет следующий уникальный id на основе текущей коллекции.
     *
     * @return следующий уникальный id.
     */
    private int computeNextId() {
        int max = 0;
        for (int id : collection.keySet()) {
            if (id > max) {
                max = id;
            }
        }
        return max + 1;
    }

    /**
     * Генерирует новый уникальный id.
     *
     * @return сгенерированный id.
     */
    private int generateId() {
        return nextId++;
    }

    /**
     * Возвращает коллекцию транспортных средств.
     *
     * @return Hashtable с объектами Vehicle.
     */
    @Override
    public Hashtable<Integer, Vehicle> getCollection() {
        return collection;
    }

    /**
     * Добавляет транспортное средство в коллекцию.
     * Если у объекта не задан id (id &amp;lt;= 0), генерирует уникальный id.
     *
     * @param vehicle объект Vehicle для добавления.
     */
    @Override
    public void addVehicle(Vehicle vehicle) {
        if (vehicle.getId() <= 0) {
            vehicle.setId(generateId());
        }
        collection.put(vehicle.getId(), vehicle);
    }

    /**
     * Возвращает дату инициализации коллекции.
     *
     * @return дата инициализации.
     */
    public Date getInitializationDate() {
        return initializationDate;
    }
}
