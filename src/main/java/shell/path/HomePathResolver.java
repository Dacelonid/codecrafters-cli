package shell.path;

/**
 * Resolves paths that use the tilde (~) notation to refer to the user's home directory.
 */
public class HomePathResolver implements PathResolver {
    @Override
    public boolean supports(String input) {
        return input.startsWith("~");
    }

    @Override
    public String resolve(String path, String currentWorkingDirectory) {
        return FileUtilities.resolveHomeDirectory(path);
    }
}
