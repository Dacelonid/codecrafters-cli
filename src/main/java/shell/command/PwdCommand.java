package shell.command;

import shell.path.WorkingDirectory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class PwdCommand implements ShellCommand {

    @Override
    public void execute(String[] args, InputStream in, OutputStream out) throws IOException {
        PrintStream ps = new PrintStream(out, true);
        try  {
            ps.println(WorkingDirectory.get().getDir());
        }finally{
            ps.flush();
        }
    }

    @Override
    public String getname() {
        return "pwd";
    }
}
