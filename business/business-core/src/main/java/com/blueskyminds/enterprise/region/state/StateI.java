package com.blueskyminds.enterprise.region.state;

import com.blueskyminds.enterprise.region.postcode.PostCodeHandle;
import com.blueskyminds.enterprise.region.suburb.SuburbHandle;

/**
 * Date Started: 9/10/2007
 * <p/>
 * History:
 */
public interface StateI {

    /**
     * Add a postcode to this state
     *
     * @param postCode
     * @return
     */
    PostCodeHandle addPostCode(PostCodeHandle postCode);

    /**
     * Add a suburb to this state
     *
     * @param suburb
     * @return
     */
    SuburbHandle addSuburb(SuburbHandle suburb);
}
