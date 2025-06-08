package Utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListFiles {
    public static List<String> list(String path) {
        List<String> filePaths = new ArrayList<>();
        File directory = new File(path);

        if (directory.exists() && directory.isDirectory()) {
            collectFiles(directory, filePaths);
        }

        return filePaths;
    }

    private static void collectFiles(File dir, List<String> filePaths) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    filePaths.add(file.getAbsolutePath());
                } else if (file.isDirectory()) {
                    collectFiles(file, filePaths);
                }
            }
        }
    }
}