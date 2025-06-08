import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import shell.command.Command;
import shell.command.ShellCommand;
import shell.io.OutputWriter;
import shell.path.WorkingDirectory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static Utilities.Utils.tokenize;
import static org.junit.jupiter.api.Assertions.*;

class ShellTest {

    private ByteArrayOutputStream output;

    @TempDir
    private Path tempDir;
    private Path subDir;

    @BeforeEach
    void setUp() throws IOException {
        subDir = Files.createDirectory(tempDir.resolve("tmp"));
        WorkingDirectory.get().setDir(tempDir);

        output = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(output);
        OutputWriter.setOut(out);
    }

    @AfterEach
    void tearDown() {
        OutputWriter.reset(); // Restore System.out
    }

    private String run(String input) {
        String[] tokens = tokenize(input);
        assertNotEquals(0, tokens.length, "No tokens parsed from: " + input);

        String cmdName = tokens[0];
        ShellCommand cmd = Command.resolve(cmdName);
        if (cmd == null) {
            fail("Unknown command: " + cmdName);
        }

        cmd.execute(tokens);
        return output.toString().trim();
    }

    @Test
    void testEcho() {
        String result = run("echo Hello World");
        assertEquals("Hello World", result);
    }

    @Test
    void testPwd() {
        String result = run("pwd");
        assertEquals(tempDir.toString(), result);
    }

    @Test
    void testExternalCommandNotFound(){
        String result = run("lssdf");
        assertEquals("lssdf: command not found", result);
    }


    @Test
    void testQuotedStringSingle() {
        String result = run("echo 'a b'");
        assertEquals("a b", result);
    }

    @Test
    void testQuotedStringDouble(){
        String result = run("echo \"a b\"");
        assertEquals("a b", result);
    }

    @Test
    void testEscapedSpace(){
        String result = run("echo a\\ b");
        assertEquals("a b", result);
    }

    @Test
    void testEscapedQuotesInDoubleQuotes() {
        String result = run("echo \"a \\\"quote\\\" b\"");
        assertEquals("a \"quote\" b", result);
    }

    @Test
    void testMultipleCommandsPreserveState(){
        run("cd tmp");
        String result = run("pwd");

        assertEquals(subDir.toString(), result);
    }

    @Test
    void testEmptyCommand() {
        String[] tokens = tokenize("");
        assertEquals(0, tokens.length);
    }

    @Test
    void testTypeBuiltin() {
        String result = run("type echo");
        assertEquals("echo is a shell builtin", result);
    }

    @Test
    void testTypeCommand(){
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
