package Utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WorkingDirectory {

    private static WorkingDirectory instance;
    private Path dir;

    private WorkingDirectory() {
        this.dir = Path.of("").toAbsolutePath();
    }

    public static WorkingDirectory get() {
        if (instance == null) {
            instance = new WorkingDirectory();
        }
        return instance;
    }

    public String getDir() {
        return dir.toString();
    }

    public void setDir(String dir) {
        if (dir == null || dir.isBlank()) {
            throw new IllegalArgumentException("Directory cannot be null or empty");
        }
        this.dir = Paths.get(dir).toAbsolutePath();
    }

    // ✅ New: Set directory directly as a Path (good for temp dirs)
    public void setDir(Path dir) {
        if (dir == null || !Files.exists(dir)) {
            throw new IllegalArgumentException("Directory must exist");
        }
        this.dir = dir.toAbsolutePath();
    }

    // ✅ Reset for testing
    public static void reset() {
        instance = null;
    }
}
