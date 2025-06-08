package Utilities;

public class HomePathResolver implements PathResolver{
    @Override
    public boolean supports(String input) {
        return input.startsWith("~");
    }

    @Override
    public String resolve(String[] arguments, String currentWorkingDirectory) {
        return FileUtilities.resolveHomeDirectory(arguments[1]);
    }
}
