package com.blueskyminds.enterprise.address.patterns;

import com.blueskyminds.homebyfive.framework.core.patterns.*;
import com.blueskyminds.enterprise.address.dao.AddressDAO;
import com.blueskyminds.enterprise.region.country.CountryHandle;
import com.blueskyminds.enterprise.region.state.StateHandle;

import java.util.*;

/**
 * Matches words to names and abbreviations of states
 *
 * Uses the state names loaded from persistence for the given country.
 * Patterns matching is fuzzy.
 * The metadata for each pattern is used to identify the name of the corresponding State instance.
 * 
 * Date Started: 18/06/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class StateBin extends NamedBin implements OrderedBin {

    /** The types of bins allowed to the left of this position */
    private static final BinType[] ALLOW_LEFT = {
        BinType.UnitNumberBin, BinType.LotNumberBin, BinType.StreetNumberBin, BinType.StreetNameBin, BinType.StreetTypeBin, BinType.StreetSectionBin, BinType.SuburbNameBin, BinType.PostCodeBin
    };

    /** The types of bins allowed to the right of this position */
    private static final BinType[] ALLOW_RIGHT = {
        BinType.PostCodeBin
    };

    public StateBin(CountryHandle country, AddressDAO addressDAO) throws PatternMatcherInitialisationException {
        addNamed(addressDAO.listStatesInCountry(country));
    }

    public BinType[] getBinTypesAllowedLeft() {
        return ALLOW_LEFT;
    }

    public BinType[] getBinTypesAllowedRight() {
        return ALLOW_RIGHT;
    }

     /**
     * Extends the default implementation to ensure that state names start with a letter
     *
     * @param word
     * @return true if an exclusion was matched
     */
    protected boolean wordMatchesExclusion(String word) {
        if (!Character.isLetter(word.charAt(0))) {
            return true;
        } else {
            return super.wordMatchesExclusion(word);
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get a list of all the states that the address could be for - where a phrase allocation has been
     * made to the state
     *
     * @return list of candidate states
     */
    public List<StateHandle> getCandidateStates() {
        List<StateHandle> states = new LinkedList<StateHandle>();
        for (PhraseToBinAllocation allocation : getAllocations()) {
            StateHandle state = (StateHandle) allocation.getPattern().getMetadata();
            if (!states.contains(state)) {
                states.add(state);
            }
        }
        return states;
    }

    // ------------------------------------------------------------------------------------------------------
}