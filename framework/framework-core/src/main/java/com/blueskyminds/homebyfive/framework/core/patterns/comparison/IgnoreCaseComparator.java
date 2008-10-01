package com.blueskyminds.homebyfive.framework.core.patterns.comparison;

/**
 * Compares two strings for equality ignoring case
 *
 * Date Started: 12/10/2007
 * <p/>
 * History:
 */
public class IgnoreCaseComparator implements StringComparator<String> {

    /**
     * True if s1 and s2 are equal ignoring case
     *
     * @param s1
     * @param s2
     * @return true if s1 and s2 are equal (ignoring case)
     */
    public boolean matches(String s1, String s2) {
        if (s1 != null) {
            if (s2 != null) {
                return s1.equalsIgnoreCase(s2);
            } else {
                return false;
            }
        } else {
            return s2 == null;
        }
    }
}
