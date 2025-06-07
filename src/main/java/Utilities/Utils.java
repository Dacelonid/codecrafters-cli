package Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }



    //Pattern to match, double quotes, single quotes or unquoted content
    private static final Pattern TOKEN_PATTERN = Pattern.compile("\"([^\"]*)\"|'([^']*)'|([^\\s\"']+)");

    public static final String WHITESPACE = ".*\\s+.*";

    public static String[] tokenize(String input) {
        List<String> parsedTokens = new ArrayList<>();
        Matcher matcher = TOKEN_PATTERN.matcher(input);

        StringBuilder current = new StringBuilder();
        int lastEnd = 0; //end of previous match

        //Get all the regex matches
        while (matcher.find()) {
            String value = getToken(matcher);

            // If there's whitespace between this match and the last, it's a new token
            if (matcher.start() > lastEnd && input.substring(lastEnd, matcher.start()).matches(WHITESPACE)) {
                //If we are in a token, finalize it and store it
                if (!current.isEmpty()) {
                    parsedTokens.add(current.toString());
                    current.setLength(0);
                }
            }

            // Append this value to the current token (merging adjacent quoted parts)
            current.append(value);
            lastEnd = matcher.end();// Move lastEnd to end of this match
        }

        // Add final token if any
        if (!current.isEmpty()) {
            parsedTokens.add(current.toString());
        }

        return parsedTokens.toArray(new String[0]);
    }

    private static String getToken(Matcher matcher) {
        String value = matcher.group(1); //check for double-quoted strings
        if (value == null) value = matcher.group(2);//check for single quoted strings
        if (value == null) value = matcher.group(3);//check for unquoted strings
        if (value == null) value = ""; // in case of completely empty match
        return value;
    }
}
