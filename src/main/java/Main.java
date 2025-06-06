import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        System.out.print("$ ");
        while(true) {
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            System.out.println(input + ": command not found");
            System.out.print("$ ");
        }
    }
}
