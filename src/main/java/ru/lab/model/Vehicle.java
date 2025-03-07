package ru.lab.model;

import java.util.Date;

/**
 * Класс, описывающий транспортное средство (Vehicle).
 * Объект класса является изменяемым, что позволяет обновлять его поля в процессе работы программы.
 */
public class Vehicle {
    private int id;                   // > 0, уникальный (назначается извне)
    private String name;              // не null, не пустая
    private Coordinates coordinates;  // не null
    private final Date creationDate;        // генерируется автоматически, не изменяется после создания
    private float enginePower;        // > 0
    private VehicleType type;         // может быть null
    private FuelType fuelType;        // может быть null

    /**
     * Конструктор, устанавливающий начальные значения.
     * Поле creationDate генерируется автоматически.
     *
     * @param id           Уникальный идентификатор (> 0), назначается извне.
     * @param name         Название транспортного средства (не null, не пустое).
     * @param coordinates  Координаты (не null).
     * @param enginePower  Мощность двигателя (должна быть > 0).
     * @param type         Тип транспортного средства (может быть null).
     * @param fuelType     Тип топлива (может быть null).
     * @throws IllegalArgumentException если переданы некорректные данные.
     */
    public Vehicle(int id, String name, Coordinates coordinates, float enginePower,
                   VehicleType type, FuelType fuelType) {
        setId(id);
        setName(name);
        setCoordinates(coordinates);
        setEnginePower(enginePower);
        // creationDate устанавливается автоматически и не изменяется
        this.creationDate = new Date();
        this.type = type;
        this.fuelType = fuelType;
    }

    // Геттеры и сеттеры

    /**
     * Возвращает уникальный идентификатор.
     */
    public int getId() {
        return id;
    }

    /**
     * Устанавливает уникальный идентификатор.
     * Поскольку id генерируется извне, изменять его после создания не предполагается.
     *
     * @param id уникальный идентификатор (> 0).
     * @throws IllegalArgumentException если id &lt;= 0.
     */
    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Поле id должно быть > 0, получено: " + id);
        }
        this.id = id;
    }

    /**
     * Возвращает название транспортного средства.
     */
    public String getName() {
        return name;
    }

    /**
     * Устанавливает название транспортного средства.
     *
     * @param name не может быть null или пустым.
     * @throws IllegalArgumentException если name равен null или пустой строке.
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Поле name не может быть null или пустым.");
        }
        this.name = name;
    }

    /**
     * Возвращает координаты.
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Устанавливает координаты.
     *
     * @param coordinates не может быть null.
     * @throws IllegalArgumentException если coordinates равен null.
     */
    public void setCoordinates(Coordinates coordinates) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Поле coordinates не может быть null.");
        }
        this.coordinates = coordinates;
    }

    /**
     * Возвращает дату создания.
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Возвращает мощность двигателя.
     */
    public float getEnginePower() {
        return enginePower;
    }

    /**
     * Устанавливает мощность двигателя.
     *
     * @param enginePower значение должно быть > 0.
     * @throws IllegalArgumentException если enginePower &lt;= 0.
     */
    public void setEnginePower(float enginePower) {
        if (enginePower <= 0) {
            throw new IllegalArgumentException("Поле enginePower должно быть > 0, получено: " + enginePower);
        }
        this.enginePower = enginePower;
    }

    /**
     * Возвращает тип транспортного средства.
     */
    public VehicleType getType() {
        return type;
    }

    /**
     * Устанавливает тип транспортного средства.
     *
     * @param type может быть null.
     */
    public void setType(VehicleType type) {
        this.type = type;
    }

    /**
     * Возвращает тип топлива.
     */
    public FuelType getFuelType() {
        return fuelType;
    }

    /**
     * Устанавливает тип топлива.
     *
     * @param fuelType может быть null.
     */
    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", enginePower=" + enginePower +
                ", type=" + type +
                ", fuelType=" + fuelType +
                '}';
    }
}
