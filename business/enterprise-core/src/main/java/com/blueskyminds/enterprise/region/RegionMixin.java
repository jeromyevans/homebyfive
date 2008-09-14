package com.blueskyminds.enterprise.region;

import com.blueskyminds.enterprise.region.RegionTypes;

/**
 * Implements the default behaviour of the Region aspect
 *
 * Date Started: 7/07/2007
 * <p/>
 * History:
 */
public class RegionMixin implements Region, RegionHandleAware {

    private RegionHandle regionHandle;
    private RegionTypes type;

    //public RegionMixin(RegionHandle regionHandle, RegionTypes type) {
    public RegionMixin(RegionTypes type) {
        this.type = type;
    }

    public RegionMixin() {
    }

    public RegionHandle getRegionHandle() {
        return regionHandle;
    }

    public void setRegionHandle(RegionHandle regionHandle) {
        this.regionHandle = regionHandle;
    }

    public RegionTypes getType() {
        return type;
    }

    public void setType(RegionTypes type) {
        this.type = type;
    }

    public String getName() {
        if (regionHandle != null) {
            return regionHandle.getName();
        } else {
            return null;
        }
    }

    public void setName(String name) {
        if (regionHandle != null) {
            regionHandle.setName(name);
        }
    }

    /**
     * Add a subregion to this region
     * The other region is not updated to point back to this parent
     *
     * @param childRegion
     * @return true if added ok
     */
    public boolean addChildRegion(Region childRegion) {
        return regionHandle.addChildRegion(childRegion);
    }

    /**
     * Add the other region as a parent of this region.
     * The other region is not updated to point to this child
     *
     * @param parentRegion
     * @return true if added ok
     */
    public boolean addParentRegion(Region parentRegion) {
        return regionHandle.addParentRegion(parentRegion);
    }
}
