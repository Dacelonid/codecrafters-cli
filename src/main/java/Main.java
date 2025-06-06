import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        printPrompt();
        while(true) {
            String userInput = getInput();
            String[] inputs = userInput.split(" ");
            checkForExit(inputs);
            System.out.println(userInput + ": command not found");
            printPrompt();
        }
    }

    private static void printPrompt() {
        System.out.print("$ ");
    }

    private static void checkForExit(String[] inputs) {
        if(inputs.length == 2){ //assume exit and exit code
            if(inputs[0].equals("exit")){
                if(isInteger(inputs[1]))
                    System.exit(Integer.parseInt(inputs[1]));
            }
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
