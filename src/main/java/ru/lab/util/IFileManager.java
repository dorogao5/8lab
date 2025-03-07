package ru.lab.util;

import ru.lab.model.Vehicle;
import java.util.Hashtable;

/**
 * Интерфейс для работы с файлом коллекции транспортных средств в формате CSV.
 */
public interface IFileManager {
    Hashtable<Integer, Vehicle> load(String fileName) throws Exception;
    void save(String fileName, Hashtable<Integer, Vehicle> collection) throws Exception;
}
