package shell.io;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class RedirectHandler {
    private static PrintStream redirectStream;

    public static String applyRedirection(String input) throws FileNotFoundException {
        String[] parts = input.split("1?>");
        redirectStream = null;

        if (parts.length == 2) {
            String targetFile = parts[1].trim();
            redirectStream = new PrintStream(new FileOutputStream(targetFile));
            OutputWriter.setOut(redirectStream);
            return parts[0].trim();
        }
        return input;
    }

    public static void cleanup() {
        if (redirectStream != null) {
            redirectStream.close();
            OutputWriter.setOut(System.out);
        }
    }
}