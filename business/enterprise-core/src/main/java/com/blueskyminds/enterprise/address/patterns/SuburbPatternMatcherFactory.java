package com.blueskyminds.enterprise.address.patterns;

import com.blueskyminds.framework.patterns.PatternMatcherInitialisationException;
import com.blueskyminds.enterprise.region.suburb.SuburbHandle;
import com.blueskyminds.enterprise.region.state.StateHandle;

import java.util.Collection;

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
     * @throws com.blueskyminds.framework.patterns.PatternMatcherInitialisationException
     */
    SuburbPatternMatcher create(String iso3DigitCode) throws PatternMatcherInitialisationException;

    /**
     * Create a fast matcher operating in the specified state only
     *
     * @param suburb
     * @return
     * @throws com.blueskyminds.framework.patterns.PatternMatcherInitialisationException
     */
    SuburbPatternMatcher create(StateHandle suburb) throws PatternMatcherInitialisationException;
}