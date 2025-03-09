package ru.lab.util;

import ru.lab.model.Vehicle;
import java.util.Hashtable;

/**
 * Интерфейс для работы с файлом коллекции транспортных средств в формате CSV.
 */
public interface IFileManager {
    /**
     * Загружает коллекцию транспортных средств из CSV файла.
     *
     * @param fileName имя CSV файла.
     * @return Hashtable, где ключ – id транспортного средства, значение – объект Vehicle.
     * @throws Exception если возникает ошибка при загрузке файла.
     */
    Hashtable<Integer, Vehicle> load(String fileName) throws Exception;

    /**
     * Сохраняет коллекцию транспортных средств в CSV файл.
     * Сохраняются только поля: name, coordinates.x, coordinates.y, enginePower, type, fuelType.
     *
     * @param fileName   имя CSV файла.
     * @param collection коллекция транспортных средств.
     * @throws Exception если возникает ошибка при сохранении файла.
     */
    void save(String fileName, Hashtable<Integer, Vehicle> collection) throws Exception;
}
