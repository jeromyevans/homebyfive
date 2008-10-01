package com.blueskyminds.homebyfive.framework.core.patterns;

import java.util.regex.Matcher;
import java.util.Set;
import java.util.HashSet;

/**
 * Represents a simple RegEx pattern used by a Bin.  The pattern contains:
 *
 *   a pre-compiled pattern
 *   an optional substitution
 *   exlusiveflag indicates whether the match should be treated as exclusive, or if the phrase can be used for
 *    other matches
 *   the groupNo may be used to specify a groupNo if the pattern is a regular expression (to extract a value out
 *    of the match)
 *   metadata - the metadata can be used to associate the pattern back with source or context
 *
 * Date Started: 18/07/2008
 *
 * History:
 *
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class RegExBinPattern extends BinPattern {

    private java.util.regex.Pattern pattern;

    public RegExBinPattern(java.util.regex.Pattern  pattern, String substitution, boolean exclusive, int groupNo, Object metadata) {
        super(substitution, exclusive, groupNo, metadata);
        this.pattern = pattern;
    }

    public RegExBinPattern(java.util.regex.Pattern  pattern, boolean exclusive, int groupNo, Object metadata) {
        super(exclusive, groupNo, metadata);
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern.pattern();
    }

    /**
     * Applies regular expressions to determine if the word matches a regular expression pattern.
     * Implementations may override wordMatchesPattern to call this regular expression variant.
     *
     * This implementation returns the first matching pattern
     *
     * If the regular expression contains groups, the group matches are all saved in the groupMatches array
     *
     * */
    protected Set<PatternMatch> wordMatchesPattern(String word) {
        String[] groupMatches;

        Set<PatternMatch> matches = new HashSet<PatternMatch>();

        Matcher matcher = pattern.matcher(word);

        if (matcher.find()) {
            // if there's any groups defined in the pattern, remember these
            if (matcher.groupCount() > 0) {
                groupMatches = new String[matcher.groupCount()+1];             // +1 to include full match at index 0
                // transfer the group values from the matcher to the array of group matches
                for (int groupNo = 0; groupNo < groupMatches.length; groupNo++) {
                    groupMatches[groupNo] = matcher.group(groupNo);
                }
            } else {
                // groupMatches contains only the entire match - no groups were defined
                groupMatches = new String[1];
                groupMatches[0] = matcher.group();
            }


            // create a pattern match, specify if its exclusive or not, and include the group results
            matches.add(new PatternMatch(this, exclusive, groupMatches, PatternMatchType.Pattern));
        }

        return matches;
    }

    /**
     * Determines whether the supplied value matches this pattern
     *
     * @param value the value to match
     * @return true if there's a match
     */
    public boolean matches(String value) {
        Matcher matcher = pattern.matcher(value);
        return matcher.find();
    }

    public String toString() {
        return pattern.pattern();
    }
}