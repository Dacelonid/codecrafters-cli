package shell.command;

import shell.Main;
import shell.io.OutputWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Represents a shell command that can be executed with arguments.
 */
public interface ShellCommand {
    // Existing method, for simple execution without redirection (optional)
    default void execute(String[] args){
        PrintStream originalOut = System.out;
        InputStream originalIn = System.in;
        PrintStream originalErr = System.err;
        // Call execute with System.in and System.out for backward compatibility
        try {
            execute(args, System.in, System.out);
        } catch (Throwable e) {
            System.setOut(originalOut); // Restore it
            System.setIn(originalIn);
            System.setErr(originalErr);
            System.err.println(args[0] + ": error executing command");
        } finally {
            System.setOut(originalOut); // Restore it
            System.setIn(originalIn);
            System.setErr(originalErr);
        }
    }

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