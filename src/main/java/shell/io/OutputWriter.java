package shell.io;

import java.io.PrintStream;

public class OutputWriter {
    private static PrintStream out = System.out;
    private static PrintStream err = System.err;
    private static boolean append = false;

    public static void println(String s) {
        if(append){
            out.append(s);
        }else {
            out.println(s);
        }
    }

    public static void print(String s) {
        if(append){
            out.append(s);
        }else {
            out.print(s);
        }
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

    public static void printlnError(String line) {
        if(append){
            err.append(line);
        }else {
            err.println(line);
        }
    }
}
