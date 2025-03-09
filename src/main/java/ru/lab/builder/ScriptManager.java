package ru.lab.builder;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Класс, управляющий очередью строк для исполнения скриптов.
 * Если очередь не пуста, ввод берется из неё, а не из стандартного ввода.
 */
public class ScriptManager {
    private final Queue<String> scriptLines = new LinkedList<>();

    /**
     * Добавляет список строк скрипта в очередь.
     *
     * @param lines список строк, прочитанных из файла.
     */
    public void addScriptLines(List<String> lines) {
        scriptLines.addAll(lines);
    }

    /**
     * Возвращает следующую строку скрипта, если она есть, иначе null.
     *
     * @return следующая строка или null.
     */
    public String pollLine() {
        return scriptLines.poll();
    }

    /**
     * Проверяет, пуста ли очередь строк скрипта.
     *
     * @return true, если очередь пуста.
     */
    public boolean isEmpty() {
        return scriptLines.isEmpty();
    }

    /**
     * Очищает очередь строк.
     */
    public void clear() {
        scriptLines.clear();
    }
}
