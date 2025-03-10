package ru.lab.util;

/**
 * Исключение, выбрасываемое при прерывании выполнения команды пользователем.
 */
public class CommandInterruptedException extends RuntimeException {
    public CommandInterruptedException(String message) {
        super(message);
    }
}
