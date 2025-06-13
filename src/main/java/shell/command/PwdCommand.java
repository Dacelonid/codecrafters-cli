package shell.command;

import shell.io.OutputWriter;
import shell.path.WorkingDirectory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class PwdCommand implements ShellCommand {

    @Override
    public void execute(String[] args) {
        // For compatibility, write to System.out
        try {
            execute(args, System.in, System.out);
        } catch (IOException e) {
            OutputWriter.println("pwd: error writing output");
        }
    }

    @Override
    public void execute(String[] args, InputStream in, OutputStream out) throws IOException {
        try (PrintStream ps = new PrintStream(out, true)) {
            ps.println(WorkingDirectory.get().getDir());
        }
    }

    @Override
    public String getname() {
        return "pwd";
    }
}
