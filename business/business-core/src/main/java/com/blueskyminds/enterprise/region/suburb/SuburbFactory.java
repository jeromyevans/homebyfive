package com.blueskyminds.enterprise.region.suburb;

import com.blueskyminds.enterprise.region.postcode.PostCodeHandle;

/**
 * Date Started: 8/07/2007
 * <p/>
 * History:
 */
public class SuburbFactory {

    public SuburbHandle createSuburb(String name) {
        Suburb suburb = new Suburb(name);
        SuburbHandle suburbHandle = new SuburbHandle(name, suburb);
        return suburbHandle;
    }

    public SuburbHandle createSuburb(String name, PostCodeHandle postCode) {
        SuburbHandle suburbHandle = createSuburb(name);
        suburbHandle.addParentRegion(postCode);
        return suburbHandle;
    }
}
