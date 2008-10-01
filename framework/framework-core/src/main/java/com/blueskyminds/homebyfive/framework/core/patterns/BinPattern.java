package com.blueskyminds.homebyfive.framework.core.patterns;

import java.util.Set;

/**
 * Represents a pattern used to match a Bin.  The pattern contains:
 *   a pattern (may be regex - up to the bin implementation)
 *   an optional substitution
 *   exlusiveflag indicates whether the match should be treated as exclusive, or if the phrase can be used for
 *    other matches
 *   the groupNo may be used to specify a groupNo if the pattern is a regular expression (to extract a value out
 *    of the match)
 *   metadata - the metadata can be used to associate the pattern back with source or context
 *
 * Date Started: 18/06/2006
 * <p/>
 * History:
 */
public abstract class BinPattern implements Pattern {

    protected String substitution;
    protected boolean exclusive;
    protected int groupNo;
    protected Object metadata;

    protected BinPattern(String substitution, boolean exclusive, int groupNo, Object metadata) {
        this.substitution = substitution;
        this.exclusive = exclusive;
        this.groupNo = groupNo;
        this.metadata = metadata;
    }

    protected BinPattern(boolean exclusive, int groupNo, Object metadata) {
        this.exclusive = exclusive;
        this.groupNo = groupNo;
        this.metadata = metadata;
    }

    public abstract String getPattern();

    public String getSubstitution() {
        return substitution;
    }

    public void setSubstitution(String substitution) {
        this.substitution = substitution;
    }

    public Object getMetadata() {
        return metadata;
    }

    public void setMetadata(Object metadata) {
        this.metadata = metadata;
    }

    public boolean isExclusive() {
        return exclusive;
    }

    public void setExclusive(boolean exclusive) {
        this.exclusive = exclusive;
    }

    public int getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(int groupNo) {
        this.groupNo = groupNo;
    }

    /**
     * Determines whether the supplied value matches this pattern
     *
     * @param value        the value to match
     * @return true if there's a match
     */
    public abstract boolean matches(String value);

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
    protected abstract Set<PatternMatch> wordMatchesPattern(String word);        
}
