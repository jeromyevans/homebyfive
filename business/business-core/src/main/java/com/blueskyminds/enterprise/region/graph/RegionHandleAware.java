package com.blueskyminds.enterprise.region.graph;

/**
 * A Region that includes a proxy
 *
 * Date Started: 8/07/2007
 * <p/>
 * History:
 */
public interface RegionHandleAware {

    /** Get the proxy for this Region */
    RegionHandle getRegionHandle();

    /** Inject the RegionHandle for a Region */
    void setRegionHandle(RegionHandle regionHandle);
}
