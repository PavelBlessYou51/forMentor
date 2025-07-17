package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Класс содержит вспомогательные методы для работы с файлами
 */
public final class FileUtils {

    /**
     * Метод проверяет наличие файл и создает его при отсутствии
     */
    public static void fileCreator(String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * Метод возвращает абсолютный путь к файлу по относительному пути
     */
    public static String getAbsolutePathToFile(String file) {
        String absolutePath = Paths.get(file).toAbsolutePath().toString();
        return absolutePath;
    }

    /**
     * Метод проверяет список файлов в папке
     */
    public static File[] getListOfFiles(String directoryPath) {
        File folder = new File(directoryPath);
        File[] listOfFiles = folder.listFiles();
        return listOfFiles;
    }

    /**
     * Метод удаляет все файлы из указанной директории
     */
    public static void fileDeleter(String path) {
        File[] listOfFile = getListOfFiles(getAbsolutePathToFile(path));
        for (File file : listOfFile) {
            file.delete();
        }
    }

    /**
     * Метод записывает номер заявки в указанный файл
     */
    public static void applicationNumbersWriter(String path, String appNumber) {
        String absPath = getAbsolutePathToFile(path);
        FileUtils.fileCreator(absPath);
        try {
            FileWriter fw = new FileWriter(path, true);
            fw.write(appNumber + "\n");
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Метод номера заявок из указанного файла
     */
    public static List<String> applicationNumbersReader(String path) {
        try {
            List<String> numbers = Files.readAllLines(Paths.get(path).toAbsolutePath());
            return numbers;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
