package ru.lab.model;

/**
 * Класс, описывающий координаты.
 * <p>
 * Поля:
 * <ul>
 *   <li>x: значение типа long, не должно превышать 225;</li>
 *   <li>y: целое число (Integer), не может быть null и не должно превышать 493;</li>
 * </ul>
 */
public class Coordinates {
    private long x;       // максимум 225
    private Integer y;    // не может быть null, максимум 493

    /**
     * Конструктор координат.
     *
     * @param x координата x (максимум 225)
     * @param y координата y (не null, максимум 493)
     * @throws IllegalArgumentException если x > 225 или y равен null или больше 493.
     */
    public Coordinates(long x, Integer y) {
        setX(x);
        setY(y);
    }

    public long getX() {
        return x;
    }

    public void setX(long x) {
        if (x > 225) {
            throw new IllegalArgumentException("Значение x должно быть не больше 225, получено: " + x);
        }
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        if (y == null) {
            throw new IllegalArgumentException("Значение y не может быть null.");
        }
        if (y > 493) {
            throw new IllegalArgumentException("Значение y должно быть не больше 493, получено: " + y);
        }
        this.y = y;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
