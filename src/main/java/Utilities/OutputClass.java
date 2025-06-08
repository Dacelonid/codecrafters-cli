package Utilities;

import java.io.PrintStream;

public class OutputClass {
    private static PrintStream out = System.out;

    public static void set(PrintStream newOut) {
        out = newOut;
    }

    public static PrintStream getOut() {
        return out;
    }

    // Optional: reset to default
    public static void reset() {
        out = System.out;
    }
}