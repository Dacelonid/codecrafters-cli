package shell.command;

import shell.io.OutputWriter;
import shell.path.WorkingDirectory;

public class PwdCommand implements ShellCommand {
    @Override
    public void execute(String[] args) {
        OutputWriter.println(WorkingDirectory.get().getDir());
    }

    @Override
    public String getname() {
        return "pwd";
    }
}
