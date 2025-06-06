import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        System.out.print("$ ");
        while(true) {
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            String[] inputs = input.split(" ");
            if(inputs.length == 2){ //assume exit and exit code
                if(inputs[0].equals("exit")){
                    if(isInteger(inputs[1]))
                        System.exit(Integer.parseInt(inputs[1]));
                }
            }
            System.out.println(input + ": command not found");
            System.out.print("$ ");
        }
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
