package com.blueskyminds.enterprise.region.suburb;

import com.blueskyminds.enterprise.address.Street;

/**
 * Date Started: 9/10/2007
 * <p/>
 * History:
 */
public interface SuburbI {

    /** Associate the specified street with this suburb */
    Street addStreet(Street street);
}
