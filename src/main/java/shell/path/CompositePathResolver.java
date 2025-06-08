package shell.path;

import java.util.List;

/**
 * Resolves a path by delegating to the first {@link PathResolver} that supports the given input.
 * Supports absolute paths, home-relative paths, and relative paths in that order.
 */
public class CompositePathResolver {

    private final List<PathResolver> resolvers = List.of(new AbsolutePathResolver(), new HomePathResolver(), new RelativePathResolver());

    /**
     * Resolves the path using the first matching {@link PathResolver}.
     *
     * @param path the path we wish to resove
     * @param currentWorkingDir The shell's current working directory.
     * @return The resolved absolute path.
     * @throws java.util.NoSuchElementException if no resolver supports the input.
     */
    public String resolve(String path, String currentWorkingDir) {
        return resolvers.stream().filter(r -> r.supports(path)).findFirst().orElseThrow().resolve(path, currentWorkingDir);
    }
}
