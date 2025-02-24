package ru.lab.model;

/**
 * Класс, описывающий координаты.
 * <p>
 * Поля:
 * <ul>
 *   <li>x: значение типа long, максимальное значение 225;</li>
 *   <li>y: целое число (Integer), не может быть null, максимальное значение 493;</li>
 * </ul>
 */
public class Coordinates {
    private long x; // максимум 225
    private Integer y; // не может быть null, максимум 493

    /**
     * Конструктор координат.
     *
     * @param x координата x (максимум 225)
     * @param y координата y (не null, максимум 493)
     */

    public Coordinates(long x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public long getX() {
        return x;
    }
    public void setX(long x) {
        this.x = x;
    }
    public Integer getY() {
        return y;
    }
    public void setY(Integer y) {
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