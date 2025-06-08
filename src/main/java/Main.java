import Utilities.OutputClass;
import Utilities.OutputWriter;

import java.util.Scanner;
import static Utilities.Utils.tokenize;

public class Main {
    private static final boolean testMode = System.getenv("CODECRAFTERS_TEST") != null;

    public static void main(String[] args) {
        //noinspection InfiniteLoopStatement
        while (true) {
            printPrompt();
            String[] inputs = tokenize(getInput());
            Command.fromString(inputs[0]).execute(inputs);
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
            System.exit(0); // Exit cleanly on EOF
        }
        return scanner.nextLine();
    }
}

