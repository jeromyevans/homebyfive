package com.blueskyminds.homebyfive.business.address.patterns;

import com.blueskyminds.homebyfive.business.region.graph.Suburb;

/**
 * Creates an AddressParser within the specified context
 *
 * Date Started: 18/03/2008
 * <p/>
 * History:
 */
public interface AddressParserFactory {

//    /**
//     * Create a broad matcher for the specified country
//     * @param iso3DigitCode
//     * @return
//     * @throws PatternMatcherInitialisationException
//     */
//    AddressPatternMatcher create(String iso3DigitCode) throws PatternMatcherInitialisationException;
//
//    /**
//     * Create a fast matcher operating in the specified suburb only
//     *
//     * @param suburb
//     * @return
//     * @throws PatternMatcherInitialisationException
//     */
//    AddressPatternMatcher create(SuburbHandle suburb) throws PatternMatcherInitialisationException;

     /**
     * Create a parser for the specified country
     *
     * @param iso3DigitCode
     * @return
     */
    AddressParser create(String iso3DigitCode);

    /**
     * Create a parser constrainted to the specified suburb.
     *
     * @param suburb
     * @return
     */
    AddressParser create(Suburb suburb);
}
