package ru.lab.model;

import java.util.Date;

/**
 * Класс, описывающий транспортное средство.
 * <p>
 * Объект изменяемый, за исключением поля creationDate, которое генерируется автоматически.
 * Поля id и creationDate генерируются автоматически при создании объекта.
 */
public class Vehicle {
    private int id;                   // > 0, уникальный (назначается извне)
    private String name;              // не null, не пустая строка
    private Coordinates coordinates;  // не null
    private Date creationDate;  // генерируется автоматически, не изменяется
    private float enginePower;        // > 0
    private VehicleType type;         // может быть null
    private FuelType fuelType;        // может быть null
    private String owner;

    /**
     * Конструктор, устанавливающий начальные значения.
     * Если переданный id &lt;= 0, считается, что id не задан и будет сгенерирован позже.
     * creationDate генерируется автоматически.
     *
     * @param id          уникальный идентификатор (> 0), если &lt;= 0, то считается не заданным.
     * @param name        название транспортного средства (не null, не пустое).
     * @param coordinates координаты транспортного средства (не null).
     * @param enginePower мощность двигателя (должна быть > 0).
     * @param type        тип транспортного средства (может быть null).
     * @param fuelType    тип топлива (может быть null).
     * @throws IllegalArgumentException если нарушены ограничения на name или enginePower.
     */
    public Vehicle(int id, String name, Coordinates coordinates, float enginePower,
                   VehicleType type, FuelType fuelType) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.enginePower = enginePower;
        this.creationDate = new Date();
        this.type = type;
        this.fuelType = fuelType;
    }

    public Vehicle(int id, String name, Coordinates coordinates, float enginePower, Date creationDate,
                   VehicleType vehicleType, FuelType fuelType, String owner) {
        this(id, name, coordinates, enginePower, vehicleType, fuelType);
        this.creationDate = creationDate;
        this.owner = owner;
    }


    /**
     * Возвращает уникальный идентификатор.
     *
     * @return id транспортного средства.
     */
    public int getId() {
        return id;
    }

    /**
     * Устанавливает уникальный идентификатор.
     * Если переданное значение &lt;= 0, id считается не заданным (будет сгенерирован автоматически).
     *
     * @param id уникальный идентификатор (> 0).
     */
    public void setId(int id) {
        /*
        if (id <= 0) {
            this.id = 0;
        } else {
            this.id = id;
        }
         */
        this.id = id;
    }

    /**
     * Возвращает название транспортного средства.
     *
     * @return название транспортного средства.
     */
    public String getName() {
        return name;
    }

    /**
     * Устанавливает название транспортного средства.
     *
     * @param name название (не null, не пустая строка).
     * @throws IllegalArgumentException если name равен null или пустой строке.
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Поле name не может быть null или пустым.");
        }
        this.name = name;
    }

    /**
     * Возвращает координаты транспортного средства.
     *
     * @return объект Coordinates.
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Устанавливает координаты транспортного средства.
     *
     * @param coordinates координаты (не null).
     * @throws IllegalArgumentException если coordinates равен null.
     */
    public void setCoordinates(Coordinates coordinates) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Поле coordinates не может быть null.");
        }
        this.coordinates = coordinates;
    }

    /**
     * Возвращает дату создания транспортного средства.
     *
     * @return объект Date, представляющий дату создания.
     */
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Возвращает мощность двигателя.
     *
     * @return мощность двигателя.
     */
    public float getEnginePower() {
        return enginePower;
    }

    /**
     * Устанавливает мощность двигателя.
     *
     * @param enginePower значение мощности (должно быть > 0).
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
     *
     * @return тип транспортного средства (VehicleType) или null.
     */
    public VehicleType getType() {
        return type;
    }

    /**
     * Устанавливает тип транспортного средства.
     *
     * @param type тип транспортного средства (может быть null).
     */
    public void setType(VehicleType type) {
        this.type = type;
    }

    /**
     * Возвращает тип топлива.
     *
     * @return тип топлива (FuelType) или null.
     */
    public FuelType getFuelType() {
        return fuelType;
    }

    /**
     * Устанавливает тип топлива.
     *
     * @param fuelType тип топлива (может быть null).
     */
    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public String getOwner() {
        return this.owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Возвращает строковое представление транспортного средства.
     *
     * @return строка с описанием транспортного средства.
     */
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
                ", owner=" + owner +
                "}";
    }
}
