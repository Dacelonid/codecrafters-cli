package shell.command;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class CommandCache {

    private static final Set<String> allCommands = new TreeSet<>(); // sorted + unique

    static {
        try {
            allCommands.addAll(Command.getCommandNames()); // Built-ins
            allCommands.addAll(findExternalExecutables()); // External commands
        } catch (IOException e) {
            // Optionally log or print error
        }
    }

    public static List<String> getAllCommands() {
        return List.copyOf(allCommands);
    }

    private static List<String> findExternalExecutables() throws IOException {
        var pathEnv = System.getenv("PATH");
        if (pathEnv == null || pathEnv.isBlank()) return List.of();

        var isWindows = System.getProperty("os.name").toLowerCase().contains("win");
        var pathSeparator = isWindows ? ";" : ":";
        var extensions = List.of(".exe", ".bat", ".cmd");

        return Arrays.stream(pathEnv.split(Pattern.quote(pathSeparator)))
//                .parallel()
                .map(Paths::get)
                .filter(Files::isDirectory)
                .flatMap(dir -> {
                    try (var stream = Files.list(dir)) {
                        // Collect the stream results to a list while the stream is still open
                        return stream.toList().stream();
                    } catch (IOException e) {
                        return Stream.empty();
                    }
                })
                .filter(Files::isRegularFile)
                .filter(p -> {
                    var name = p.getFileName().toString();
                    return isWindows
                            ? extensions.stream().anyMatch(name::endsWith)
                            : Files.isExecutable(p);
                })
                .map(p -> {
                    var name = p.getFileName().toString();
                    return isWindows ? stripWindowsExtension(name) : name;
                })
                .collect(Collectors.toSet()).stream().toList();
    }

    private static String stripWindowsExtension(String name) {
        return name.replaceFirst("\\.(exe|bat|cmd)$", "");
    }
}