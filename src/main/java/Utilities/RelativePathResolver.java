package Utilities;

public class RelativePathResolver implements PathResolver{
    @Override
    public boolean supports(String input) {
        return true;
    }

    @Override
    public String resolve(String[] arguments, String currentWorkingDirectory) {
        return FileUtilities.resolveRelativeDirectory(currentWorkingDirectory, arguments);
    }
}
