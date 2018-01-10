package shm.dim.dailybudget.file_manager;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class FileManager {

    // Проверка наличия файла
    public static boolean existFile(File file) {
        return file.exists();
    }

    // Создание файла
    public static void createFile(File file) {
        try {
            file.createNewFile();
        }
        catch (IOException e) {
            Log.d("LOG", e.getMessage());
        }
    }

    // Запись в файл строку
    public static void writeToFile(String str, File file) {
        try (FileWriter fw = new FileWriter(file, true)) {
            fw.write(str);
        }
        catch (IOException e) {
            Log.d("LOG", e.getMessage());
        }
    }

    // Прочитать файл
    public static String readFile(File file, int line) {
        String str = new String();
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            raf.skipBytes(8*line);
            str = raf.readLine();
        }
        catch (IOException e) {
            Log.d("LOG", e.getMessage());
        }
        return str;
    }
}