package shell.command;

import static shell.command.Command.resolve;

public class TypeCommand implements ShellCommand {
    @Override
    public void execute(String[] args) {
        resolve(args[1]).explain(args);
    }

    @Override
    public String getname() {
        return "type";
    }
}
