package ru.lab.util;

import ru.lab.model.Vehicle;
import java.util.Hashtable;

/**
 * Интерфейс для управления коллекцией объектов Vehicle.
 */
public interface ICollectionManager {
    /**
     * Возвращает коллекцию транспортных средств.
     *
     * @return Hashtable, где ключ – id транспортного средства, значение – объект Vehicle.
     */
    Hashtable<Integer, Vehicle> getCollection();

    /**
     * Добавляет транспортное средство в коллекцию.
     * Если поле id транспортного средства не установлено (&lt;= 0), генерируется новый уникальный id.
     *
     * @param vehicle объект Vehicle для добавления.
     */
    void addVehicle(Vehicle vehicle);
    // Дополнительные методы (update, remove и т.д.) можно добавить по необходимости.
}
