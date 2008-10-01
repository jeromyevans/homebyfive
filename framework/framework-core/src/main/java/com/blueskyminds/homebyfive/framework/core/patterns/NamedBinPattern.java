package com.blueskyminds.homebyfive.framework.core.patterns;

import com.blueskyminds.homebyfive.framework.core.tools.Named;
import com.blueskyminds.homebyfive.framework.core.patterns.comparison.StringComparator;

import java.util.Set;
import java.util.HashSet;

/**
 * A specialisation of a BinPattern that uses a Named object as the source of the pattern instead of a String.
 *
 * The advantage of a Named object is that it supports aliases so a match on any of the aliases is a match on the Named Object
 *
 * Date Started: 12/10/2007
 * <p/>
 * History:
 */
public class NamedBinPattern extends BinPattern {

    private Named named;
    private StringComparator<Named> comparator;

    public NamedBinPattern(Named named, StringComparator<Named> comparator, boolean exclusive, int groupNo, Object metadata) {
        super(exclusive, groupNo, metadata);
        this.named = named;
        this.comparator = comparator;
    }

    public String getPattern() {
        return named.getName();
    }

    public Named getNamed() {
        return named;
    }

    public String toString() {
        return getPattern();
    }

    /**
     * Determines whether the value matches this named object or the aliases of the named object
     *
     * Uses an exact match ignoring case
     *
     * @param value
     * @return
     */
    public boolean matches(String value) {
        return comparator.matches(value, named);
    }

     /**
     *
     * Determines if the given word matches any of the given patterns exactly.
     * Creates an Exclusive allocation.
     *
     * This implementation returns the first matching pattern
     *
     * Override this method to use non-equality matching or non-exclusive allocations.
     *
     * @param word
     * @return set of patterns that were matched (always zero or one in this case)
     */
    protected Set<PatternMatch> wordMatchesPattern(String word) {
        Set<PatternMatch> matches = new HashSet<PatternMatch>();
        if (comparator.matches(word, named)) {
            matches.add(new PatternMatch(this, isExclusive(), null, PatternMatchType.Pattern));
        }
        return matches;
    }
}
