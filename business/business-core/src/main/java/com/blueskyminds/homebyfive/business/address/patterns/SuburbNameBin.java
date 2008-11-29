package com.blueskyminds.homebyfive.business.address.patterns;

import com.blueskyminds.homebyfive.framework.core.patterns.*;
import com.blueskyminds.homebyfive.framework.core.patterns.comparison.IgnoreCaseComparator;
import com.blueskyminds.homebyfive.business.address.dao.AddressDAO;
import com.blueskyminds.homebyfive.business.address.StreetType;
import com.blueskyminds.homebyfive.business.region.graph.State;
import com.blueskyminds.homebyfive.business.region.graph.Country;
import com.blueskyminds.homebyfive.business.region.graph.Suburb;
import com.blueskyminds.homebyfive.business.region.service.SuburbService;

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

    private State state;

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
     * @param suburbService
     * @throws PatternMatcherInitialisationException
     */
    public SuburbNameBin(State state, SuburbService suburbService) throws PatternMatcherInitialisationException {
        this.state = state;

        // add all the street types as exclusions
        addExclusion(StreetType.asList(), new IgnoreCaseComparator());
        addNamedGroupByMetaphone(suburbService.listSuburbs(state));
    }

    /**
     * All suburbs in a country
     *
     * @param suburbService
     * @throws PatternMatcherInitialisationException
     */
    public SuburbNameBin(Country country, SuburbService suburbService) throws PatternMatcherInitialisationException {
        // add all the street types as exclusions
        addExclusion(StreetType.asList(), new IgnoreCaseComparator());
        addNamedGroupByMetaphone(suburbService.listSuburbs(country));
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
    public List<Suburb> getCandidateSuburbs() {
        List<Suburb> suburbs = new LinkedList<Suburb>();
        for (PhraseToBinAllocation allocation : getAllocations()) {
            Suburb suburb = (Suburb) allocation.getPattern().getMetadata();
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