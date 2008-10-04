package com.blueskyminds.housepad.core.region.service;

import com.blueskyminds.enterprise.region.RegionBean;

/**
 * Exception indicates that an attempt was made to create a new region with invalid properties
 *
 * Date Started: 8/03/2008
 * <p/>
 * History:
 */
public class InvalidRegionException extends RegionException {
    private static final long serialVersionUID = 672848634018856753L;

    public InvalidRegionException(RegionBean regionBean) {
        super(InvalidRegionException.class.getSimpleName()+":"+regionBean.getPath());
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