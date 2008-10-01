package com.blueskyminds.homebyfive.framework.framework.patterns.comparison;

import com.blueskyminds.homebyfive.framework.framework.tools.Named;
import com.blueskyminds.homebyfive.framework.framework.alias.Aliased;

/**
 * Date Started: 12/10/2007
 * <p/>
 * History:
 */
public class NamedStringComparator implements StringComparator<Named> {
        
    private StringComparator<String> comparator;

    /**
     *
     * @param stringComparator       the StringComparator to use when comparing two the Named object's name and aliases
     */
    public NamedStringComparator(StringComparator<String> stringComparator) {
        this.comparator = stringComparator;
    }

    /**
     * Compares a String with another Named object object and returns true if they are equivalent
     * <p/>
     * Compares the Named object aliases if any
     *
     * @param s1        the string to compare
     * @param named     the Named object to compare with
     * @return
     */
    public boolean matches(String s1, Named named) {
        boolean matches = false;

        if (named instanceof Aliased) {
            // determine if the name or aliases match the value

            Aliased aliased = (Aliased) named;
            for (String name : aliased.getNames()) {
                if (comparator.matches(name, s1)) {
                    matches = true;
                    break;
                }
            }
        } else {
            // test the name only
            matches = comparator.matches(named.getName(), s1);
        }
        return matches;
    }
}
