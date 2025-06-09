package shell;

import shell.command.Command;
import shell.io.OutputWriter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static shell.io.RedirectHandler.applyRedirection;
import static shell.io.RedirectHandler.cleanup;
import static Utilities.Utils.tokenize;

public class Main {
    private static final boolean testMode = System.getenv("CODECRAFTERS_TEST") != null;

    public static void main(String[] args) throws IOException {
        try {
            if (!testMode) setRawMode();

            runShell();

        } finally {
            if (!testMode) restoreTerminal();
        }
    }

    private static void runShell() throws IOException {
        List<String> commands = Command.getCommandNames();
        StringBuilder buffer = new StringBuilder();

        if (!testMode) OutputWriter.print("$ ");

        while (true) {
            int ch = System.in.read();

            switch (ch) {
                case -1: // EOF
                    return;

                case '\n':
                case '\r': {
                    OutputWriter.println("");
                    String input = buffer.toString();
                    buffer.setLength(0);

                    if (!input.isBlank()) {
                        try {
                            String commandInput = applyRedirection(input);
                            String[] tokens = tokenize(commandInput);
                            Command.resolve(tokens[0]).execute(tokens);
                        } catch (IOException e) {
                            OutputWriter.printlnError("Redirection failed: " + e.getMessage());
                        } catch (Exception e) {
                            OutputWriter.printlnError("Error: " + e.getMessage());
                        } finally {
                            cleanup();
                        }
                    }

                    if (!testMode) OutputWriter.print("$ ");
                    break;
                }

                case '\t': {
                    String partial = buffer.toString().trim();
                    List<String> matches = commands.stream()
                            .filter(cmd -> cmd.startsWith(partial))
                            .collect(Collectors.toList());

                    if (matches.size() == 1) {
                        String completion = matches.get(0).substring(partial.length()) + " ";
                        OutputWriter.print(completion);
                        buffer.append(completion);
                    } else if (matches.size() > 1) {
                        OutputWriter.println("");
                        for (String match : matches) {
                            OutputWriter.println(match);
                        }
                        if (!testMode) OutputWriter.print("$ ");
                        OutputWriter.print(buffer.toString());
                    }
                    break;
                }

                case 127: // Backspace (DEL)
                case 8:   // Backspace (BS)
                    if (buffer.length() > 0) {
                        buffer.setLength(buffer.length() - 1);
                        OutputWriter.print("\b \b");
                    }
                    break;

                default:
                    if (ch >= 32 && ch <= 126) { // Printable ASCII range
                        OutputWriter.print(String.valueOf((char) ch));
                        buffer.append((char) ch);
                    }
                    break;
            }
        }
    }

    // Disable terminal echo and canonical mode
    private static void setRawMode() throws IOException {
        new ProcessBuilder("sh", "-c", "stty -echo -icanon min 1 time 0").inheritIO().start();
    }

    // Restore terminal to normal settings
    private static void restoreTerminal() throws IOException {
        new ProcessBuilder("sh", "-c", "stty sane").inheritIO().start();
    }
}
