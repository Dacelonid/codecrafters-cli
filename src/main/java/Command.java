import java.io.File;
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
        public void execute(String[] options) {
            System.out.println(java.util.Arrays.stream(options, 1, options.length).collect(Collectors.joining(" ")));
        }

    }, EXIT("exit") {
        @Override
        public void execute(String[] options) {
            if (Utils.isInteger(options[1])) {
                System.exit(Integer.parseInt(options[1]));
            } else {
                System.out.println("Exit called with a non numerical exit code");
            }
        }

    }, TYPE("type") {
        @Override
        public void execute(String[] options) {
            valeOf(options[1]).explain(options);
        }
    }, UNKNOWN("unknown") {
        @Override
        public void execute(String[] options) {
            System.out.println(options[0] + ": command not found");
        }

        @Override
        public void explain(String[] options) {
            String command = options[1];
            Optional<Path> possiblePath = Stream.of(getenv("PATH").split(quote(File.pathSeparator)))
                    .map(Paths::get)
                    .filter(path -> exists(path.resolve(command))).findFirst();
            if (possiblePath.isPresent()) {
                System.out.println(possiblePath.get() + File.separator + command);
            } else {
                System.out.println(command + ": not found");
            }
        }
    };

    private final String name;

    Command(String name) {
        this.name = name;
    }

    public void explain(String[] options) {
        System.out.println(name + " is a shell builtin");
    }

    public static Command valeOf(String command) {
        return switch (command) {
            case "type" -> TYPE;
            case "echo" -> ECHO;
            case "exit" -> EXIT;
            default -> UNKNOWN;
        };
    }

    public abstract void execute(String[] options);
}
