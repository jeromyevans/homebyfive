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
    public Country createCountry(String name, String iso2CountryCode, String iso3CountryCode, String currencyCode) {
        Country countryHandle = new Country(name, iso2CountryCode, iso3CountryCode);
        CountryBean countryBean = new CountryBean(countryHandle);
        countryHandle.addRegionBean(countryBean);
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
        CountryBean countryBean = new CountryBean(countryHandle.getName(), countryHandle.getAbbreviation());
        countryBean.setCountryHandle(countryHandle);
        countryHandle.addRegionBean(countryBean);
        return countryHandle;
    }

    public PostalCode createPostCode(String postCodeValue) {
        PostalCode postCodeHandle = new PostalCode(postCodeValue);
        PostalCodeBean postalCodeBean = new PostalCodeBean(postCodeHandle);
        postCodeHandle.addRegionBean(postalCodeBean);
        return postCodeHandle;
    }

    public State createState(String fullName, String abbreviation) {
        State stateHandle = new State(fullName, abbreviation);
        StateBean stateBean = new StateBean(stateHandle);
        stateHandle.addRegionBean(stateBean);
        return stateHandle;
    }

    public Suburb createSuburb(String name) {
        Suburb suburbHandle = new Suburb(name);
        SuburbBean suburbBean = new SuburbBean(suburbHandle);
        suburbHandle.addRegionBean(suburbBean);
        return suburbHandle;
    }

    public Suburb createSuburb(String name, PostalCode postCode) {
        Suburb suburbHandle = createSuburb(name);
        suburbHandle.addParentRegion(postCode);
        SuburbBean suburbBean = new SuburbBean(suburbHandle);
        suburbHandle.addRegionBean(suburbBean);
        return suburbHandle;
    }

    public Street createStreet(String name, StreetType streetType, StreetSection section, Suburb suburb) {
        Street streetHandle = new Street(name, streetType, section);
        streetHandle.addParentRegion(suburb);
        StreetBean streetBean = new StreetBean(streetHandle);
        streetHandle.addRegionBean(streetBean);
        return streetHandle;
    }
}
