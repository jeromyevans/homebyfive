package com.blueskyminds.enterprise.address.patterns;

import com.blueskyminds.framework.patterns.*;
import com.blueskyminds.framework.patterns.comparison.IgnoreCaseComparator;
import com.blueskyminds.enterprise.address.dao.AddressDAO;
import com.blueskyminds.enterprise.address.StreetType;
import com.blueskyminds.enterprise.region.state.StateHandle;
import com.blueskyminds.enterprise.region.country.CountryHandle;
import com.blueskyminds.enterprise.region.suburb.SuburbHandle;

import java.util.Set;
import java.util.List;
import java.util.LinkedList;

import org.apache.commons.lang.StringUtils;

/**
 * Matches words to suburb names within a State or Country
 *
 *
 * Date Started: 18/06/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class SuburbNameBin extends NamedBin {

    private StateHandle state;

    /**
     * Extends the default implementation to ensure that suburb names start with a letter
     *
     * @param word
     * @return true if an exclusion was matched
     */
    protected boolean wordMatchesExclusion(String word) {
        if (StringUtils.isNumericSpace(word)) {
        //if (!Character.isLetter(word.charAt(0))) {
            return true;
        } else {
            return super.wordMatchesExclusion(word);
        }
    }

    /**
     * Limit the suburbs to a specific state
     *
     * @param state
     * @param addressDAO
     * @throws PatternMatcherInitialisationException
     */
    public SuburbNameBin(StateHandle state, AddressDAO addressDAO) throws PatternMatcherInitialisationException {
        this.state = state;

        // add all the street types as exclusions
        addExclusion(StreetType.asList(), new IgnoreCaseComparator());
        addNamedGroupByMetaphone(addressDAO.listSuburbsInState(state));
    }

    /**
     * All suburbs in a country
     *
     * @param addressDAO
     * @throws PatternMatcherInitialisationException
     */
    public SuburbNameBin(CountryHandle country, AddressDAO addressDAO) throws PatternMatcherInitialisationException {
        // add all the street types as exclusions
        addExclusion(StreetType.asList(), new IgnoreCaseComparator());
        addNamedGroupByMetaphone(addressDAO.listSuburbsInCountry(country));
    }


    /**
     * The suburb names are grouped by their double metaphone.
     * Calculate which group this input string belongs to
     *
     * @param inputString
     * @return
     */
    @Override
    protected Object calculateGroup(String inputString) {
        return LevensteinDistanceTools.calculateDoubleMetaphone(inputString);
    }


    /**
     * Get a list of all the suburbs that the address could be for - where a phrase allocation has been
     * made to the suburb
     *
     * @return list of candidate suburbs
     */
    public List<SuburbHandle> getCandidateSuburbs() {
        List<SuburbHandle> suburbs = new LinkedList<SuburbHandle>();
        for (PhraseToBinAllocation allocation : getAllocations()) {
            SuburbHandle suburb = (SuburbHandle) allocation.getPattern().getMetadata();
            if (!suburbs.contains(suburb)) {
                suburbs.add(suburb);
            }
        }
        return suburbs;
    }
    /** Equality using the state */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final SuburbNameBin that = (SuburbNameBin) o;

        if (state != null ? !state.equals(that.state) : that.state != null) return false;

        return true;
    }

    public int hashCode() {
        return (state != null ? state.hashCode() : 0);
    }
}