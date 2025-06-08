package shell.command;

import shell.io.OutputWriter;

/**
 * Represents a shell command that can be executed with arguments.
 */
public interface ShellCommand {
    void execute(String[] args);

    /**
     * Optional help or explanation for the command.
     */
    default void explain(String[] args) {
        OutputWriter.println(getname() + " is a shell builtin");
    }

    String getname();
}