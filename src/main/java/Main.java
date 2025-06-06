import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        //noinspection InfiniteLoopStatement
        while (true) {
            printPrompt();
            String[] inputs = getInput().split(" ");
            Command.valeOf(inputs[0]).execute(inputs);
        }
    }

    private static void printPrompt() {
        System.out.print("$ ");
    }

    private static String getInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }


}
