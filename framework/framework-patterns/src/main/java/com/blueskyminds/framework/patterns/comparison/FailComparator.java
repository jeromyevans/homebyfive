package com.blueskyminds.framework.patterns.comparison;

/**
 * Date Started: 12/10/2007
 * <p/>
 * History:
 */
public class FailComparator<T> implements StringComparator<T>{


    /**
     * Compares a String with another object and returns false (always)
     *
     * @param s1 the string to compare
     * @param s2 the object to compare with
     * @return false
     */
    public boolean matches(String s1, T s2) {
        return false;
    }
}
