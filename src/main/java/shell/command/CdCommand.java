package shell.command;

import shell.io.OutputWriter;
import shell.path.CompositePathResolver;
import shell.path.WorkingDirectory;

import java.nio.file.Path;

import static java.nio.file.Files.exists;

public class CdCommand implements ShellCommand {
    @Override
    public void execute(String[] args) {
        String target = new CompositePathResolver().resolve(args[1], WorkingDirectory.get().getDir());
        changeDir(target);
    }

    @Override
    public String getname() {
        return "cd";
    }

    private void changeDir(String directory) {
        Path absolutePath = Path.of(directory).toAbsolutePath();
        if (exists(absolutePath))
            WorkingDirectory.get().setDir(absolutePath);
        else {
            OutputWriter.println("cd: " + directory + ": No such file or directory");
        }
    }
}
