package com.blueskyminds.enterprise.region;

import com.blueskyminds.enterprise.region.graph.PostalCode;
import com.blueskyminds.enterprise.region.graph.State;
import com.blueskyminds.enterprise.region.graph.Suburb;
import com.blueskyminds.enterprise.region.graph.Country;

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
    public Country createCountry(String name, String iso2CountryCode, String iso3CountryCode, String currencyCode) {
        Country countryHandle = new Country(name, iso2CountryCode, iso3CountryCode);
        return countryHandle;
    }

    /**
     * Create a new country instance with the specified name
     *
     * @param name
     * @return
     */
    public Country createCountry(String name) {
        Country countryHandle = new Country(name);
        return countryHandle;
    }

    public PostalCode createPostCode(String postCodeValue) {
        PostalCode postCodeHandle = new PostalCode(postCodeValue);
        return postCodeHandle;
    }

    public State createState(String fullName, String abbreviation) {
        State stateHandle = new State(fullName, abbreviation);
        return stateHandle;
    }

    public Suburb createSuburb(String name) {
        Suburb suburbHandle = new Suburb(name);
        return suburbHandle;
    }

    public Suburb createSuburb(String name, PostalCode postCode) {
        Suburb suburbHandle = createSuburb(name);
        suburbHandle.addParentRegion(postCode);
        return suburbHandle;
    }
}
