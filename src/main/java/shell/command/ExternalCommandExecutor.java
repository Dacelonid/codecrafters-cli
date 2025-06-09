package shell.command;

import shell.io.OutputWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The {@code ExternalCommandExecutor} is responsible for executing commands that are not
 * handled internally by the shell. It uses the native shell (/bin/sh or cmd.exe) to launch
 * external programs, supporting redirection and proper output handling.
 *
 * <p>Standard output is routed through {@link OutputWriter}, while standard error always prints to {@code System.err}.</p>
 *
 * <p>This class automatically escapes arguments to prevent shell injection or syntax errors.</p>
 */

public class ExternalCommandExecutor {

    /**
     * Executes an external shell command by spawning a child process.
     * Arguments are shell-escaped and passed to the underlying platform's shell.
     *
     * @param options the array of strings representing the command and its arguments
     */
    public void executeExternalCommand(String[] options) {
        String os = System.getProperty("os.name").toLowerCase();
        String commandLine = shellEscapeJoin(List.of(options));

        ProcessBuilder builder = createOsSpecificShell(os, commandLine);

        try {
            Process process = builder.start();
            forwardStandardOut(process);
            forwardStandardErr(process);
            process.waitFor();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Could not execute external command: " + commandLine, e);
        }
    }

    /**
     * Reads and forwards standard output from the given process to {@link OutputWriter}.
     *
     * @param process the process whose stdout is being forwarded
     * @throws IOException if an I/O error occurs while reading the stream
     */
    private static void forwardStandardOut(Process process) throws IOException {
        // Read stdout
        try (BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = stdout.readLine()) != null) {
                OutputWriter.getOut().println(line);
            }
        }
    }

    /**
     * Reads and forwards standard error from the given process to {@code System.err}.
     * This bypasses redirection to ensure errors are always visible to the user.
     *
     * @param process the process whose stderr is being forwarded
     * @throws IOException if an I/O error occurs while reading the stream
     */

    private static void forwardStandardErr(Process process) throws IOException {
        // Read stderr — this must go to terminal regardless of redirection
        try (BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            String line;
            while ((line = stderr.readLine()) != null) {
                OutputWriter.getErr().println(line);
            }
        }
    }

    private static ProcessBuilder createOsSpecificShell(String os, String commandLine) {
        return os.contains("win") ? new ProcessBuilder("cmd.exe", "/c", commandLine) : new ProcessBuilder("/bin/sh", "-c", commandLine);
    }

    /**
     * Escapes a single shell argument by wrapping it in single quotes
     * and escaping internal single quotes.
     *
     * @param s the string to escape
     * @return the shell-safe escaped version
     */
    private String shellEscape(String s) {
        if (s == null || s.isEmpty()) {
            return "''";
        }
        return "'" + s.replace("'", "'\\''") + "'";
    }

    /**
     * Escapes and joins a list of shell arguments into a single command-line string.
     *
     * @param args the list of arguments
     * @return the escaped, space-separated shell command
     */
    private String shellEscapeJoin(List<String> args) {
        return args.stream().map(this::shellEscape).collect(Collectors.joining(" "));
    }
}
