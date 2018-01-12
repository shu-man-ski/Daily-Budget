package shm.dim.dailybudget.file_helper;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class FileHelper {

    public static int countLineInFile(Context context, String filename) {
        int result = 0;
        try {
            AssetManager assetManager = context.getAssets();
            InputStreamReader istream = new InputStreamReader(assetManager.open(filename));
            BufferedReader br = new BufferedReader(istream);

            while(br.readLine() != null) {
                result++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String[] readFile(Context context, String filename) {
        String[] result = new String[countLineInFile(context, filename)];
        try {
            AssetManager assetManager = context.getAssets();
            InputStreamReader istream = new InputStreamReader(assetManager.open(filename));
            Scanner scanner = new Scanner(istream);

            int i = 0;
            while(scanner.hasNextLine()){
                result[i] = scanner.nextLine();
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}