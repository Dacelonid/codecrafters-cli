package shell.io;

import shell.Main;

import java.io.OutputStream;
import java.io.PrintStream;

public class OutputWriter {
    private PrintStream out;
    private static PrintStream err = System.err;
    private static boolean append = false;

    public OutputWriter(OutputStream outputStream) {
        this.out = new PrintStream(outputStream);
    }

    // Keep static methods for backward compatibility or default usage
    private static PrintStream staticOut = System.out;

    public static void setAppendMode() {
        append = true;
    }

    public static boolean isAppend() {
        return append;
    }

    public static void println(String s) {
        if (append) {
            staticOut.append(s).append("\n");
        } else {
            staticOut.println(s);
        }
    }

    public static void print(String s) {
        if (append) {
            staticOut.append(s);
        } else {
            staticOut.print(s);
        }
    }

    public static void setOut(PrintStream customOut) {
        staticOut = customOut;
    }

    public static void reset() {
        staticOut = System.out;
        err = System.err;
        append = false;
    }

    public static void setErr(PrintStream customErr) {
        err = customErr;
    }

    public static void printlnError(String line) {
        if (append) {
            err.append(line).append("\n");
        } else {
            err.println(line);
        }
    }

    // Non-static versions for instances
    public void printlnInstance(String s) {
        if (append) {
            out.append(s).append("\n");
        } else {
            out.println(s);
        }
    }

    public void printInstance(String s) {
        if (append) {
            out.append(s);
        } else {
            out.print(s);
        }
    }
}
