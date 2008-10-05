package com.blueskyminds.enterprise.region;

import com.blueskyminds.enterprise.region.graph.*;
import com.blueskyminds.enterprise.region.index.*;
import com.blueskyminds.enterprise.address.StreetSection;
import com.blueskyminds.enterprise.address.StreetType;

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
    public Country createCountry(String name, String iso2DigitCode, String iso3CountryCode, String currencyCode) {
        Country countryHandle = new Country(name, iso2DigitCode, iso3CountryCode);
        CountryBean countryBean = new CountryBean(countryHandle);
        countryHandle.setRegionIndex(countryBean);
        return countryHandle;
    }

    /**
     * Create a new country instance with the specified name
     *
     * @param name
     * @return
     */
    public Country createCountry(String name, String iso2DigitCode) {
        Country countryHandle = new Country(name, iso2DigitCode);
        CountryBean countryBean = new CountryBean(countryHandle.getName(), countryHandle.getAbbr());
        countryBean.setCountryHandle(countryHandle);
        countryHandle.setRegionIndex(countryBean);
        return countryHandle;
    }

    public PostalCode createPostCode(String postCodeValue, State state) {
        PostalCode postCodeHandle = new PostalCode(state, postCodeValue);
        PostalCodeBean postalCodeBean = new PostalCodeBean(postCodeHandle);
        postCodeHandle.setRegionIndex(postalCodeBean);
        return postCodeHandle;
    }

    public State createState(String fullName, String abbreviation, Country country) {
        State stateHandle = new State(country, fullName, abbreviation);
        StateBean stateBean = new StateBean(stateHandle);
        stateHandle.setRegionIndex(stateBean);
        return stateHandle;
    }

    public Suburb createSuburb(String name, State state) {
        Suburb suburbHandle = new Suburb(state, null, name);
        SuburbBean suburbBean = new SuburbBean(suburbHandle);
        suburbHandle.setRegionIndex(suburbBean);
        return suburbHandle;
    }

    public Suburb createSuburb(String name, State state, PostalCode postCode) {
        Suburb suburbHandle = createSuburb(name, state);
        suburbHandle.addParentRegion(postCode);
        SuburbBean suburbBean = new SuburbBean(suburbHandle);
        suburbHandle.setRegionIndex(suburbBean);
        return suburbHandle;
    }

    public Street createStreet(String name, StreetType streetType, StreetSection section, Suburb suburb) {
        Street streetHandle = new Street(name, streetType, section);
        streetHandle.addParentRegion(suburb);
        StreetBean streetBean = new StreetBean(streetHandle);
        streetHandle.setRegionIndex(streetBean);
        return streetHandle;
    }
}
