package ru.lab.functions.commands;

import ru.lab.functions.Command;
import ru.lab.functions.Invoker;
import ru.lab.builder.ScriptManager;
import ru.lab.util.DBUserManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Команда execute_script file_name - считывает и выполняет скрипт из указанного файла.
 * <p>
 * Если скрипт уже выполняется, предотвращается рекурсия.
 * Поддерживается формат файла .txt.
 */
public class ExecuteScript implements Command {
    private final Invoker invoker;
    private final ScriptManager scriptManager;
    private boolean isExecutingScript = false;

    /**
     * Конструктор команды ExecuteScript.
     *
     * @param invoker       объект Invoker, содержащий зарегистрированные команды.
     * @param scriptManager менеджер строк скрипта.
     */
    public ExecuteScript(Invoker invoker, ScriptManager scriptManager) {
        this.invoker = invoker;
        this.scriptManager = scriptManager;
    }

    /**
     * Выполняет команду execute_script, считывая и выполняя команды из указанного файла.
     * При этом, если скрипт уже выполняется, предотвращается рекурсия.
     *
     * @param args аргументы команды; args[0] – имя файла скрипта.
     */
    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            if(DBUserManager.getInstance().getCurrentUser() == null) {
                System.out.println("Нужно авторизоваться для выполнения этой операции");
                return;
            }
            System.out.println("Ошибка: необходимо указать имя файла скрипта.");
            return;
        }
        String fileName = args[0];
        File scriptFile = new File(fileName);
        if (!scriptFile.exists() || !scriptFile.isFile()) {
            System.out.println("Ошибка: файл " + fileName + " не найден. Возможно к нему нет доступа.");
            return;
        }
        if (!fileName.toLowerCase().endsWith(".txt")) {
            System.out.println("Ошибка: формат файла не поддерживается (только .txt).");
            return;
        }
        if (isExecutingScript) {
            System.out.println("Скрипт " + scriptFile.getName() + " уже выполняется. Пропуск для предотвращения рекурсии.");
            return;
        }
        isExecutingScript = true;

        // конструкция try-with-resources, которая автоматически закрывает ресурс по завершении работы блока try
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(scriptFile), StandardCharsets.UTF_8))) {
            String line = reader.readLine();
            if (line == null) {
                System.out.println("Файл пуст, нечего считывать.");
                return;
            }
            while (line != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    System.out.println("Выполняем команду: " + line);
                    executeCommand(line);
                }
                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден.");
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        } finally {
            isExecutingScript = false;
        }
    }

    /**
     * Разбивает строку команды на имя и аргументы и выполняет соответствующую команду.
     *
     * @param commandLine строка команды.
     */
    private void executeCommand(String commandLine) {
        String[] tokens = commandLine.split("\\s+");
        if (tokens.length == 0) return;
        String commandName = tokens[0];
        Command command = invoker.getCommands().get(commandName);
        if (command == null) {
            System.out.println("Неизвестная команда: " + commandName);
            return;
        }
        String[] cmdArgs = new String[tokens.length - 1];
        System.arraycopy(tokens, 1, cmdArgs, 0, cmdArgs.length);
        try {
            command.execute(cmdArgs);
        } catch (Exception e) {
            System.out.println("Ошибка при выполнении команды '" + commandLine + "': " + e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "execute_script file_name - считать и исполнить скрипт из указанного файла";
    }
}
