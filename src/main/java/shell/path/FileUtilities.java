package shell.path;

import java.nio.file.Path;

/**
 * Utility class for resolving and normalizing file paths in a POSIX-compliant shell environment.
 * Provides methods for determining absolute paths, resolving paths relative to the user's home directory,
 * and handling relative path resolution.
 */
public class FileUtilities {
    /**
     * Checks if a given directory path is an absolute path.
     *
     * @param directory The path string to check.
     * @return {@code true} if the path starts with '/', indicating an absolute path.
     */

    public static boolean isAbsolutePath(String directory) {
        return directory.startsWith("/");
    }

    /**
     * Resolves a path that begins with '~' to the user's home directory.
     * Supports both {@code ~} and {@code ~/some/path} syntax.
     *
     * @param directory The input path string beginning with '~'.
     * @return The absolute path to the resolved home directory path.
     * @throws IllegalArgumentException If the path does not conform to '~' or '~/...' format.
     */

    public static String resolveHomeDirectory(String directory) {
        if (directory.equals("~"))//we were only looking for home
            return getHomeDirectory();
        if (directory.startsWith("~\\") || directory.startsWith("~/")) {//path relative to home
            String relativePath = directory.substring(2);
            return resolveAndNormalize(getHomeDirectory(), relativePath);
        }
        throw new IllegalArgumentException("Could not resolve path:" + directory);

    }

    /**
     * Resolves a relative path against a given current directory.
     *
     * @param currentDir The current working directory.
     * @param path       The relative path to resolve.
     * @return The absolute normalized path.
     */

    public static String resolveRelativeDirectory(String currentDir, String path) {
        return resolveAndNormalize(currentDir, path);
    }

    /**
     * Gets the user's home directory using environment variables or system properties.
     *
     * @return The user's home directory path as a string.
     */
    private static String getHomeDirectory() {
        String home = System.getenv("HOME");
        return (home != null) ? home : System.getProperty("user.home");
    }

    /**
     * Combines and normalizes two path components into a single absolute path.
     *
     * @param base  The base path.
     * @param child The child path to resolve relative to the base.
     * @return The normalized absolute path.
     */

    private static String resolveAndNormalize(String base, String child) {
        return Path.of(base).resolve(child).toAbsolutePath().normalize().toString();
    }
}
