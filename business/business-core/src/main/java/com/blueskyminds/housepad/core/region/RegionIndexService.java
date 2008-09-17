package com.blueskyminds.housepad.core.region;

/**
 * Provides indexes to the various types of regions
 *
 * Date Started: 4/01/2008
 * <p/>
 * History:
 */
public interface RegionIndexService {
    
    Long getCountryId(String countryKey);

    Long getStateId(String countryKey, String stateKey);

    Long getSuburbId(String countryKey, String stateKey, String suburbKey);

    Long getPostCodeId(String countryKey, String stateKey, String postCodeKey);

    Long getStreetId(String countryKey, String stateKey, String suburbKey, String streetKey);
}
