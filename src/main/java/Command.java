import Utilities.FileUtilities;
import Utilities.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.System.getenv;
import static java.nio.file.Files.exists;
import static java.util.regex.Pattern.quote;


public enum Command {
    ECHO("echo") {
        @Override
        public void execute(String[] arguments) {
            System.out.println(java.util.Arrays.stream(arguments, 1, arguments.length).collect(Collectors.joining(" ")));
        }

    }, EXIT("exit") {
        @Override
        public void execute(String[] arguments) {
            if (Utils.isInteger(arguments[1])) {
                System.exit(Integer.parseInt(arguments[1]));
            } else {
                System.out.println("Exit called with a non numerical exit code");
            }
        }

    }, TYPE("type") {
        @Override
        public void execute(String[] arguments) {
            valeOf(arguments[1]).explain(arguments);
        }
    }, UNKNOWN("unknown") {
        @Override
        public void execute(String[] arguments) {
            if (findCommandInPath(arguments[0]).isPresent()) {
                executeExternalCommand(arguments);
            } else {
                System.out.println(arguments[0] + ": command not found");
            }
        }

        private static void executeExternalCommand(String[] options) {
            ProcessBuilder builder = new ProcessBuilder(options);
            // Redirect output to inherit from the parent process (your console)
            builder.inheritIO();
            try {
                builder.start().waitFor();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException("Could not execute External Command", e);
            }
        }

        @Override
        public void explain(String[] arguments) {
            String commandToExplain = arguments[1];
            Optional<Path> possiblePath = findCommandInPath(commandToExplain);
            if (possiblePath.isPresent()) {
                System.out.println(possiblePath.get() + File.separator + commandToExplain);
            } else {
                System.out.println(commandToExplain + ": not found");
            }
        }

        private static Optional<Path> findCommandInPath(String command) {
            return Stream.of(getenv("PATH").split(quote(File.pathSeparator)))
                    .map(Paths::get)
                    .filter(path -> exists(path.resolve(command))).findFirst();
        }
    }, PWD("pwd") {
        @Override
        public void execute(String[] arguments) {
            System.out.println(wd.getDir());
        }
    }, CD("cd") {
        @Override
        public void execute(String[] arguments) {
            String target;
            if (FileUtilities.isAbsolutePath(arguments[1])) {
                target = arguments[1];
            }
            else if(arguments[1].startsWith("~")){
                target = FileUtilities.resolveHomeDirectory(arguments[1]);
            }
            else{
                target = FileUtilities.resolveRelativeDirectory(wd.getDir(), arguments);
            }
            changeDir(target);
        }

        private void changeDir(String directory) {
            if (exists(Path.of(directory).toAbsolutePath()))
                wd.setDir(directory);
            else {
                System.out.println("cd: " + directory + ": No such file or directory");
            }
        }


    };

    private final String name;
    private static final WorkingDirectory wd = new WorkingDirectory();

    Command(String name) {
        this.name = name;
    }

    public void explain(String[] arguments) {
        System.out.println(name + " is a shell builtin");
    }

    public static Command valeOf(String command) {
        return switch (command) {
            case "type" -> TYPE;
            case "echo" -> ECHO;
            case "exit" -> EXIT;
            case "pwd" -> PWD;
            case "cd" -> CD;
            default -> UNKNOWN;
        };
    }

    public abstract void execute(String[] arguments);
}
