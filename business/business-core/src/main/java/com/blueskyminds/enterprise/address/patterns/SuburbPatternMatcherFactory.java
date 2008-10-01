package com.blueskyminds.enterprise.address.patterns;

import com.blueskyminds.homebyfive.framework.core.patterns.PatternMatcherInitialisationException;
import com.blueskyminds.enterprise.region.state.StateHandle;

/**
 * Sets up a SuburbPatternMatcher
 *
 * Date Started: 31/05/2008
 * <p/>
 * History:
 */
public interface SuburbPatternMatcherFactory {

    /**
     * Create a broad matcher for the specified country
     * @param iso3DigitCode
     * @return
     * @throws com.blueskyminds.homebyfive.framework.core.patterns.PatternMatcherInitialisationException
     */
    SuburbPatternMatcher create(String iso3DigitCode) throws PatternMatcherInitialisationException;

    /**
     * Create a fast matcher operating in the specified state only
     *
     * @param suburb
     * @return
     * @throws com.blueskyminds.homebyfive.framework.core.patterns.PatternMatcherInitialisationException
     */
    SuburbPatternMatcher create(StateHandle suburb) throws PatternMatcherInitialisationException;
}