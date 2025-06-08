import Utilities.OutputWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

import static Utilities.Utils.tokenize;

public class Main {
    private static final boolean testMode = System.getenv("CODECRAFTERS_TEST") != null;

    public static void main(String[] args) {
        while (true) {
            printPrompt();
            String input = getInput();

            // Check for redirection (handles ">", "1>" only)
            String[] parts = input.split("1?>"); // match '>' or '1>'
            PrintStream redirectStream = null;

            if (parts.length == 2) {
                input = parts[0].trim();
                String targetFile = parts[1].trim();

                try {
                    redirectStream = new PrintStream(new FileOutputStream(targetFile));
                    OutputWriter.setOut(redirectStream); // ✅ Only redirect stdout
                    // Do NOT redirect System.err
                } catch (IOException e) {
                    System.err.println("Redirection failed: " + e.getMessage());
                    continue;
                }
            }

            String[] inputs = tokenize(input);
            if (inputs.length == 0) continue;
            Command.fromString(inputs[0]).execute(inputs);

            if (redirectStream != null) {
                redirectStream.close();
                OutputWriter.setOut(System.out); // Reset stdout
                // No need to reset stderr
            }
        }
    }

    private static void printPrompt() {
        if (!testMode) {
            OutputWriter.print("$ ");
        }
    }

    private static String getInput() {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) {
            System.exit(0);
        }
        return scanner.nextLine();
    }
}
