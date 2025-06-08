package Utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class ExternalCommandExecutor {

    public void executeExternalCommand(String[] options) {
        String os = System.getProperty("os.name").toLowerCase();
        String commandLine = shellEscapeJoin(List.of(options));

        ProcessBuilder builder = os.contains("win")
                ? new ProcessBuilder("cmd.exe", "/c", commandLine)
                : new ProcessBuilder("/bin/sh", "-c", commandLine);

        try {
            Process process = builder.start();

            // Read stdout
            try (BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = stdout.readLine()) != null) {
                    OutputWriter.getOut().println(line);
                }
            }

            // Read stderr — this must go to terminal regardless of redirection
            try (BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = stderr.readLine()) != null) {
                    System.err.println(line); // ✅ Use actual stderr
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                // Don't print unless needed (already handled by stderr)
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Could not execute external command", e);
        }
    }

    private String shellEscape(String s) {
        if (s == null || s.isEmpty()) {
            return "''";
        }
        return "'" + s.replace("'", "'\\''") + "'";
    }

    private String shellEscapeJoin(List<String> args) {
        return args.stream().map(this::shellEscape).collect(Collectors.joining(" "));
    }
}
