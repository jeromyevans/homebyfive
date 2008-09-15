package com.blueskyminds.enterprise.region.country;

import com.blueskyminds.enterprise.region.state.StateHandle;

/**
 * Date Started: 9/10/2007
 * <p/>
 * History:
 */
public interface CountryI {

    /**
     * Add the specified state to this country
     *
     * @param state
     */
    StateHandle addState(StateHandle state); 
}
