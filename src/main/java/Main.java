import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        printPrompt();

        //noinspection InfiniteLoopStatement
        while (true) {
            String userInput = getInput();
            String[] inputs = userInput.split(" ");
            checkForExit(inputs);
            if (inputs[0].equals("echo")) {
                System.out.println(handleEchoCommand(inputs));
            } else {
                System.out.println(handleCommandNotFound(userInput));
            }
            printPrompt();
        }
    }

    private static String handleCommandNotFound(String userInput) {
        return userInput + ": command not found";
    }

    private static String handleEchoCommand(String[] inputs) {
        return java.util.Arrays.stream(inputs, 1, inputs.length).collect(Collectors.joining(" "));
    }

    private static void printPrompt() {
        System.out.print("$ ");
    }

    private static void checkForExit(String[] inputs) {
        if (inputs[0].equals("exit")) {
            if (isInteger(inputs[1]))
                System.exit(Integer.parseInt(inputs[1]));
        }
    }

    private static String getInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
