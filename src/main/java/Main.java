import Utilities.OutputWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

import static Utilities.Utils.tokenize;

/**
 * The {@code Main} class serves as the entry point for a POSIX-compliant shell interpreter.
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
    static PrintStream redirectStream;

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
            String input = getInput();
            try {
                input = handleRedirectIfNecessary(input);
                String[] inputs = tokenize(input);

                if (emptyUserInput(inputs)) continue;

                Command.fromString(inputs[0]).execute(inputs); //finally execute the command
            } catch (IOException e) {
                System.err.println("Redirection failed: " + e.getMessage());
            } finally {
                if (redirectStream != null) {
                    redirectStream.close();
                    OutputWriter.setOut(System.out); // Reset stdout
                }
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
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) {
            System.exit(0);
        }
        return scanner.nextLine();
    }

    /**
     * Parses the input for output redirection using {@code >} or {@code 1>}.
     * If redirection is detected, sets the global redirect stream and updates
     * {@link OutputWriter}'s output.
     *
     * @param input The raw input line.
     * @return The input string without the redirection part.
     * @throws FileNotFoundException If the redirection target file cannot be opened.
     */
    private static String handleRedirectIfNecessary(String input) throws FileNotFoundException {
        String output = input;
        // Check for redirection (handles ">", "1>" only)
        String[] parts = input.split("1?>"); // match '>' or '1>'
        redirectStream = null;

        if (parts.length == 2) {
            output = parts[0].trim();
            String targetFile = parts[1].trim();

            redirectStream = new PrintStream(new FileOutputStream(targetFile));
            OutputWriter.setOut(redirectStream); // Only redirect stdout

        }
        return output;
    }

    /**
     * Checks if the parsed token list from the user input is empty.
     *
     * @param inputs Tokenized input.
     * @return {@code true} if no input was given, {@code false} otherwise.
     */
    private static boolean emptyUserInput(String[] inputs) {
        return inputs.length == 0;
    }
}
