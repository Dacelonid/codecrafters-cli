package Utilities;

public interface PathResolver {
    boolean supports(String input);
    String resolve(String[] arguments, String currentWorkingDirectory);
}
