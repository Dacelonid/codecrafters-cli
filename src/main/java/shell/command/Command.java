package shell.command;

import java.util.HashMap;
import java.util.Map;

/**
 * Dispatches command names to their respective implementations.
 */
public class Command {
    private static final Map<String, ShellCommand> COMMANDS = new HashMap<>();

    static {
        COMMANDS.put("echo", new EchoCommand());
        COMMANDS.put("exit", new ExitCommand());
        COMMANDS.put("pwd", new PwdCommand());
        COMMANDS.put("cd", new CdCommand());
        COMMANDS.put("type", new TypeCommand());
    }

    public static ShellCommand resolve(String name) {
        return COMMANDS.getOrDefault(name, new ExternalCommand());
    }
}
