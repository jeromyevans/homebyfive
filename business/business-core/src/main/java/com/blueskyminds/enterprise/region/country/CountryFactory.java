package com.blueskyminds.enterprise.region.country;

/**
 * Date Started: 8/07/2007
 * <p/>
 * History:
 */
public class CountryFactory {

    /**
     * Create a new country instance with the specified name and codes
     *
     * @param name
     * @return
     */
    public CountryHandle createCountry(String name, String iso2CountryCode, String iso3CountryCode, String currencyCode) {
        Country country = new Country(name, iso2CountryCode, iso3CountryCode, currencyCode);
        CountryHandle countryHandle = new CountryHandle(name, country, iso2CountryCode, iso3CountryCode);
        return countryHandle;
    }

    /**
     * Create a new country instance with the specified name
     *
     * @param name
     * @return
     */
    public CountryHandle createCountry(String name) {
        Country country = new Country(name);
        CountryHandle countryHandle = new CountryHandle(name, country);
        return countryHandle;
    }
}
