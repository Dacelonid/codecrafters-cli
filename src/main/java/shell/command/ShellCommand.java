package shell.command;

import shell.io.OutputWriter;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Represents a shell command that can be executed with arguments.
 */
public interface ShellCommand {
    // Existing method, for simple execution without redirection (optional)
    void execute(String[] args);

    // New method with I/O streams, for piping support
    default void execute(String[] args, InputStream in, OutputStream out) throws Exception {
        // Default implementation: redirect OutputWriter to 'out' and execute
        // You may want to override in each built-in that needs to read from 'in'
        OutputWriter.setOut(new PrintStream(out));
        execute(args);
        OutputWriter.reset();
    }

    /**
     * Optional help or explanation for the command.
     */
    default void explain(String[] args) {
        OutputWriter.println(getname() + " is a shell builtin");
    }

    String getname();
}