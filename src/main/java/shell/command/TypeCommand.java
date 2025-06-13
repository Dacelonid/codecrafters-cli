package shell.command;

import shell.Main;
import shell.io.OutputWriter;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static shell.command.Command.resolve;

public class TypeCommand implements ShellCommand {

    private static class NonClosingPrintStream extends PrintStream {
        public NonClosingPrintStream(OutputStream out) {
            super(out, true, StandardCharsets.UTF_8);
        }
        @Override
        public void close() {
            // Override close to only flush, not close underlying stream
            flush();
        }
    }

    @Override
    public void execute(String[] args) {
        resolve(args[1]).explain(args);
    }

    @Override
    public void execute(String[] args, InputStream in, OutputStream out) throws Exception {
        // Fully consume the input to avoid blocking/broken pipe upstream
        byte[] buffer = new byte[8192];
        while (in.read(buffer) != -1) {
            // discard input
        }
        // Wrap the OutputStream so close() doesn't close the underlying stream
        try (PrintStream ps = new NonClosingPrintStream(out)) {
            OutputWriter.setOut(ps);
            execute(args);
            ps.flush();
        } finally {
            OutputWriter.reset();
        }
    }

    @Override
    public String getname() {
        return "type";
    }
}
