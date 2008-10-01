package com.blueskyminds.homebyfive.framework.core.tools.text;

import com.blueskyminds.homebyfive.framework.core.tasks.TaskInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Set;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.ParseException;

/**
 *
 * Additional methods for handling strings
 *
 * Date Started: 18/06/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class StringTools {

    public static final Pattern NON_DIGIT_PATTERN = Pattern.compile("\\D");
    public static final Pattern NUMBER_PATTERN = Pattern.compile("[\\d|\\-|\\.]");
    public static final Pattern NON_NUMBER_PATTERN = Pattern.compile("[^\\d|\\-|\\.]");
    public static final char[] NON_BREAKING_WHITESPACE = { '\u00A0', '\u2007', '\u202F' };
    public static final String NON_BREAKING_WHITESPACE_CHARS = new String(NON_BREAKING_WHITESPACE);

    private static final String[] HTML_ESCAPE_PATTERNS =
        { "&lt;", "&gt;", "&quot;", "&#039;", "&#092;", "&amp;" };

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /** Remove the leading punctuation from a string */
    public static String stripLeadingPunctuation(String inputString) {
        String outputString = inputString;
        Pattern leadingPunctuation = Pattern.compile("^\\p{Punct}+");
        Matcher leading = leadingPunctuation.matcher(inputString);
        if (leading.find()) {
            outputString = leading.replaceAll("");
        }
        return outputString;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Remove the trailing punctuation from a string */
    public static String stripTrailingPunctuation(String inputString) {
        String outputString = inputString;
        Pattern trailingPunctuation = Pattern.compile("\\p{Punct}+$");
        Matcher trailing = trailingPunctuation.matcher(inputString);
        if (trailing.find()) {
            outputString = trailing.replaceAll("");
        }
        return outputString;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Removes leading and trailing punctuation from a string.
     * Punctuation is as defined by POSIX US-ASCII  */
    public static String stripPunctuation(String inputString) {
        return stripLeadingPunctuation(stripTrailingPunctuation(inputString)).trim();
    }

    /** Removes leading and trailing single or double quotes from a string if present
     *  at both ends */
    public static String stripQuotes(String inputString) {
        String outputString = StringUtils.trim(inputString);
        if (outputString.startsWith("\"")) {
            if (outputString.endsWith("\"")) {
                outputString = outputString.substring(1, outputString.length()-1);
            }
        } else {
            if (outputString.startsWith("'")) {
                if (outputString.endsWith("'")) {
                    outputString = outputString.substring(1, outputString.length()-1);
                }
            }
        }
        return outputString;        
    }

    // ------------------------------------------------------------------------------------------------------

    /** Appends an array of words into a single string, separated by whitespace.
     * The words are obtains from the input array from the first to the last index
     *
     * @param words
     * @param first
     * @param last
     * @return the string of joined words
     */
    public static String joinWords(String[] words, int first, int last) {
        StringBuffer sb = new StringBuffer();
        for (int index = first; index <= last; index++) {
            if (index > first) {
                sb.append(" ");
            }
            sb.append(words[index]);
        }
        return sb.toString();
    }

    /**
     * Asserts that the input string only contains numeric characters (including . and -)
     *
     *  A blank input returns false
     *
     * @param inputString
     * @return true if the input string contains only numbers or . or -
     */
    public static boolean isNumeric(String inputString) {
        if (StringUtils.isNotBlank(inputString)) {
            Matcher matcher = NON_NUMBER_PATTERN.matcher(inputString);  // contains non-numbers?
            return (!matcher.find());
        } else {
            return false;
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /** Extracts the first encountered whole number (consecuative digits) in the input string.
     * @param occurrenceNo can be used to specify which occurence of a number to return (0 is the first) */
    private static String extractNumber(String inputString, Pattern pattern, int occurrenceNo) {
        String found = null;
        int occurences = 0;

        if (inputString != null) {
            String[] parts = pattern.split(inputString);
            if (parts != null) {
                for (String part : parts) {
                    if (isNumeric(part)) {
                        if (occurences == occurrenceNo) {
                            found = part;
                        } else {
                            occurences++;
                        }
                        break;
                    }
                }
            }
        }
        return found;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Extracts the first encountered whole number (consecuative digits) in the input string. */
    public static String extractInteger(String inputString) {
        return extractNumber(inputString, NON_DIGIT_PATTERN, 0);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Extracts the first encountered whole number (consecuative digits) in the input string. */
    public static String extractInteger(String inputString, int occurrenceNo) {
        return extractNumber(inputString, NON_DIGIT_PATTERN, occurrenceNo);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Extracts the first encountered whole number (consecuative digits) in the input string. */
    public static int extractInt(String inputString, int defaultValue) {
        return extractInt(inputString, 0, defaultValue);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Extracts the first encountered whole number (consecuative digits) in the input string. */
    public static int extractInt(String inputString, int occurrenceNo, int defaultValue) {
        int value = defaultValue;
        try {
            String number = extractInteger(inputString, occurrenceNo);
            if (number != null) {
                value = Integer.parseInt(number);
            }
        } catch (NumberFormatException e) {
            // use default
        }
        return value;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Extracts the first encountered real number (consecuative digits) in the input string. */
    public static String extractReal(String inputString) {
        return extractNumber(inputString, NON_NUMBER_PATTERN, 0);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Extracts the n'th encountered real  number (consecuative digits) in the input string. */
    public static String extractReal(String inputString, int occurrenceNo) {
        return extractNumber(inputString, NON_NUMBER_PATTERN, occurrenceNo);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Extracts the first encountered real number (consecuative digits) in the input string. */
    public static float extractFloat(String inputString, float defaultValue) {
        return extractFloat(inputString, 0, defaultValue);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Extracts the n'th encountered real number (consecuative digits including . and - ) in the input string. */
    public static float extractFloat(String inputString, int occurrenceNo, float defaultValue) {
        float value = defaultValue;
        try {
            String number = extractNumber(inputString, NON_NUMBER_PATTERN, occurrenceNo);
            if (number != null) {
                value = Float.parseFloat(number);
            }
        } catch (NumberFormatException e) {
            // use default
        }
        return value;
    }

    private static final String[] COMMON_DATE_PATTERNS = {
            "dd/mm/yyyy",
            "dd/mm/yy",
            "dd-mm-yyyy",
            "dd-mm-yy",
            "yyyy-MM-dd",
            "dd MMM yyyy",
            "dd MMM yy",
            "dd-MMM-yyyy",
            "dd-MMM-yy",
            "MMM dd yyyy",
            "MMM dd yy",
            "dd MMMM yyyy",
            "MMMM dd yyyy",
             };

    /** Attempts to extract a date from an input string using a variety of common patterns.
     * Note: does not support the US silly order MMddyy
      * @param inputString
     * @return
     */
    public static Date extractDate(String inputString) {
        Date date = null;
        try {
            if (StringUtils.isNotBlank(inputString)) {
                date = DateUtils.parseDate(inputString, COMMON_DATE_PATTERNS);
            }
        } catch(ParseException e) {
            // fail
        }
        return date;
    }

    /** Removes leading and trailing whitespace from a string INCLUDING non-breaking spaces */
    public static String strip(String inputString) {
        String firstPass = StringUtils.strip(inputString);
        return StringUtils.strip(firstPass, NON_BREAKING_WHITESPACE_CHARS);
    }

    /** Replace each non-breaking space in a string with a whitespace character */
    public static String replaceNonBreakingSpaces(String inputString) {
        for (char ch : NON_BREAKING_WHITESPACE) {
            inputString = StringUtils.replaceChars(inputString, ch, ' ');
        }
        return inputString;
    }

    /**
     * Split the input string on whitespace, including non-breaking spaces
     *
     * @param inputString   contains zero or more whitespaces
     * @return array of strings separated by whitespace
     */
    public static String[] split(String inputString) {
        String firstPass = replaceNonBreakingSpaces(inputString);
        return StringUtils.split(firstPass);
    }

    /** Create a string containing the specified sequence up to the given length
     *
     *  eg.  fill("A", 3) = "AAA"
     *       fill("ha", 4) = "haha"
     *       fill("ha", 5) = "hahah"
     *
     * @param sequence  1 or more characters
     * @param length    exact length
     *
     * @return new string filled with the sequence */
    public static String fill(String sequence, int length) {
        String result = "";

        while (result.length() < length) {
            result += sequence;
        }

        return result.substring(0, length);
    }

    /**
     * Create a string containing the specified number of occurrences of the sequence
     *
     * @param sequence    1 or more characters
     * @param occurrences number of occurences of the sequence
     *
     * @return new string filled with the sequence */
    public static String fillRepeat(String sequence, int occurrences) {
        String result = "";

        for (int i = 0; i < occurrences; i ++) {
            result += sequence;
        }

        return result;
    }

    /** Determines if the string contains the segment */
    public static boolean containsIgnoreCase(String string, String subString) {
        if (string != null) {
            return StringUtils.lowerCase(string).contains(StringUtils.lowerCase(subString));
        } else {
            return false;
        }
    }

    /** Determines if a string contains any of the specified words, ignoring the case */
    public static boolean containsAnyIgnoreCase(String string, String... words) {
        boolean found = false;
        for (String word : words) {
            if (containsIgnoreCase(string, word)) {
                found = true;
                break;
            }
        }
        return found;
    }

    /** Determines if a string contains any of the specified words, ignoring the case */
    public static boolean containsAny(String string, String... words) {
        boolean found = false;
        for (String word : words) {
            if (StringUtils.contains(string, word)) {
                found = true;
                break;
            }
        }
        return found;
    }

    /** Replace the carriage returns in a plain-text with <br/> */
    public static boolean formatAsHtmlCode(Set<TaskInfo> taskInfos) {
        return false;  //To change body of created methods use File | Settings | File Templates.
    }

    /**
     * Escape a string for use in HTTP
     * @param  value to escape
     * @return null if not possible (only if UTF-8 is not supported by this system)
     **/
    public static String escapeForHTTP(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String unescapeFromHTTP(String value) {
        try {
            if (value != null) {
                return URLDecoder.decode(value, "UTF-8");
            } else {
                return null;
            }
        } catch(UnsupportedEncodingException e) {
            return null;
        }
    }

    /** True if the value starts with any of the patterns in the list */
    public static boolean startsWithAny(String string, String[] list) {
        boolean found = false;
        for (String pattern : list) {
            if (string.startsWith(pattern)) {
                found = true;
                break;
            }
        }
        return found;
    }

    /**
     * Escape a string for use in HTML
     *
     * @param value         value to escape
     * @param insertBreaks  if true, the carriage returns will be replaced with <br/>
     * @param nonBreakingSpaces if true, the spaces will be repaced with &nbsp;
     * @return escaped value
     **/
    public static String escapeForHTML(String value, boolean insertBreaks, boolean nonBreakingSpaces) {

        StringBuilder result = new StringBuilder();
        int index = 0;

        for (char ch : value.toCharArray()) {
            switch (ch) {
                case '<':
                    result.append(HTML_ESCAPE_PATTERNS[0]);
                    break;
                case '>':
                    result.append(HTML_ESCAPE_PATTERNS[1]);
                    break;
                case '\"':
                    result.append(HTML_ESCAPE_PATTERNS[2]);
                    break;
                case '\'':
                    result.append(HTML_ESCAPE_PATTERNS[3]);
                    break;
                case '\\':
                    result.append(HTML_ESCAPE_PATTERNS[4]);
                    break;
                case '&':
                    // don't escape an already escaped &
                    String subString = StringUtils.substring(value, index);
                    if (!startsWithAny(subString, HTML_ESCAPE_PATTERNS)) {
                        result.append(HTML_ESCAPE_PATTERNS[5]);
                    }
                    break;
                case '\n':
                    if (insertBreaks) {
                        result.append("<br/>");
                    }
                    result.append(ch);
                case ' ': {
                    if (nonBreakingSpaces) {
                        result.append("&nbsp;");
                    } else {
                        result.append(ch);
                    }
                }
                default:
                    result.append(ch);
                    break;
            }
            index++;
        }
        return result.toString();
    }

    public static boolean startWithIgnoreCase(String string, String substring) {
        if ((string != null) && (substring != null)) {
            return string.toLowerCase().startsWith(substring.toLowerCase());
        } else {
            return false;
        }
    }

    /** Truncate a string to the maximum length specified */
    public static String truncate(String str, int maxLength) {
        if (str != null) {
            if (str.length() > maxLength) {
                str = str.substring(0, maxLength);
            }
        }
        return str;
    }

    /** Accepts a whitespace char if the previous wasn't a whitepace */
    private static boolean accept(char last, char b) {
        if (Character.isWhitespace(last)) {
            return !Character.isWhitespace(b);
        } else {
            return true;
        }
    }

    /** Removes consecutive whitespace from a string.  Also normalized all spaces to ' ' and trims
     * training and leading whitespace */
    public static String stripConsecutiveWhitespace(String str) {
        char[] b = str.toCharArray();
        char[] filtered = new char[b.length];

        char last = 'a';
        int offset = 0;
        int length = filtered.length;

        int index = 0;
        for (int sourceIndex = offset; sourceIndex < offset + length; sourceIndex++) {
            if (accept(last, b[sourceIndex])) {
                if (!Character.isWhitespace(b[sourceIndex])) {
                    filtered[index++] = b[sourceIndex];
                } else {
                    filtered[index++] = ' ';  // normalize whitespace
                }
            }
            last = b[sourceIndex];
        }
        return new String(filtered, 0, index).trim();
    }

    /**
     * Append a suffix to multiple lines
     *
     * @param strings
     * @param suffix
     * @return
     */
    public static String[] appendSuffix(String[] strings, String suffix) {
        if ((strings != null) && (suffix != null)) {
            String[] result = new String[strings.length];
            int index = 0;
            for (String string : strings) {
                result[index++] = string+suffix;
            }
            return result;
        } else {
            return new String[0];
        }
    }

    public static boolean equalsAnyIgnoreCase(String str, String[] candidates) {
        if (str != null && candidates != null) {
            for (String candidate : candidates) {
                if (candidate != null) {
                    if (str.equalsIgnoreCase(candidate)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
