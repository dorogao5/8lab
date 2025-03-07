package ru.lab.util;

import ru.lab.model.Vehicle;
import java.util.Date;
import java.util.Hashtable;

/**
 * Класс для управления коллекцией транспортных средств.
 * Коллекция хранится в Hashtable, ключ – id транспортного средства.
 */
public class CollectionManager implements ICollectionManager {
    private Hashtable<Integer, Vehicle> collection;
    private Date initializationDate;
    private int nextId;

    public CollectionManager() {
        this.collection = new Hashtable<>();
        this.initializationDate = new Date();
        this.nextId = 1;
    }

    public CollectionManager(Hashtable<Integer, Vehicle> collection) {
        this.collection = collection;
        this.initializationDate = new Date();
        this.nextId = computeNextId();
    }

    private int computeNextId() {
        int max = 0;
        for (int id : collection.keySet()) {
            if (id > max) {
                max = id;
            }
        }
        return max + 1;
    }

    private int generateId() {
        return nextId++;
    }

    @Override
    public Hashtable<Integer, Vehicle> getCollection() {
        return collection;
    }

    @Override
    public void addVehicle(Vehicle vehicle) {
        if (vehicle.getId() <= 0) { // если id не установлен, генерируем его
            vehicle.setId(generateId());
        }
        collection.put(vehicle.getId(), vehicle);
    }
}
