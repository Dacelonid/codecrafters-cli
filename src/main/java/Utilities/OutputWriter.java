package Utilities;

import java.io.PrintStream;

public class OutputWriter {
    private static PrintStream out = System.out;

    public static void println(String s) {
        out.println(s);
    }

    public static void print(String s) {
        out.print(s);
    }

    public static void setOut(PrintStream customOut) {
        out = customOut;
    }

    public static void reset() {
        out = System.out;
    }

    public static PrintStream getOut() {
        return out;
    }
}
