import java.nio.file.Path;
import java.nio.file.Paths;

class WorkingDirectory {

    private Path dir;

    WorkingDirectory(){
        dir = Path.of("").toAbsolutePath();
    }

    public String getDir() {
        return dir.toString();
    }

    public void setDir(String dir) {
        this.dir = Paths.get(dir);

    }
}
