package Utilities;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String[] tokenize(String input) {
        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        boolean inSingleQuote = false;
        boolean inDoubleQuote = false;
        boolean escaping = false;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (escaping) {
                // In double quotes: only escape certain characters
                if (inDoubleQuote && (c == '"' || c == '\\' || c == '$' || c == '`')) {
                    current.append(c);
                } else if (!inDoubleQuote && !inSingleQuote) {
                    // Outside quotes: remove the backslash, append next char literally
                    current.append(c);
                } else {
                    // In single quotes or other cases: keep the backslash
                    current.append('\\').append(c);
                }
                escaping = false;
                continue;
            }

            if (c == '\\') {
                if (inSingleQuote) {
                    // Backslashes are literal inside single quotes
                    current.append(c);
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
