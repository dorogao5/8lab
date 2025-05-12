package ru.lab.util;

import ru.lab.model.Vehicle;
import ru.lab.model.VehicleType;

import java.util.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
     * If the object does not have an ID set (id &lt;= 0), generates a unique sequential ID.
     * If the object has an ID > 0, it might overwrite an existing entry or create a gap,
     * it's generally expected that vehicles passed here might have ID 0 or an ID from DB sequence.
     * This implementation now ensures sequential IDs by calculating the next available one.
     *
     * @param vehicle объект Vehicle для добавления.
     * @return The actual sequential ID assigned to the vehicle in the collection.
     */
    @Override
    public int addVehicle(Vehicle vehicle) {
        // Calculate the next sequential ID based on current max ID in the collection
        int newId = this.collection.keySet().stream()
                .max(Integer::compareTo)
                .orElse(0) + 1;

        // Set the calculated ID on the vehicle object
        vehicle.setId(newId);

        // Add warnings if owner or creationDate are missing (as before, but kept)
        if (vehicle.getOwner() == null || vehicle.getOwner().isEmpty()) {
            System.err.println("Warning: Adding vehicle without an owner.");
        }
        if (vehicle.getCreationDate() == null) {
            // This shouldn't happen if Insert.java uses new Date(), but good to keep the check
            System.err.println("Warning: Adding vehicle without a creation date.");
        }
        this.collection.put(newId, vehicle); // Add using the new sequential ID as the key
        // Return the assigned sequential ID
        return newId;
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
        for (Integer key : keysToRemove) {
            this.collection.remove(key);
        }
        this.reIndexCollection();
    }

    /**
     * Re-indexes the collection to ensure sequential IDs starting from 1.
     * Assumes Vehicle.setId() method exists and is usable.
     */
    private void reIndexCollection() {
        if (this.collection.isEmpty()) {
            return;
        }

        List<Vehicle> sortedVehicles = new ArrayList<>(this.collection.values());
        sortedVehicles.sort(Comparator.comparingInt(Vehicle::getId));

        Hashtable<Integer, Vehicle> newCollection = new Hashtable<>();
        int newCurrentId = 1;
        for (Vehicle vehicle : sortedVehicles) {
            vehicle.setId(newCurrentId);
            newCollection.put(newCurrentId, vehicle);
            newCurrentId++;
        }
        this.collection = newCollection;
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
        this.collection.clear();
    }

    public void clearByUser(String username) {
        List<Integer> keysToRemove = new ArrayList<>();
        for (Map.Entry<Integer, Vehicle> entry : this.collection.entrySet()) {
            if (entry.getValue().getOwner() != null && entry.getValue().getOwner().equals(username)) {
                keysToRemove.add(entry.getKey());
            }
        }
        removeVehiclesFromCollection(keysToRemove);
    }

    @Override
    public void save() {
        dbCollectionManager.save(this.collection);
    }
}
