package shell;

public class ExitHandler {
    private static boolean shouldExit = false;
    private static int exitCode = 0;

    public static boolean shouldExit() {
        return shouldExit;
    }

    public static int getExitCode() {
        return exitCode;
    }

    public static void triggerExit(int code) {
        shouldExit = true;
        exitCode = code;
    }
}
