package com.blueskyminds.homebyfive.business.region.service;

import com.blueskyminds.homebyfive.business.region.index.RegionIndex;
import com.blueskyminds.homebyfive.business.region.graph.Region;

/**
 * Exception indicates that an attempt was made to create a new region with invalid properties
 *
 * Date Started: 8/03/2008
 * <p/>
 * History:
 */
public class InvalidRegionException extends RegionException {
    private static final long serialVersionUID = 672848634018856753L;

    public InvalidRegionException(RegionIndex regionBean) {
        super(InvalidRegionException.class.getSimpleName()+":"+regionBean.getPath());
    }

    public InvalidRegionException(Region region) {
        super(region.getClass().getSimpleName()+": "+region.getPath());
    }

    public InvalidRegionException(String message, Region region) {
        super(InvalidRegionException.class.getSimpleName()+":"+region.getPath()+" - "+message);
    }

    public InvalidRegionException(String message) {
        super(message);
    }

    public InvalidRegionException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidRegionException(Throwable cause) {
        super(cause);
    }
}