package shell.io;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class RedirectHandler {
    private static PrintStream redirectStream;

    public static String applyRedirection(String input) throws FileNotFoundException {
        if(input.contains("2>")){
            return redirectErr(input);
        }else if(input.contains("1>") || input.contains(">")){
            return redirectOut(input);
        }
        return input;

    }

    private static String redirectOut(String input) throws FileNotFoundException {
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

    private static String redirectErr(String input) throws FileNotFoundException {
        String[] parts = input.split("2>");
        redirectStream = null;
        if (parts.length == 2) {
            String targetFile = parts[1].trim();
            redirectStream = new PrintStream(new FileOutputStream(targetFile));
            OutputWriter.setErr(redirectStream);
            return parts[0].trim();
        }
        return input;
    }

    public static void cleanup() {
        if (redirectStream != null) {
            redirectStream.close();
            OutputWriter.setOut(System.out);
            OutputWriter.setErr(System.err);
        }
    }
}