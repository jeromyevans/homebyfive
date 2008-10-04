package com.blueskyminds.enterprise.region;

import com.blueskyminds.enterprise.region.postcode.PostCodeHandle;
import com.blueskyminds.enterprise.region.state.StateHandle;
import com.blueskyminds.enterprise.region.suburb.SuburbHandle;
import com.blueskyminds.enterprise.region.country.CountryHandle;

/**
 * Date Started: 8/07/2007
 * <p/>
 * History:
 */
public class RegionFactory {

    /**
     * Create a new country instance with the specified name and codes
     *
     * @param name
     * @return
     */
    public CountryHandle createCountry(String name, String iso2CountryCode, String iso3CountryCode, String currencyCode) {
        CountryHandle countryHandle = new CountryHandle(name, iso2CountryCode, iso3CountryCode);
        return countryHandle;
    }

    /**
     * Create a new country instance with the specified name
     *
     * @param name
     * @return
     */
    public CountryHandle createCountry(String name) {
        CountryHandle countryHandle = new CountryHandle(name);
        return countryHandle;
    }

    public PostCodeHandle createPostCode(String postCodeValue) {
        PostCodeHandle postCodeHandle = new PostCodeHandle(postCodeValue);
        return postCodeHandle;
    }

    public StateHandle createState(String fullName, String abbreviation) {
        StateHandle stateHandle = new StateHandle(fullName, abbreviation);
        return stateHandle;
    }

    public SuburbHandle createSuburb(String name) {
        SuburbHandle suburbHandle = new SuburbHandle(name);
        return suburbHandle;
    }

    public SuburbHandle createSuburb(String name, PostCodeHandle postCode) {
        SuburbHandle suburbHandle = createSuburb(name);
        suburbHandle.addParentRegion(postCode);
        return suburbHandle;
    }
}
