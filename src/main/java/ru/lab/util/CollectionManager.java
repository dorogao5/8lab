package ru.lab.util;

import ru.lab.model.Vehicle;
import ru.lab.model.VehicleType;

import java.util.*;

/**
 * Класс для управления коллекцией транспортных средств.
 * Коллекция хранится в Hashtable, где ключ &amp;lt;= id транспортного средства.
 */
public class CollectionManager implements ICollectionManager {
    private DBCollectionManager dbCollectionManager;
    private Hashtable<Integer, Vehicle> collection;
    private final Date initializationDate;

    /**
     * Конструктор без параметров.
     * Инициализирует пустую коллекцию и устанавливает дату инициализации.
     */
    public CollectionManager() {
        this.collection = new Hashtable<>();
        this.initializationDate = new Date();
    }

    /**
     * Конструктор с передачей существующей коллекции.
     *
     * @param collection Hashtable с транспортными средствами.
     */
    public CollectionManager(Hashtable<Integer, Vehicle> collection, DBCollectionManager dbCollectionManager) {
        this.dbCollectionManager = dbCollectionManager;
        this.collection = collection;
        this.initializationDate = new Date();
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
        try {
            vehicle.setId(dbCollectionManager.getNextId());
            vehicle.setCreationDate(new Date());
            vehicle.setOwner(DBUserManager.getInstance().getCurrentUser().getUsername());
            this.collection.put(vehicle.getId(), vehicle);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateVehicle(Vehicle updatedVehicle) {
        this.collection.put(updatedVehicle.getId(), updatedVehicle);
    }


    public void removeVehicleWithID(Integer givenKey, boolean lower) {
        List<Integer> keysToRemove = new ArrayList<>();
        if (!lower) {
            keysToRemove.add(givenKey);
        } else {
            for (Integer key : this.collection.keySet()) {
                if (key < givenKey && this.collection.get(key).getOwner().equals(DBUserManager.getInstance().getCurrentUser().getUsername())) {
                    keysToRemove.add(key);
                }
            }
        }

        this.removeVehiclesFromCollection(keysToRemove);

        if (!lower) {
            System.out.println("Элемент с ключом " + givenKey + " удален");
        } else {
            System.out.println("Удалено " + keysToRemove.size() + " элемент(ов) с ключом меньше " + givenKey + ".");
        }
    }

    @Override
    public void removeVehicleWithEnginePowerGreaterThen(float enginePower) {
        Hashtable<Integer, Vehicle> collection = this.collection;
        List<Integer> keysToRemove = new ArrayList<>();
        for (Integer key : collection.keySet()) {
            Vehicle v = collection.get(key);
            if (v.getEnginePower() > enginePower && v.getOwner().equals(DBUserManager.getInstance().getCurrentUser().getUsername())) {
                keysToRemove.add(key);
            }
        }

        this.removeVehiclesFromCollection(keysToRemove);

        System.out.println("Удалено " + keysToRemove.size() + " элемент(ов) с enginePower больше " + enginePower + ".");
    }

    @Override
    public void removeVehicleWithType(VehicleType vehicleType) {
        Hashtable<Integer, Vehicle> collection = this.collection;
        List<Integer> keysToRemove = new ArrayList<>();
        for (Integer key : collection.keySet()) {
            Vehicle v = collection.get(key);
            if (vehicleType == null) {
                if (v.getType() == null && v.getOwner().equals(DBUserManager.getInstance().getCurrentUser().getUsername())) {
                    keysToRemove.add(key);
                }
            } else {
                if (vehicleType.equals(v.getType()) && v.getOwner().equals(DBUserManager.getInstance().getCurrentUser().getUsername())) {
                    keysToRemove.add(key);
                }
            }
        }

        this.removeVehiclesFromCollection(keysToRemove);

        String typeName = (vehicleType == null) ? "null" : vehicleType.name();
        System.out.println("Удалено " + keysToRemove.size() + " элемент(ов) с типом " + typeName + ".");
    }

    private void removeVehiclesFromCollection(List<Integer> keysToRemove) {
        int maxID = this.collection.size();
        int newMaxID = maxID - keysToRemove.size() + 1;

        for (Integer key : keysToRemove) {
            this.collection.remove(key);
        }

        this.dbCollectionManager.alterID(newMaxID);

        List<Integer> remainingKeys = new ArrayList<>(this.collection.keySet());
        Collections.sort(remainingKeys);
        Hashtable<Integer, Vehicle> newCollection = new Hashtable<>();
        int newKey = 1;
        for (Integer oldKey : remainingKeys) {
            Vehicle v = this.collection.get(oldKey);
            v.setId(newKey);
            newCollection.put(newKey, v);
            newKey++;
        }

        this.collection.clear();
        this.collection.putAll(newCollection);
    }

    /**
     * Возвращает дату инициализации коллекции.
     *
     * @return дата инициализации.
     */
    public Date getInitializationDate() {
        return initializationDate;
    }

    @Override
    public void clear() {
        this.dbCollectionManager.alterID(1);
        this.collection.clear();
    }

    @Override
    public void save() {
        //System.out.println("сохраняю коллекцию в БД");
        dbCollectionManager.save(this.collection);
    }
}
