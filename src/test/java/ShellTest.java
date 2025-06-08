import Utilities.OutputClass;
import Utilities.OutputWriter;
import Utilities.WorkingDirectory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static Utilities.Utils.tokenize;
import static org.junit.jupiter.api.Assertions.*;

class ShellTest {

    private ByteArrayOutputStream output;
    private PrintStream out;

    @TempDir
    private Path tempDir;
    private Path subDir;

    @BeforeEach
    void setUp() throws IOException {
        subDir = Files.createDirectory(tempDir.resolve("tmp"));
//        WorkingDirectory.reset();
        WorkingDirectory.get().setDir(tempDir);

        output = new ByteArrayOutputStream();
        out = new PrintStream(output);
        OutputWriter.setOut(out);
    }

    @AfterEach
    void tearDown() throws IOException {
        OutputWriter.reset(); // Restore System.out
    }

    private String run(String input) throws Exception {
        String[] tokens = tokenize(input);
        assertNotEquals(0, tokens.length, "No tokens parsed from: " + input);

        String cmdName = tokens[0];
        Command cmd = Command.fromString(cmdName);
        if (cmd == null) {
            fail("Unknown command: " + cmdName);
        }

        cmd.execute(tokens);
        return output.toString().trim();
    }

    @Test
    void testEcho() throws Exception {
        String result = run("echo Hello World");
        assertEquals("Hello World", result);
    }

    @Test
    void testPwd() throws Exception {
        String result = run("pwd");
        assertEquals(tempDir.toString(), result);
    }

    @Test
    void testExternalCommandNotFound() throws Exception {
        String result = run("lssdf");
        assertEquals("lssdf: command not found", result);
    }


    @Test
    void testQuotedStringSingle() throws Exception {
        String result = run("echo 'a b'");
        assertEquals("a b", result);
    }

    @Test
    void testQuotedStringDouble() throws Exception {
        String result = run("echo \"a b\"");
        assertEquals("a b", result);
    }

    @Test
    void testEscapedSpace() throws Exception {
        String result = run("echo a\\ b");
        assertEquals("a b", result);
    }

    @Test
    void testEscapedQuotesInDoubleQuotes() throws Exception {
        String result = run("echo \"a \\\"quote\\\" b\"");
        assertEquals("a \"quote\" b", result);
    }

    @Test
    void testMultipleCommandsPreserveState() throws Exception {
        run("cd tmp");
        String result = run("pwd");

        assertEquals(subDir.toString(), result);
    }

    @Test
    void testEmptyCommand() throws Exception {
        String[] tokens = tokenize("");
        assertEquals(0, tokens.length);
    }

    @Test
    void testTypeBuiltin() throws Exception {
        String result = run("type echo");
        assertEquals("echo is a shell builtin", result);
    }

    @Test
    void testTypeCommand() throws Exception {
        String os = System.getProperty("os.name").toLowerCase();
        String command;
        String expected1;
        String expected2;
        if (os.contains("win")) {
            command = "type where";
            expected1 = "C:\\WINDOWS\\system32\\where"; // either the type command returns without the extension
            expected2 = "C:\\WINDOWS\\system32\\where.exe"; // or with it
        } else {
            command = "type ls";
            expected1 = "/usr/bin/ls"; // depends on environment, this is on my linux
            expected2 = "/bin/ls"; // this in on CodeCrafters
        }

        String result = run(command).trim();
        assertOneOf(expected1, expected2, result); //Check if the actual is one of the expected values
    }

    private static void assertOneOf(String expected1, String expected2, String actual) {
        if (!actual.equals(expected1) && !actual.equals(expected2)) {
            fail("Expected one of:\n  " + expected1 + "\n  " + expected2 + "\nbut got:\n  " + actual);
        }
    }
}
