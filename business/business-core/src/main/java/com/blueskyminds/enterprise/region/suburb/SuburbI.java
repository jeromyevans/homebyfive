package com.blueskyminds.enterprise.region.suburb;

import com.blueskyminds.enterprise.region.street.StreetHandle;

/**
 * Date Started: 9/10/2007
 * <p/>
 * History:
 */
public interface SuburbI {

    /** Associate the specified street with this suburb */
    StreetHandle addStreet(StreetHandle street);
}
