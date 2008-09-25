package com.blueskyminds.framework.patterns.comparison;

import com.blueskyminds.framework.patterns.LevensteinDistanceTools;

/**
 * Compares two strings and evalates them as equivalent if they're almost the same as determined by fuzzy metrics
 *
 * Date Started: 12/10/2007
 * <p/>
 * History:
 */
public class FuzzyStringComparator implements StringComparator<String> {

    /**
     * Compares two strings and evalates them as equivalent if they're almost the same as determined by fuzzy metrics
     *
     * @param s1
     * @param s2
     * @return
     */
    public boolean matches(String s1, String s2) {
        if (s1 != null) {
            if (s2 != null) {
                return LevensteinDistanceTools.isSimilar(s1, s2);
            } else {
                return false;
            }
        } else {
            return s2 == null;
        }
    }
}
