package com.blueskyminds.framework.patterns;

/**
 * Describes the matching between a Phrase and a Pattern
 *
 * Date Started: 22/06/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class PatternMatch {

    public static final boolean EXCLUSIVE_ALLOCATION = true;
    public static final boolean NON_EXCLUSIVE_ALLOCATION = false;

    /** The pattern that was matched */
    private Pattern pattern;
    /** Whether the match is exclusive or not */
    private boolean exclusive;

    private String[] groupMatches;

    /** The type of pattern match that was made */
    private PatternMatchType type;

    // ------------------------------------------------------------------------------------------------------

    /**
     * @param exclusive if true, identifies that the phrase is exclusively allocated to the bin.  If false, the
     *  allocation is non-exclusive, meaning other bins are permitted to use this phrase IN THE SAME result.
     * The latter case would typically only be used when the 'phrase' is split into smaller exclusive parts
     * by the bins.
     */
    public PatternMatch(Pattern pattern, boolean exclusive, String[] groupMatches, PatternMatchType type) {
        //this.matchingPatternIndex = matchingPatternIndex;
        this.pattern = pattern;
        this.exclusive = exclusive;
        this.groupMatches = groupMatches;
        this.type = type;
    }

    // ------------------------------------------------------------------------------------------------------

//    public int getMatchingPatternIndex() {
//        return matchingPatternIndex;
//    }

    /** The pattern that was matched */
    public Pattern getPattern() {
        return pattern;
    }

    public boolean isExclusive() {
        return exclusive;
    }

    public void setExclusive(boolean exclusive) {
        this.exclusive = exclusive;
    }

    public String[] getGroupMatches() {
        return groupMatches;
    }

    public PatternMatchType getType() {
        return type;
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
}
