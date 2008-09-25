package com.blueskyminds.framework.patterns.comparison;

import com.blueskyminds.framework.tools.Named;
import com.blueskyminds.framework.patterns.LevensteinDistanceTools;

/**
 *
 * Compares a String with a Named object object and returns true if they are equivalent as determined by a fuzzy
 * matching algorithm
 *
 * Date Started: 12/10/2007
 * <p/>
 * History:
 */
public class FuzzyNamedStringComparator implements StringComparator<Named> {

    /**
     * Compares a String with a Named object and returns true if they are equivalent as determined by a fuzzy
     * matching algorithm that uses the name and aliases for the Named object
     * <p/>
     *
     * @param s1 the string to compare
     * @param s2 the Named object to compare with
     * @return
     */
    public boolean matches(String s1, Named s2) {
        return LevensteinDistanceTools.isSimilar(s1, s2);
    }
}
