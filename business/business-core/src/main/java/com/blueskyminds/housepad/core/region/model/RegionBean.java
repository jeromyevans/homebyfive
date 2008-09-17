package com.blueskyminds.housepad.core.region.model;

import com.blueskyminds.enterprise.region.RegionHandle;
import com.blueskyminds.enterprise.region.RegionTypes;
import com.blueskyminds.framework.DomainObjectStatus;

/**
 * Date Started: 3/03/2008
 * <p/>
 * History:
 */
public interface RegionBean {

    RegionBean getParent();
    String getParentPath();
    String getPath();

    /** Full human-readable name */
    String getName();
    
    /**
     * Merge this region with another region of the same type
     * 
     * This operation cannot be reversed
     *
     * @param regionBean
     */
    void mergeWith(RegionBean regionBean);

    RegionHandle getRegionHandle();

    void setStatus(DomainObjectStatus status);

    RegionTypes getType();
}
