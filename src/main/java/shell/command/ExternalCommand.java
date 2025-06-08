package shell.command;

import shell.io.OutputWriter;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

import static java.lang.System.getenv;
import static java.nio.file.Files.exists;
import static java.util.regex.Pattern.quote;

public class ExternalCommand implements ShellCommand {
    @Override
    public void execute(String[] args) {
        if (findCommandInPath(args[0]).isEmpty()) {
            OutputWriter.println(args[0] + ": command not found");
            return;
        }

        new ExternalCommandExecutor().executeExternalCommand(args);
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
