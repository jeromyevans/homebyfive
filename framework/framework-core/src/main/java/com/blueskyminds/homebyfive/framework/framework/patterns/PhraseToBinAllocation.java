package com.blueskyminds.homebyfive.framework.framework.patterns;

/**
 * Records an allocation of a phrase to a bin.  Records the phrase as well as the reference to the
 * pattern that was matched
 *
 * An allocation may be exclusive (usual) or non-exclusive (special-case)
 *
 * Exclusive means the phrase is exclusively allocated to the bin with for a result, meaning other bins can't
 * use the phrase if this bin is.  This is normal.
 *
 * Non-exlusive means other bins are permitted to use this phrase IN THE SAME result. This case would typically only
 * be used when the 'phrase' is split into smaller exclusive parts by pattern consistent matching between the bins.
 *   eg. separated into two smaller phrases by something other than whitespace
 *
 * Date Started: 18/06/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */

public class PhraseToBinAllocation {

    private Bin bin;
    private Phrase phrase;
    private PatternMatch patternMatch;

    //private Pattern pattern;
    //private boolean exclusive;
     /** When a regular expression is used to match a pattern, this array contains the values matched in
     * each group defined for the expression.  By convention, groupMatches[0] is the entire pattern.
     */
    //private String[] groupMatches;

//    public PhraseToBinAllocation(Bin bin, Phrase phrase, Pattern pattern, boolean exclusive, String[] groupMatches) {
//        this.bin = bin;
//        this.phrase = phrase;
//        this.pattern = pattern;
//        this.exclusive = exclusive;
//        this.groupMatches = groupMatches;
//    }

    public PhraseToBinAllocation(Bin bin, Phrase phrase, PatternMatch patternMatch) {
        this.bin = bin;
        this.phrase = phrase;
        this.patternMatch = patternMatch;
    }

    public Phrase getPhrase() {
        return phrase;
    }

    public Pattern getPattern() {
        return patternMatch.getPattern();
    }

    public Bin getBin() {
        return bin;
    }

    public boolean isExclusive() {
        return patternMatch.isExclusive();
    }    

    public String[] getGroupMatches() {
        return patternMatch.getGroupMatches();
    }

    public String toString() {
        return "'"+phrase.getStringLiteral() + "' to " +bin.getClass().getSimpleName()+"("+getPattern().toString()+")";
    }

    public PatternMatchType getPatternMatchType() {
        return patternMatch.getType();
    }

    /**
     * Equals if same bin and same phrase with same pattern
     *
     * @param o
     * @return
     */
    public boolean equals(Object o) {
        if (PhraseToBinAllocation.class.equals(o.getClass())) {
            PhraseToBinAllocation other = (PhraseToBinAllocation) o;
            return ((this.bin.equals(other.bin)) && (this.phrase.equals(other.phrase)) && (this.patternMatch.getPattern().equals(other.patternMatch.getPattern())));
        } else {
            return false;
        }
    }
}
