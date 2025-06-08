package Utilities;

import java.util.List;

public class CompositePathResolver {
    private final List<PathResolver> resolvers = List.of(new AbsolutePathResolver(), new HomePathResolver(), new RelativePathResolver());

    public String resolve(String[] args, String currentWorkingDir){
        return resolvers.stream().filter(r -> r.supports(args[1])).findFirst().orElseThrow().resolve(args, currentWorkingDir);
    }
}
