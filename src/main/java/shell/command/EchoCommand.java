package shell.command;

import shell.io.OutputWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class EchoCommand implements ShellCommand{
    @Override
    public void execute(String[] args) {
        OutputWriter.println(stream(args, 1, args.length).collect(Collectors.joining(" ")));
    }

    @Override
    public void execute(String[] args, InputStream in, OutputStream out) throws IOException {
        // Create an OutputWriter instance wrapping the provided output stream
        OutputWriter writer = new OutputWriter(out);

        // Join the args starting at index 1 separated by spaces
        String output = stream(args, 1, args.length).collect(Collectors.joining(" "));

        // Use the instance OutputWriter to print the output line
        writer.printlnInstance(output);
    }

    @Override
    public String getname() {
        return "echo";
    }
}
