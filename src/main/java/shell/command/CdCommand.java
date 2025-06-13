package shell.command;

import shell.io.OutputWriter;
import shell.path.CompositePathResolver;
import shell.path.WorkingDirectory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

import static java.nio.file.Files.exists;

public class CdCommand implements ShellCommand {
    @Override
    public void execute(String[] args) {
        String target = new CompositePathResolver().resolve(args[1], WorkingDirectory.get().getDir());
        changeDir(target);
    }

    @Override
    public void execute(String[] args, InputStream in, OutputStream out) throws IOException {
        // cd doesn't read from 'in' or write to 'out', but must accept these parameters
        String target = new CompositePathResolver().resolve(args[1], WorkingDirectory.get().getDir());

        // Use OutputWriter instance to write errors to 'out' (important for pipelines)
        OutputWriter writer = new OutputWriter(out);

        Path absolutePath = Path.of(target).toAbsolutePath();
        if (exists(absolutePath)) {
            WorkingDirectory.get().setDir(absolutePath);
        } else {
            writer.printlnInstance("cd: " + target + ": No such file or directory");
        }
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
