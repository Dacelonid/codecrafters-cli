import java.util.stream.Collectors;

public enum Command {
    ECHO("echo") {
        @Override
        public void execute(String[] options) {
            System.out.println(java.util.Arrays.stream(options, 1, options.length).collect(Collectors.joining(" ")));
        }

    }, EXIT("exit") {
        @Override
        public void execute(String[] options) {
            if (isInteger(options[1])) {
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
            System.out.println(options[1] + ": not found");
        }
    };

    private final String name;

    Command(String name) {
        this.name = name;
    }

    public static Command valeOf(String command) {
        return switch (command) {
            case "type" -> TYPE;
            case "echo" -> ECHO;
            case "exit" -> EXIT;
            default -> UNKNOWN;
        };
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public abstract void execute(String[] options);

    public void explain(String[] options) {
        System.out.println(name + " is a shell builtin");
    }
}
