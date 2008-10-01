package com.blueskyminds.enterprise.address.patterns;

import com.blueskyminds.homebyfive.framework.core.patterns.PatternMatcherInitialisationException;
import com.blueskyminds.homebyfive.framework.core.patterns.NamedBin;
import com.blueskyminds.enterprise.region.country.CountryHandle;

/**
 *
 * Matches words to the country name/abbreviations
 * Matches for the country name and iso abbreviations is fuzzy
 *
 * Date Started: 18/06/2006
 *
 * History:
 * 
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class CountryBin extends NamedBin {
       
    public CountryBin(CountryHandle country) throws PatternMatcherInitialisationException {
        if (country != null) {
            addNamed(country);
        } else {
            throw new PatternMatcherInitialisationException("Country is null");
        }
    }

    /**
     * Extends the default implementation to ensure that country names start with a letter
     *
     * @param word
     * @return true if an exclusion was matched
     */
    protected boolean wordMatchesExclusion(String word) {
        if ((word != null) && (word.length() > 0)) {
            if (!Character.isLetter(word.charAt(0))) {
                return true;
            } else {
                return super.wordMatchesExclusion(word);
            }
        } else {
            return super.wordMatchesExclusion(word);
        }
    }

}
