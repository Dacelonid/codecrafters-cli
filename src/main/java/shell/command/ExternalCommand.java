package shell.command;

import Utilities.Utils;
import shell.Main;
import shell.io.OutputWriter;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

import static java.lang.System.getenv;
import static java.nio.file.Files.exists;
import static java.util.regex.Pattern.quote;

public class ExternalCommand implements ShellCommand {

    @Override
    public void execute(String[] args, InputStream in, OutputStream out) throws IOException {
        if (findCommandInPath(args[0]).isEmpty()) {
            PrintStream ps = null;
            try {
                ps = new PrintStream(out, true);
                ps.println(args[0] + ": command not found");
            } finally {
                if (ps != null) {
                    ps.flush();
                }
            }
            return;
        }

        ProcessBuilder builder = new ProcessBuilder(args);
        builder.redirectError(ProcessBuilder.Redirect.PIPE);

        Process process;
        try {
            process = builder.start();
        } catch (IOException e) {
            try (PrintStream ps = new PrintStream(out, true)) {
                ps.println(args[0] + ": failed to start");
            }
            return;
        }

        // Forward input stream to process's stdin
        Thread inputThread = new Thread(() -> {
            try (OutputStream processIn = process.getOutputStream()) {
                in.transferTo(processIn);
            } catch (IOException ignored) {
            }
        });

        // Forward process stdout to out
        Thread outputThread = new Thread(() -> {
            try (InputStream processOut = process.getInputStream()) {
                processOut.transferTo(out);
            } catch (IOException ignored) {
            }
        });

        // Forward process stderr to System.err (or consider redirecting it)
        Thread errorThread = new Thread(() -> {
            try (InputStream processErr = process.getErrorStream()) {
                processErr.transferTo(System.err);
            } catch (IOException ignored) {
            }
        });

        inputThread.start();
        outputThread.start();
        errorThread.start();
        try {
            int exitCode = process.waitFor();

            inputThread.join(5);
            outputThread.join(5);
            errorThread.join(5);
            // You might want to handle non-zero exit codes here or upstream

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }

    @Override
    public void explain(String[] args) {
        String commandToExplain = args[1];
        Optional<Path> possiblePath = findCommandInPath(commandToExplain);
        if (possiblePath.isPresent()) {
            OutputWriter.println(possiblePath.get() + File.separator + commandToExplain);
        } else {
            OutputWriter.println(commandToExplain + ": not found");
        }
    }

    private Optional<Path> findCommandInPath(String command) {
        String suffix = System.getProperty("os.name").toLowerCase().contains("win") ? ".exe" : "";
        return Stream.of(getenv("PATH").split(quote(File.pathSeparator)))
                .map(Paths::get)
                .filter(path -> exists(path.resolve(command + suffix))).findFirst();
    }

    @Override
    public String getname() {
        return "external";
    }
}
