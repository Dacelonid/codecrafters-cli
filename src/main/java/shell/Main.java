package shell;

import shell.command.Command;
import shell.command.CommandCache;
import shell.io.OutputWriter;

import java.io.IOException;
import java.util.List;

import static Utilities.Utils.tokenize;
import static shell.io.RedirectHandler.applyRedirection;
import static shell.io.RedirectHandler.cleanup;


/**
 * The Main class acts as the entry point for the POSIX-compliant shell.
 * It provides an interactive REPL interface with support for command execution,
 * tab completion, and output redirection.
 */
public class Main {
    private static final boolean testMode = System.getenv("CODECRAFTERS_TEST") != null;

    private static String lastTabPrefix = "";
    private static int tabPressCount = 0;

    public static void main(String[] args) throws IOException {
        try {
            if (!testMode) setRawMode();

            runShell();

        } finally {
            if (!testMode) restoreTerminal();
        }
        // Exit with the code specified by the shell's "exit" command
        System.exit(ExitHandler.getExitCode());
    }

    /**
     * Runs the main interactive shell loop. Supports:
     * - command execution
     * - tab completion
     * - backspace support
     * - output redirection
     */
    private static void runShell() throws IOException {
        List<String> commands = CommandCache.getAllCommands(); // includes built-ins and external
        StringBuilder buffer = new StringBuilder();

        if (!testMode) printPrompt();

        do {
            int ch = System.in.read();
            if (!handleInput(ch, buffer, commands)) return;
        } while (!ExitHandler.shouldExit());
    }

    private static boolean handleInput(int character, StringBuilder buffer, List<String> commands) {
        return switch (character) {
            case -1 -> false;
            case '\n', '\r' -> {
                handleEnterKey(buffer);
                yield true;
            }
            case '\t' -> {
                handleTab(buffer, commands);
                yield true;
            }
            case 127, 8 -> {
                handleDeleting(buffer);
                yield true;
            }
            default -> {
                handleNormalTyping(character, buffer);
                yield true;
            }
        };
    }

    private static void handleEnterKey(StringBuilder buffer) {
        OutputWriter.println("");
        String input = buffer.toString();
        buffer.setLength(0);
        // ✅ Reset tab tracking
        tabPressCount = 0;

        if (!input.isBlank()) {
            try {
                String commandInput = applyRedirection(input);
                String[] tokens = tokenize(commandInput);
                Command.resolve(tokens[0]).execute(tokens);
                if (ExitHandler.shouldExit()) {
                    return; // gracefully exit shell loop
                }
            } catch (IOException e) {
                OutputWriter.printlnError("Redirection failed: " + e.getMessage());
            } catch (Exception e) {
                OutputWriter.printlnError("Error: " + e.getMessage());
            } finally {
                cleanup();
            }
        }

        if (!testMode) printPrompt();
    }

    private static void handleTab(StringBuilder buffer, List<String> allCommands) {
        String partial = buffer.toString().trim();

        // Reset tab press count if input changed
        if (!partial.equals(lastTabPrefix)) {
            lastTabPrefix = partial;
            tabPressCount = 0;
        }

        // Find matching commands (builtins + PATH)
        List<String> matches = allCommands.stream()
                .filter(cmd -> cmd.startsWith(partial))
                .toList();

        if (matches.isEmpty()) {
            // No matches: ring bell
            OutputWriter.print("\u0007");
            return;
        }

        if (matches.size() == 1) {
            // One match: complete it
            String completion = matches.getFirst().substring(partial.length()) + " ";
            OutputWriter.print(completion);
            buffer.append(completion);

            // Reset tracking after completion
            lastTabPrefix = "";
            tabPressCount = 0;
        } else {
            // Find the longest common prefix among matches
            String firstMatch = matches.getFirst();
            int commonPrefixLength = partial.length();

            // Extend the common prefix as long as all matches share the same characters
            outer:
            while (commonPrefixLength < firstMatch.length()) {
                char currentChar = firstMatch.charAt(commonPrefixLength);
                for (int i = 1; i < matches.size(); i++) {
                    String match = matches.get(i);
                    if (commonPrefixLength >= match.length() ||
                            match.charAt(commonPrefixLength) != currentChar) {
                        break outer;
                    }
                }
                commonPrefixLength++;
            }

            // If we found a longer common prefix, complete to it
            if (commonPrefixLength > partial.length()) {
                String completion = firstMatch.substring(partial.length(), commonPrefixLength);
                OutputWriter.print(completion);
                buffer.append(completion);
                lastTabPrefix = buffer.toString().trim();
            } else {
                // No longer common prefix found, behave as before
                tabPressCount++;
                if (tabPressCount == 1) {
                    OutputWriter.print("\u0007");
                } else {
                    OutputWriter.println("");
                    String matchLine = String.join("  ", matches);
                    OutputWriter.println(matchLine);
                    printPrompt();
                    OutputWriter.print(buffer.toString());
                    tabPressCount = 0;
                }
            }
        }
    }


    private static void handleDeleting(StringBuilder buffer) {
        if (!buffer.isEmpty()) {
            buffer.setLength(buffer.length() - 1);
            OutputWriter.print("\b \b");
            // ✅ Reset tab tracking
            tabPressCount = 0;
        }
    }

    private static void handleNormalTyping(int ch, StringBuilder buffer) {
        if (ch >= 32 && ch <= 126) { // Printable ASCII range
            OutputWriter.print(String.valueOf((char) ch));
            buffer.append((char) ch);
            // ✅ Reset tab tracking
            tabPressCount = 0;
        }
    }

    private static void printPrompt() {
        OutputWriter.print("$ ");
    }

    /**
     * Enables raw mode on Unix-like terminals (disables echo, enables char-by-char input)
     */
    private static void setRawMode() throws IOException {
        new ProcessBuilder("sh", "-c", "stty -echo -icanon min 1 time 0").inheritIO().start();
    }

    /**
     * Restores terminal to default sane settings (Unix)
     */
    private static void restoreTerminal() throws IOException {
        new ProcessBuilder("sh", "-c", "stty sane").inheritIO().start();
    }
}