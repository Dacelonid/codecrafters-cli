package shell.command;

import Utilities.Utils;
import shell.ExitHandler;
import shell.io.OutputWriter;

public class ExitCommand implements ShellCommand {
    @Override
    public void execute(String[] args) {
        if (args.length > 1 && Utils.isInteger(args[1])) {
            ExitHandler.triggerExit(Integer.parseInt(args[1]));
        } else if (args.length == 1) {
            ExitHandler.triggerExit(0);
        } else {
            OutputWriter writer = new OutputWriter(System.err);
            writer.printlnInstance("Exit called with a non-numerical exit code");
        }
    }

    @Override
    public String getname() {
        return "exit";
    }
}
