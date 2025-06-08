package Utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility methods used throughout the shell interpreter.
 * This class is not meant to be instantiated.
 */
public final class Utils {

    // Private constructor to prevent instantiation
    private Utils() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Determines whether a given string can be parsed as an integer.
     *
     * @param s the input string
     * @return {@code true} if the string can be parsed as an integer; {@code false} otherwise
     */
    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Tokenizes a shell command input string into an array of arguments,
     * correctly handling quoting and escaping according to POSIX shell rules.
     *
     * <p>This supports:
     * <ul>
     *     <li>Whitespace as a separator (unless inside quotes)</li>
     *     <li>Single quotes: literal content, no escapes</li>
     *     <li>Double quotes: allows \", \\, \$, \`</li>
     *     <li>Backslashes outside quotes escape the next character</li>
     * </ul>
     *
     * @param input the raw command input string
     * @return an array of parsed tokens
     */
    public static String[] tokenize(String input) {
        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        boolean inSingleQuote = false;
        boolean inDoubleQuote = false;
        boolean escaping = false;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (escaping) {
                // Handle escaped character
                if (inDoubleQuote && (c == '"' || c == '\\' || c == '$' || c == '`')) {
                    current.append(c);
                } else if (!inSingleQuote && !inDoubleQuote) {
                    current.append(c); // Strip backslash, append char
                } else {
                    current.append('\\').append(c); // Keep backslash in single quotes
                }
                escaping = false;
                continue;
            }

            if (c == '\\') {
                if (inSingleQuote) {
                    current.append(c); // Backslashes are literal in single quotes
                } else {
                    escaping = true;
                }
                continue;
            }

            if (c == '\'' && !inDoubleQuote) {
                inSingleQuote = !inSingleQuote;
                continue;
            }

            if (c == '"' && !inSingleQuote) {
                inDoubleQuote = !inDoubleQuote;
                continue;
            }

            if (Character.isWhitespace(c) && !inSingleQuote && !inDoubleQuote) {
                if (current.length() > 0) {
                    tokens.add(current.toString());
                    current.setLength(0);
                }
                continue;
            }

            current.append(c);
        }

        if (current.length() > 0) {
            tokens.add(current.toString());
        }

        return tokens.toArray(new String[0]);
    }
}
