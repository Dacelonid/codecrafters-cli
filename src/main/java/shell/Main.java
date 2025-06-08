package shell;

import shell.command.Command;
import shell.io.OutputWriter;

import java.io.IOException;
import java.util.Scanner;

import static shell.io.RedirectHandler.applyRedirection;
import static shell.io.RedirectHandler.cleanup;
import static Utilities.Utils.tokenize;

/**
 * The {@code shell.Main} class serves as the entry point for a POSIX-compliant shell interpreter.
 * It reads user input in a REPL loop, handles basic output redirection (stdout), and
 * dispatches execution to supported shell commands or external commands.
 *
 * <p>The shell supports redirection using {@code >} or {@code 1>}, and properly resets
 * stdout after each command. Built-in commands are handled via the {@link Command} enum.</p>
 */
public class Main {
    /**
     * Indicates whether the shell is running in test mode (e.g. during Codecrafters evaluation).
     */
    private static final boolean testMode = System.getenv("CODECRAFTERS_TEST") != null;
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * The main REPL loop of the shell. Continuously prompts the user for input,
     * parses redirection, tokenizes the command, and delegates execution.
     *
     * @param args Unused program arguments.
     */
    public static void main(String[] args) {
        runShell();
    }

    private static void runShell() {
        while (true) {
            printPrompt();
            String rawInput = getInput();
            if (rawInput.isBlank()) continue;
            try {
                String commandInput = applyRedirection(rawInput);
                String[] tokens = tokenize(commandInput);

                Command.resolve(tokens[0]).execute(tokens); //finally execute the command
            } catch (IOException e) {
                System.err.println("Redirection failed: " + e.getMessage());
            } finally {
                cleanup();
            }
        }
    }

    /**
     * Prints the shell prompt unless running in test mode.
     */
    private static void printPrompt() {
        if (!testMode) {
            OutputWriter.print("$ ");
        }
    }

    /**
     * Reads a line of input from the user. If EOF is encountered, exits the shell.
     *
     * @return The user input as a string.
     */
    private static String getInput() {
        if (!scanner.hasNextLine()) {
            System.exit(0);
        }
        return scanner.nextLine();
    }
}
