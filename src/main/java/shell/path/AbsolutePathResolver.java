package shell.path;

/**
 * Resolves paths that are already in absolute form.
 */
public class AbsolutePathResolver implements PathResolver {
    @Override
    public boolean supports(String input) {
        return FileUtilities.isAbsolutePath(input);
    }

    @Override
    public String resolve(String path, String currentWorkingDirectory) {
        return path;
    }
}
