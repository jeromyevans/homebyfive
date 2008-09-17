package com.blueskyminds.housepad.core.region.indexes;

/**
 * Date Started: 4/01/2008
 * <p/>
 * History:
 */
public class StreetIndex extends RegionIndex {

    public Long getStreet(String streetKey) {
        return get(streetKey);
    }
}
