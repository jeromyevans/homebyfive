package com.blueskyminds.enterprise.region;

import com.blueskyminds.homebyfive.framework.framework.tools.Named;
import com.blueskyminds.enterprise.region.RegionTypes;

/**
 * Date Started: 7/07/2007
 * <p/>
 * History:
 */
public interface Region extends Named {

     /**
     * Add a subregion to this region
     *   The other region is not updated to point back to this parent
     *
     * @param childRegion
     * @return true if added ok
     */
    boolean addChildRegion(Region childRegion);

    /**
     * Add the other region as a parent of this region.
     *  The other region is not updated to point to this child
     *
     * @param parentRegion
     * @return true if added ok
     */
    boolean addParentRegion(Region parentRegion);
        
    RegionTypes getType();
   
}
