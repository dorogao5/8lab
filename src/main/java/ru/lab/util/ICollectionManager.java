package ru.lab.util;

import ru.lab.model.Vehicle;
import java.util.Hashtable;

/**
 * Интерфейс для управления коллекцией объектов Vehicle.
 */
public interface ICollectionManager {
    Hashtable<Integer, Vehicle> getCollection();
    void addVehicle(Vehicle vehicle);
    // Можно добавить методы update, remove и т.д.
}
