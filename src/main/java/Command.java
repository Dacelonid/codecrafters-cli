import Utilities.*;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.System.getenv;
import static java.nio.file.Files.exists;
import static java.util.Arrays.stream;
import static java.util.regex.Pattern.quote;

public enum Command {
    ECHO("echo") {
        @Override
        public void execute(String[] arguments) {
            OutputWriter.println(stream(arguments, 1, arguments.length).collect(Collectors.joining(" ")));
        }
    },

    EXIT("exit") {
        @Override
        public void execute(String[] arguments) {
            if (Utils.isInteger(arguments[1])) {
                System.exit(Integer.parseInt(arguments[1]));
            } else {
                OutputWriter.getOut().println("Exit called with a non numerical exit code");
            }
        }
    },

    TYPE("type") {
        @Override
        public void execute(String[] arguments) {
            fromString(arguments[1]).explain(arguments);
        }
    },

    EXTERNAL("external") {
        @Override
        public void execute(String[] arguments) {
            if (findCommandInPath(arguments[0]).isEmpty()) {
                OutputWriter.println(arguments[0] + ": command not found");
                return;
            }

            new ExternalCommandExecutor().executeExternalCommand(arguments);
        }


        @Override
        public void explain(String[] arguments) {
            String commandToExplain = arguments[1];
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
    },

    PWD("pwd") {
        @Override
        public void execute(String[] arguments) {
            OutputWriter.println(wd.getDir());
        }
    },

    CD("cd") {
        @Override
        public void execute(String[] arguments) {
            String target = new CompositePathResolver().resolve(arguments, wd.getDir());
            changeDir(target);
        }

        private void changeDir(String directory) {
            if (exists(Path.of(directory).toAbsolutePath()))
                wd.setDir(directory);
            else {
                OutputWriter.println("cd: " + directory + ": No such file or directory");
            }
        }
    };

    private final String name;
    private static final WorkingDirectory wd = WorkingDirectory.get();

    Command(String name) {
        this.name = name;
    }

    public void explain(String[] arguments) {
        OutputWriter.println(name + " is a shell builtin");
    }

    public static Command fromString(String command) {
        return switch (command) {
            case "type" -> TYPE;
            case "echo" -> ECHO;
            case "exit" -> EXIT;
            case "pwd" -> PWD;
            case "cd" -> CD;
            default -> EXTERNAL;
        };
    }

    public abstract void execute(String[] arguments);
}
