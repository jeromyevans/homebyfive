package com.blueskyminds.housepad.core.region.indexes;

import com.blueskyminds.enterprise.address.dao.AddressDAO;
import com.blueskyminds.enterprise.region.country.CountryHandle;
import com.blueskyminds.enterprise.region.state.StateHandle;
import com.blueskyminds.enterprise.region.state.State;
import com.blueskyminds.enterprise.region.suburb.SuburbHandle;
import com.blueskyminds.enterprise.region.postcode.PostCodeHandle;
import com.blueskyminds.housepad.core.region.RegionTools;
import com.google.inject.Inject;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;

/**
 * Date Started: 4/01/2008
 * <p/>
 * History:
 */
public class CountryIndex extends RegionIndex {

    private Map<String, StateIndex> countryStateIndex;

    public CountryIndex() {
        countryStateIndex = new HashMap<String, StateIndex>();
    }

    /**
     * Get the id of the specified Country
     */
    public Long getCountry(String countryKey) {
        return get(countryKey);
    }

    /**
     * Get the id of the specified state
     */
    public Long getState(String countryKey, String stateKey) {
        StateIndex stateIndex = countryStateIndex.get(countryKey);
        if (stateIndex != null) {
            return stateIndex.getState(stateKey);
        }
        return null;
    }

    /** Adds a new state to this country index.
     *
     * @param stateKey
     * @param stateId
     * @return the corresponding StateIndex
     */
    public StateIndex addState(String countryKey, String stateKey, Long stateId) {
        StateIndex stateIndex = countryStateIndex.get(countryKey);
        if (stateIndex == null) {
            stateIndex = new StateIndex();
            countryStateIndex.put(countryKey, stateIndex);
        }

        stateIndex.add(stateKey, stateId);

        return stateIndex;
    }

    public void addStateIndex(String countryKey, StateIndex stateIndex) {
        countryStateIndex.put(countryKey, stateIndex);
    }

    /**
     * Get the id of the specified suburb
     */
    public Long getSuburb(String countryKey, String stateKey, String suburbKey) {
        StateIndex stateIndex = countryStateIndex.get(countryKey);
        if (stateIndex != null) {
            return stateIndex.getSuburb(stateKey, suburbKey);
        }
        return null;
    }

    /**
     * Get the id of the specified postcode
     */
    public Long getPostCode(String countryKey, String stateKey, String postCodeKey) {
        StateIndex stateIndex = countryStateIndex.get(countryKey);
        if (stateIndex != null) {
            return stateIndex.getPostCode(stateKey, postCodeKey);
        }
        return null;
    }

    /**
     * Get the id of the specified street
     */
    public Long getStreet(String countryKey, String stateKey, String suburbKey, String streetKey) {
        StateIndex stateIndex = countryStateIndex.get(countryKey);
        if (stateIndex != null) {
            return stateIndex.getStreet(stateKey, suburbKey, streetKey);
        }
        return null;
    }

    /** The Factory is used to create a CountryIndex instance */
    public static class Factory {

        private AddressDAO addressDAO;

        public Factory() {
        }

        /** Create a CountryIndex instance for a single country */
        public CountryIndex setup(String countryCode) {
            CountryIndex countryIndex = new CountryIndex();

            CountryHandle countryHandle = addressDAO.findCountryByISO2DigitCode(countryCode);

            if (countryHandle != null) {
                countryIndex.add(countryCode, countryHandle.getId());

                Set<StateHandle> states = addressDAO.listStatesInCountry(countryHandle);
                for (StateHandle state : states) {

                    String stateKey = RegionTools.encode(((State) state.getRegion()).getAbbreviation());
                    StateIndex stateIndex = countryIndex.addState(countryCode, stateKey, state.getId());

                    Set<SuburbHandle> suburbs = addressDAO.listSuburbsInState(state);
                    for (SuburbHandle suburb : suburbs) {
                        stateIndex.addSuburb(stateKey, RegionTools.encode(suburb.getName()), suburb.getId());
                    }

                    Set<PostCodeHandle> postCodes = addressDAO.listPostCodesInState(state);
                    for (PostCodeHandle postCodeHandle : postCodes) {
                        stateIndex.addPostCode(stateKey, RegionTools.encode(postCodeHandle.getName()), postCodeHandle.getId());
                    }
                }
            }
            return countryIndex;
        }

        @Inject
        public void setAddressDAO(AddressDAO addressDAO) {
            this.addressDAO = addressDAO;
        }
    }
}
