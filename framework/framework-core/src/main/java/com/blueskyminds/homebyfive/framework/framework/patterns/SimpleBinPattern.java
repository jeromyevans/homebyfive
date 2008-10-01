package com.blueskyminds.homebyfive.framework.framework.patterns;

import com.blueskyminds.homebyfive.framework.framework.patterns.comparison.StringComparator;

import java.util.Set;
import java.util.HashSet;

/**
 * Represents a simple pattern used by a Bin.  The pattern contains:
 *   a pattern (may be regex - up to the bin implementation)
 *   an optional substitution
 *   exlusiveflag indicates whether the match should be treated as exclusive, or if the phrase can be used for
 *    other matches
 *   the groupNo may be used to specify a groupNo if the pattern is a regular expression (to extract a value out
 *    of the match)
 *   metadata - the metadata can be used to associate the pattern back with source or context
 *
 * Date Started: 18/06/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class SimpleBinPattern extends BinPattern {

    private String pattern;
    private StringComparator comparator;

    public SimpleBinPattern(String pattern, StringComparator comparator, String substitution, boolean exclusive, int groupNo, Object metadata) {
        super(substitution, exclusive, groupNo, metadata);
        this.pattern = pattern;
        this.comparator = comparator;
    }

    public SimpleBinPattern(String pattern, StringComparator comparator, boolean exclusive, int groupNo, Object metadata) {
        super(exclusive, groupNo, metadata);
        this.pattern = pattern;
        this.comparator = comparator;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    /**
     * Determines whether the supplied value matches this pattern
     *
     * @param value
     * @return true if there's a match
     */
    public boolean matches(String value) {
        return comparator.matches(pattern, value);
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
        if (comparator.matches(pattern, word)) {
            matches.add(new PatternMatch(this, isExclusive(), null, PatternMatchType.Pattern));
        }
        return matches;
    }
    
    public String toString() {
        return pattern;
    }
}