package shell.command;

import shell.io.OutputWriter;

import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class EchoCommand implements ShellCommand{
    @Override
    public void execute(String[] args) {
        OutputWriter.println(stream(args, 1, args.length).collect(Collectors.joining(" ")));
    }

    @Override
    public String getname() {
        return "echo";
    }
}
