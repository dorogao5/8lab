package ru.lab.util;

/**
 * Создает исключение CommandInterruptedException с указанным сообщением.
 *
 */
public class CommandInterruptedException extends RuntimeException {
    public CommandInterruptedException(String message) {
        super(message);
    }
}
