package com.blueskyminds.housepad.core.region.indexes;

import java.util.Map;
import java.util.HashMap;

/**
 * Provides a quick lookup of a suburbs id its key
 *
 * Date Started: 4/01/2008
 * <p/>
 * History:
 */
public abstract class RegionIndex {

    private Map<String, Long> index;

    public RegionIndex() {
        index = new HashMap<String,Long>();
    }

    /**
     * Get the id of the specified region
     */
    public Long get(String regionKey) {
        init();
        return index.get(regionKey);
    }

    private void init() {

    }

    public void add(String key, Long id) {
        index.put(key, id);
    }

}
