package shell.command;

import Utilities.Utils;
import shell.io.OutputWriter;

public class ExitCommand implements ShellCommand {
    @Override
    public void execute(String[] args) {
        if (Utils.isInteger(args[1])) {
            System.exit(Integer.parseInt(args[1]));
        } else {
            OutputWriter.println("Exit called with a non numerical exit code");
        }
    }

    @Override
    public String getname() {
        return "exit";
    }
}
