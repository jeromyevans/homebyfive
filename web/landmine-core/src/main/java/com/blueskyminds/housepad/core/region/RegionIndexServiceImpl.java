package com.blueskyminds.housepad.core.region;

import com.blueskyminds.housepad.core.region.indexes.CountryIndex;

/**
 * Date Started: 4/01/2008
 * <p/>
 * History:
 */
public class RegionIndexServiceImpl implements RegionIndexService {

    private CountryIndex countryIndex;

    public RegionIndexServiceImpl() {
        countryIndex = new CountryIndex();
    }

    public Long getCountryId(String countryKey) {
        return countryIndex.get(countryKey);
    }

    public Long getStateId(String countryKey, String stateKey) {
        return countryIndex.getState(countryKey, stateKey);
    }

    public Long getSuburbId(String countryKey, String stateKey, String suburbKey) {
        return countryIndex.getSuburb(countryKey, stateKey, suburbKey);
    }

    public Long getPostCodeId(String countryKey, String stateKey, String postCodeKey) {
        return countryIndex.getSuburb(countryKey, stateKey, postCodeKey);
    }

    public Long getStreetId(String countryKey, String stateKey, String suburbKey, String streetKey) {
        return countryIndex.getStreet(countryKey, stateKey, suburbKey, streetKey);
    }
}
