package shell.path;

/**
 * Strategy interface for resolving file system paths based on different path types.
 * Implementations determine if they support a given path and how to resolve it.
 */
public interface PathResolver {
    /**
     * Checks if the resolver supports the given input path.
     *
     * @param input The path string to check.
     * @return {@code true} if this resolver can handle the input, {@code false} otherwise.
     */
    boolean supports(String input);

    /**
     * Resolves the given path and current working directory.
     *
     * @param path  the target path.
     * @param currentWorkingDirectory The shell's current working directory.
     * @return The fully resolved absolute path.
     */
    String resolve(String path, String currentWorkingDirectory);
}
