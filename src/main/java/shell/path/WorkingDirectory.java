package shell.path;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Singleton class representing the current working directory of the shell.
 * Provides controlled access to modify and query the working directory,
 * ensuring consistency across shell operations.
 */
public class WorkingDirectory {

    private static WorkingDirectory instance;
    private Path dir;

    /**
     * Private constructor initializes the working directory to the current directory.
     */
    private WorkingDirectory() {
        this.dir = Path.of("").toAbsolutePath();
    }

    /**
     * Returns the singleton instance of {@code WorkingDirectory}, initializing it if necessary.
     *
     * @return the singleton {@code WorkingDirectory} instance
     */

    public static WorkingDirectory get() {
        if (instance == null) {
            instance = new WorkingDirectory();
        }
        return instance;
    }

    /**
     * Gets the current working directory as an absolute path string.
     *
     * @return the current working directory
     */
    public String getDir() {
        return dir.toString();
    }

    /**
     * Sets the current working directory using a {@code Path} object.
     *
     * @param dir the new working directory path
     * @throws IllegalArgumentException if the path is {@code null} or does not exist
     */
    public void setDir(Path dir) {
        if (dir == null || !Files.exists(dir)) {
            throw new IllegalArgumentException("Directory must exist");
        }
        this.dir = dir.toAbsolutePath();
    }
}
