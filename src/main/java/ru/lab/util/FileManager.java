package ru.lab.util;

import ru.lab.model.Vehicle;
import ru.lab.model.Coordinates;
import ru.lab.model.VehicleType;
import ru.lab.model.FuelType;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Hashtable;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

/**
 * Класс для загрузки и сохранения коллекции транспортных средств в CSV формате.
 * Чтение файла осуществляется через BufferedInputStream.
 */
public class FileManager implements IFileManager {

    /**
     * Загружает коллекцию из CSV файла.
     * Формат CSV:
     * id, name, coordinates.x, coordinates.y, creationDate (epochMillis),
     * enginePower, type, fuelType
     */
    @Override
    public Hashtable<Integer, Vehicle> load(String fileName) throws Exception {
        Hashtable<Integer, Vehicle> collection = new Hashtable<>();
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileName));
             InputStreamReader isr = new InputStreamReader(bis, StandardCharsets.UTF_8);
             CSVReader csvReader = new CSVReader(isr)) {

            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                if (nextLine.length < 8) {
                    continue; // пропускаем некорректные строки
                }
                try {
                    int id = Integer.parseInt(nextLine[0]);
                    String name = nextLine[1];
                    long coordX = Long.parseLong(nextLine[2]);
                    int coordY = Integer.parseInt(nextLine[3]);
                    long creationMillis = Long.parseLong(nextLine[4]);
                    Date creationDate = new Date(creationMillis);
                    float enginePower = Float.parseFloat(nextLine[5]);
                    VehicleType type = nextLine[6].isEmpty() ? null : VehicleType.valueOf(nextLine[6]);
                    FuelType fuelType = nextLine[7].isEmpty() ? null : FuelType.valueOf(nextLine[7]);

                    Coordinates coordinates = new Coordinates(coordX, coordY);
                    // Создаем Vehicle; creationDate генерируется автоматически в конструкторе,
                    // поэтому для восстановления исходной даты можно использовать рефлексию,
                    // либо добавить перегруженный конструктор.
                    // Здесь для простоты создадим Vehicle и затем переопределим creationDate через рефлексию.
                    Vehicle vehicle = new Vehicle(id, name, coordinates, enginePower, type, fuelType);

                    // Устанавливаем восстановленную дату создания (если необходимо, через рефлексию)
                    // Пример (без обработки исключений):
                    java.lang.reflect.Field field = Vehicle.class.getDeclaredField("creationDate");
                    field.setAccessible(true);
                    field.set(vehicle, creationDate);

                    collection.put(id, vehicle);
                } catch (Exception e) {
                    System.err.println("Ошибка обработки строки: " + String.join(",", nextLine));
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            throw new Exception("Ошибка загрузки файла: " + fileName, e);
        }
        return collection;
    }

    /**
     * Сохраняет коллекцию в CSV файл.
     */
    @Override
    public void save(String fileName, Hashtable<Integer, Vehicle> collection) throws Exception {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8));
             CSVWriter csvWriter = new CSVWriter(bw)) {

            for (Vehicle vehicle : collection.values()) {
                String[] record = new String[8];
                record[0] = String.valueOf(vehicle.getId());
                record[1] = vehicle.getName();
                record[2] = String.valueOf(vehicle.getCoordinates().getX());
                record[3] = String.valueOf(vehicle.getCoordinates().getY());
                record[4] = String.valueOf(vehicle.getCreationDate().getTime());
                record[5] = String.valueOf(vehicle.getEnginePower());
                record[6] = (vehicle.getType() == null) ? "" : vehicle.getType().name();
                record[7] = (vehicle.getFuelType() == null) ? "" : vehicle.getFuelType().name();
                csvWriter.writeNext(record);
            }
            csvWriter.flush();
        } catch (Exception e) {
            throw new Exception("Ошибка сохранения файла: " + fileName, e);
        }
    }
}
