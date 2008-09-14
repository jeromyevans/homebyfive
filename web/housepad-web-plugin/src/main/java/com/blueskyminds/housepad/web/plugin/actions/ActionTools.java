package com.blueskyminds.housepad.web.plugin.actions;

import org.apache.commons.collections.OrderedMap;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Date Started: 29/04/2008
 */
public class ActionTools {

    private static final String[] HTML_ESCAPE_PATTERNS =
                { "&lt;", "&gt;", "&quot;", "&#039;", "&#092;", "&amp;" };

    /**
     * Create an ordered Map<String, String>  from the allowed values of an enumeration
     *
     * The enum's name() is used as the key.
     * The enum's toString() output is used as the value.
     *
     * // todo: shouldn't need to escape here - the tag should do that
     *
     * @param values
     * @return
     */
    public static <E extends Enum> OrderedMap toOrderedMap(E[] values) {
        OrderedMap result = new LinkedMap();

        for (E enumValue : values) {
            //result.put(escapeForHTTP(enumValue.name()), escapeForHTML(enumValue.toString()));
            result.put(enumValue.name(), enumValue.toString());
        }

        return result;
    }


     /**
     * Create an ordered Map<String, String> from the values of a List
     *
     * The index is used as the key.
     * The value's toString() output is used as the value.
     *
     * @param values
     * @return
     */
    public static OrderedMap toOrderedMap(List values) {
        OrderedMap result = new LinkedMap();

        for (int index = 0; index < values.size(); index++) {
            Object value = values.get(index);
            if (value != null) {
                result.put(Integer.toString(index), value.toString());
            } else {
                result.put(Integer.toString(index), null);
            }
        }

        return result;
    }

    /**
     * Escape a string for use in HTTP
     * 
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

    /** True if the value starts with any of the patterns in the list */
    private static boolean startsWithAny(String string, String[] list) {
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
     * @param value     value to escape
     * @return escaped value
     **/
    public static String escapeForHTML(String value) {

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
                default:
                    result.append(ch);
                    break;
            }
            index++;
        }
        return result.toString();
    }
}
