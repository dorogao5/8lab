package ru.lab.functions.commands;

import ru.lab.functions.Command;
import ru.lab.functions.Invoker;

import java.util.LinkedList;

/**
 * Команда для вывода последних 8 выполненных команд (без их аргументов).
 */
public class History implements Command {
    private final Invoker invoker;

    /**
     * Конструктор команды History.
     *
     * @param invoker объект Invoker, хранящий историю команд.
     */
    public History(Invoker invoker) {
        this.invoker = invoker;
    }

    /**
     * Выполняет команду History, выводя последние 8 выполненных команд.
     *
     * @param args аргументы команды (не используются).
     */
    @Override
    public void execute(String[] args) {
        LinkedList<String> history = invoker.getHistory();
        if (history.isEmpty()) {
            System.out.println("История команд пуста.");
            return;
        }
        System.out.println("Последние выполненные команды:");
        for (String cmd : history) {
            System.out.println(cmd);
        }
    }

    /**
     * Возвращает краткое описание команды History.
     *
     * @return описание команды.
     */
    @Override
    public String getDescription() {
        return "history - вывести последние 8 команд (без их аргументов)";
    }
}
