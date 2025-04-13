package ru.lab;

import ru.lab.builder.ApplicationBuilder;
import ru.lab.builder.Console;

/**
 * Главный класс для запуска приложения.
 */
public class Main {
    /**
     * Точка входа в приложение.
     *
     * @param args аргументы командной строки, первый из которых должен содержать имя CSV файла.
     */
    public static void main(String[] args) {
        try {
            ApplicationBuilder builder = new ApplicationBuilder(args);
            Console console = builder.createConsole();
            console.start();
        } catch (Exception e) {
            System.err.println("Ошибка запуска приложения: " + e.getMessage());
        }
    }
}
