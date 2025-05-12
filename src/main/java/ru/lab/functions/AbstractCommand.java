package ru.lab.functions;

import ru.lab.builder.Console;

/**
 * Абстрактный класс для реализации команд.
 */
public abstract class AbstractCommand implements Command {
    private final Console console; // Используем интерактивный ввод

    /**
     * Конструктор команды Update.
     *
     * @param console           объект Console для считывания данных.
     */
    public AbstractCommand(Console console) {
        this.console = console;
    }

    protected String promptString(String prompt, boolean nonEmpty) {
        String input;
        while (true) {
            input = console.readInteractiveLine(prompt);
            if (nonEmpty && (input == null || input.trim().isEmpty())) {
                System.out.println("Ошибка: строка не может быть пустой.");
            } else {
                break;
            }
        }
        return input;
    }

    protected long promptLong(String prompt, long min, long max) {
        while (true) {
            String input = console.readInteractiveLine(prompt);
            try {
                long value = Long.parseLong(input);
                if (value < min || value > max) {
                    System.out.println("Ошибка: значение должно быть в диапазоне [" + min + ", " + max + "].");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите корректное число.");
            }
        }
    }

    protected int promptInt(String prompt, int min, int max) {
        while (true) {
            String input = console.readInteractiveLine(prompt);
            try {
                int value = Integer.parseInt(input);
                if (value < min || value > max) {
                    System.out.println("Ошибка: значение должно быть в диапазоне [" + min + ", " + max + "].");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите целое число.");
            }
        }
    }

    protected float promptFloat(String prompt, float min, float max) {
        while (true) {
            String input = console.readInteractiveLine(prompt);
            try {
                float value = Float.parseFloat(input);
                if (value <= min || value > max) {
                    System.out.println("Ошибка: значение должно быть больше " + min + " и не превышать " + max + ".");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите число с плавающей запятой.");
            }
        }
    }

    protected <E extends Enum<E>> E promptEnum(String prompt, Class<E> enumClass) {
        while (true) {
            String input = console.readInteractiveLine(prompt);
            if (input == null || input.trim().isEmpty()) {
                return null;
            }
            try {
                return Enum.valueOf(enumClass, input.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.print("Доступные значения: ");
                for (E constant : enumClass.getEnumConstants()) {
                    System.out.print(constant.name() + " ");
                }
                System.out.println();
                System.out.println("Ошибка: введите одно из указанных значений.");
            }
        }
    }
}
