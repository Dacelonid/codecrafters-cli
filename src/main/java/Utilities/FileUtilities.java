package Utilities;

import java.nio.file.Path;

public class FileUtilities {
    public static boolean isAbsolutePath(String directory) {
        return directory.startsWith("/");
    }

    public static String resolveHomeDirectory(String directory) {
        if (directory.equals("~"))//we were only looking for home
            return getHomeDirectory();
        else if (directory.startsWith("~\\") || directory.startsWith("~/")) {//path relative to home
            String relativePath = directory.substring("~\\".length());
            return resolveAndNormalize(getHomeDirectory(), relativePath);
        } else {
            throw new IllegalArgumentException("Could not resolve path:" + directory);
        }
    }

    public static String resolveRelativeDirectory(String currentDir, String[] arguments) {
        return resolveAndNormalize(currentDir, arguments[1]);
    }

    private static String getHomeDirectory() {
        String home = System.getenv("HOME");
        return (home != null) ? home : System.getProperty("user.home");
    }

    private static String resolveAndNormalize(String base, String child) {
        return Path.of(base).resolve(child).toAbsolutePath().normalize().toString();
    }
}
