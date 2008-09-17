package com.blueskyminds.housepad.core.region.indexes;

/**
 * A specialisation of the RegionIndex for PostCodes
 * 
 * Date Started: 4/01/2008
 * <p/>
 * History:
 */
public class PostCodeIndex extends RegionIndex {

    public Long getPostCode(String postCodeKey) {
        return get(postCodeKey);
    }
}
