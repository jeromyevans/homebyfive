package com.blueskyminds.homebyfive.framework.core.patterns.comparison;

/**
 * A StringComparator compares two Strings for equality
 *
 * Date Started: 12/10/2007
 * <p/>
 * History:
 */
public interface StringComparator<T> {

    /**
     * Compares a String with another object and returns true if they are equivalent
     *
     * @param s1    the string to compare
     * @param s2    the object to compare with
     * @return
     */
    boolean matches(String s1, T s2);
}
