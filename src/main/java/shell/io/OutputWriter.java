package shell.io;

import java.io.PrintStream;

public class OutputWriter {
    private static PrintStream out = System.out;
    private static PrintStream err = System.err;

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
        err = System.err;
    }

    public static PrintStream getOut() {
        return out;
    }

    public static PrintStream getErr() {
        return err;
    }

    public static void setErr(PrintStream customErr) {
        err = customErr;
    }
}
