package Utilities;

public class AbsolutePathResolver implements PathResolver{
    @Override
    public boolean supports(String input) {
        return FileUtilities.isAbsolutePath(input);
    }

    @Override
    public String resolve(String[] arguments, String currentWorkingDirectory) {
        return arguments[1];
    }
}
