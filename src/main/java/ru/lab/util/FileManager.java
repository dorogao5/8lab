package ru.lab.util;

import ru.lab.model.Vehicle;
import ru.lab.model.Coordinates;
import ru.lab.model.VehicleType;
import ru.lab.model.FuelType;

import java.io.File;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

/**
 * Класс для загрузки и сохранения коллекции транспортных средств в формате CSV.
 * &amp;lt;p&amp;gt;
 * Формат CSV: name, coordinates.x, coordinates.y, enginePower, type, fuelType.
 * Поля id и creationDate генерируются автоматически при создании объекта.
 */
public class FileManager implements IFileManager {

    /**
     * Загружает коллекцию транспортных средств из CSV файла.
     * При создании каждого объекта Vehicle, поля id и creationDate генерируются автоматически.
     *
     * @param fileName имя CSV файла.
     * @return Hashtable, где ключ &ndash; временный ключ для объекта, значение &ndash; объект Vehicle.
     * @throws Exception если возникает ошибка при загрузке файла.
     */
    @Override
    public Hashtable<Integer, Vehicle> load(String fileName) throws Exception {
        File file = new File(fileName);
        if (!file.exists()) {
            throw new Exception("Файл " + fileName + " не найден.");
        }
        if (!file.canRead()) {
            throw new Exception("Нет прав для чтения файла " + fileName);
        }
        if (file.isDirectory()) {
            throw new Exception("Указанный путь " + fileName + " является директорией.");
        }
        Hashtable<Integer, Vehicle> collection = new Hashtable<>();
        List<String[]> lines = getStrings(fileName, file);
        Collections.reverse(lines);

        int counter = 0;
        for (String[] nextLine : lines) {
            try {
                String name = nextLine[0];
                long coordX = Long.parseLong(nextLine[1]);
                int coordY = Integer.parseInt(nextLine[2]);
                float enginePower = Float.parseFloat(nextLine[3]);
                VehicleType type = nextLine[4].isEmpty() ? null : VehicleType.valueOf(nextLine[4]);
                FuelType fuelType = nextLine[5].isEmpty() ? null : FuelType.valueOf(nextLine[5]);

                Coordinates coordinates = new Coordinates(coordX, coordY);
                // Создаем Vehicle с временным id = 0, creationDate генерируется автоматически
                Vehicle vehicle = new Vehicle(0, name, coordinates, enginePower, type, fuelType);
                collection.put(++counter, vehicle);
            } catch (Exception e) {
                System.err.println("Ошибка обработки строки: " + String.join(",", nextLine));
            }
        }
        return collection;
    }

    private static List<String[]> getStrings(String fileName, File file) throws Exception {
        List<String[]> lines = new ArrayList<>();
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
             InputStreamReader isr = new InputStreamReader(bis, StandardCharsets.UTF_8);
             CSVReader csvReader = new CSVReader(isr)) {

            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                if (nextLine.length < 6) {
                    continue; // Пропускаем строки с недостаточным количеством полей
                }
                lines.add(nextLine);
            }
        } catch (Exception e) {
            throw new Exception("Ошибка загрузки файла: " + fileName, e);
        }
        return lines;
    }

    /**
     * Сохраняет коллекцию транспортных средств в CSV файл.
     * При сохранении записываются только поля: name, coordinates.x, coordinates.y, enginePower, type, fuelType.
     *
     * @param fileName   имя CSV файла для сохранения.
     * @param collection коллекция транспортных средств.
     * @throws Exception если возникает ошибка при сохранении файла.
     */
    @Override
    public void save(String fileName, Hashtable<Integer, Vehicle> collection) throws Exception {
        File file = new File(fileName);
        if (file.isDirectory()) {
            throw new Exception("Указанный путь " + fileName + " является директорией.");
        }
        if (file.exists()) {
            if (!file.canWrite()) {
                throw new Exception("Нет прав для записи в файл " + fileName);
            }
        } else {
            File parent = file.getAbsoluteFile().getParentFile();
            if (parent != null && (!parent.exists() || !parent.canWrite())) {
                throw new Exception("Невозможно создать файл " + fileName +
                        ". Нет прав доступа к директории " + parent.getAbsolutePath());
            }
        }

        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8));
             CSVWriter csvWriter = new CSVWriter(bw,
                     CSVWriter.DEFAULT_SEPARATOR,
                     CSVWriter.NO_QUOTE_CHARACTER,
                     CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                     CSVWriter.DEFAULT_LINE_END)) {

            List<Integer> keys = new ArrayList<>(collection.keySet());
            Collections.sort(keys);
            for (Integer key : keys) {
                Vehicle vehicle = collection.get(key);
                String[] record = new String[6];
                record[0] = vehicle.getName();
                record[1] = String.valueOf(vehicle.getCoordinates().getX());
                record[2] = String.valueOf(vehicle.getCoordinates().getY());
                record[3] = String.valueOf(vehicle.getEnginePower());
                record[4] = (vehicle.getType() == null) ? "" : vehicle.getType().name();
                record[5] = (vehicle.getFuelType() == null) ? "" : vehicle.getFuelType().name();
                csvWriter.writeNext(record);
            }
            csvWriter.flush();
        } catch (Exception e) {
            throw new Exception("Ошибка сохранения файла: " + fileName, e);
        }
    }
}
