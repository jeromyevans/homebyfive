package com.blueskyminds.housepad.core.region.indexes;

import java.util.Map;
import java.util.HashMap;

/**
 * A specialisation of the RegionIndex that identifies the ID of each suburb
 *
 * Date Started: 4/01/2008
 * <p/>
 * History:
 */
public class SuburbIndex extends RegionIndex {

    private Map<String, StreetIndex> suburbStreetIndex;

    public SuburbIndex() {
        suburbStreetIndex = new HashMap<String, StreetIndex>();
    }

    /**
     * Get the id of the specified Suburb
     */
    public Long getSuburb(String suburbKey) {
        return get(suburbKey);
    }

    /**
     * Get the id of the specified street
     */
    public Long getStreet(String suburbKey, String streetKey) {
        StreetIndex streetIndex = suburbStreetIndex.get(suburbKey);
        if (streetIndex != null) {
            return streetIndex.getStreet(streetKey);
        }
        return null;
    }

}
