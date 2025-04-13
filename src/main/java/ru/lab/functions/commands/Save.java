package ru.lab.functions.commands;

import ru.lab.functions.Command;
import ru.lab.util.IFileManager;
import ru.lab.util.CollectionManager;
import ru.lab.model.Vehicle;
import java.io.File;
import java.util.Hashtable;

/**
 * Команда для сохранения коллекции в файл.
 */
public class Save implements Command {
    private final IFileManager fileManager;
    private final CollectionManager collectionManager;
    private final String fileName;

    /**
     * Конструктор команды Save.
     *
     * @param fileManager файловый менеджер для работы с CSV файлом.
     * @param collectionManager менеджер коллекции транспортных средств.
     * @param fileName имя файла для сохранения коллекции.
     */
    public Save(IFileManager fileManager, CollectionManager collectionManager, String fileName) {
        this.fileManager = fileManager;
        this.collectionManager = collectionManager;
        this.fileName = fileName;
    }

    /**
     * Выполняет команду Save, сохраняя коллекцию в файл.
     *
     * @param args аргументы команды (не используются).
     */
    @Override
    public void execute(String[] args) {
        try {
            // Проверка прав доступа и существования файла перед сохранением
            File file = new File(fileName);
            if (file.isDirectory()) {
                System.err.println("Ошибка: Указанный путь " + fileName + " является директорией.");
                return;
            }
            if (file.exists() && !file.canWrite()) {
                System.err.println("Ошибка: Нет прав для записи в файл " + fileName);
                return;
            }
            if (!file.exists()) {
                File parent = file.getAbsoluteFile().getParentFile();
                if (parent != null && (!parent.exists() || !parent.canWrite())) {
                    System.err.println("Ошибка: Невозможно создать файл " + fileName +
                            ". Нет прав доступа к директории " + parent.getAbsolutePath());
                    return;
                }
            }
            Hashtable<Integer, Vehicle> collection = collectionManager.getCollection();
            fileManager.save(fileName, collection);
            System.out.println("Коллекция успешно сохранена в файл: " + fileName);
        } catch (Exception e) {
            System.err.println("Ошибка сохранения коллекции: " + e.getMessage());
        }
    }

    /**
     * Возвращает краткое описание команды Save.
     *
     * @return описание команды.
     */
    @Override
    public String getDescription() {
        return "save - сохранить коллекцию в файл";
    }
}
