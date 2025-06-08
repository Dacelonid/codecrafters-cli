package shell.path;

/**
 * Resolves paths that are relative to the current working directory.
 * This resolver is used as a fallback when no other resolvers match.
 */
public class RelativePathResolver implements PathResolver {
    @Override
    public boolean supports(String input) {
        return true;
    }

    @Override
    public String resolve(String path, String currentWorkingDirectory) {
        return FileUtilities.resolveRelativeDirectory(currentWorkingDirectory, path);
    }
}
