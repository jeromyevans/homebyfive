package com.blueskyminds.housepad.core.region.indexes;

import java.util.Map;
import java.util.HashMap;

/**
 * Quick index to the suburbs and postcodes in a state
 *
 * Date Started: 4/01/2008
 * <p/>
 * History:
 */
public class StateIndex extends RegionIndex {

    /** References a SuburbIndex for each state */
    private Map<String, SuburbIndex> stateSuburbIndex;
    /** References a PostcodeIndex for each state */
    private Map<String, PostCodeIndex> statePostCodeIndex;

    public StateIndex() {
        stateSuburbIndex = new HashMap<String, SuburbIndex>();
        statePostCodeIndex = new HashMap<String, PostCodeIndex>();
    }

    /**
     * Get the id of the specified State
     */
    public Long getState(String stateKey) {
        return get(stateKey);
    }

    /**
     * Get the id of the suburb in the specified state
     */
    public Long getSuburb(String stateKey, String suburbKey) {
        SuburbIndex suburbIndex = stateSuburbIndex.get(stateKey);
        if (suburbIndex != null) {
            return suburbIndex.getSuburb(suburbKey);
        }

        return null;
    }

    /** Adds a new suburb to this state index.
     *
     * @param suburbKey
     * @param suburbId
     * @return the corresponding SuburbIndex
     */
    public SuburbIndex addSuburb(String stateKey, String suburbKey, Long suburbId) {
        SuburbIndex suburbIndex = stateSuburbIndex.get(stateKey);
        if (suburbIndex == null) {
            suburbIndex = new SuburbIndex();
            stateSuburbIndex.put(stateKey, suburbIndex);
        }

        suburbIndex.add(suburbKey, suburbId);

        return suburbIndex;
    }

    /**
     * Get the id of the postcode in the specified state
     */
    public Long getPostCode(String stateKey, String postCodeKey) {
        PostCodeIndex postCodeIndex = statePostCodeIndex.get(stateKey);
        if (postCodeIndex != null) {
            return postCodeIndex.getPostCode(postCodeKey);
        }

        return null;
    }

    /** Adds a new postcode to this state index.
     *
     * @param postCodeKey
     * @param postCodeId
     * @return the corresponding PostCodeIndex
     */
    public PostCodeIndex addPostCode(String stateKey, String postCodeKey, Long postCodeId) {
        PostCodeIndex postCodeIndex = statePostCodeIndex.get(stateKey);
        if (postCodeIndex == null) {
            postCodeIndex = new PostCodeIndex();
            statePostCodeIndex.put(stateKey, postCodeIndex);
        }

        postCodeIndex.add(postCodeKey, postCodeId);

        return postCodeIndex;
    }

    /**
     * Get the id of the specified street
     */
    public Long getStreet(String stateKey, String suburbKey, String streetKey) {
        SuburbIndex suburbIndex = stateSuburbIndex.get(stateKey);
        if (suburbIndex != null) {
            return suburbIndex.getStreet(suburbKey, streetKey);
        }
        return null;
    }
}
