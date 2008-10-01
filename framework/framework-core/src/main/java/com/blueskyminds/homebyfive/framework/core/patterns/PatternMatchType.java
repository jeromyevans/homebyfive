package com.blueskyminds.homebyfive.framework.core.patterns;

/**
 * Identities the type of match performed.  The type can be used to weigh the match
 *
 * Date Started: 27/10/2007
 * <p/>
 * History:
 */
public enum PatternMatchType {
    Exact,              // an exact match
    Fuzzy,              // a close match
    Pattern             // a match based on a pattern/bound
}
