package com.blueskyminds.enterprise.region.service;

import com.blueskyminds.enterprise.region.index.RegionBean;

/**
 * Exception indicates that an attempt was made to create a new region when one already has the same path
 *
 * Date Started: 7/03/2008
 * <p/>
 * History:
 */
public class DuplicateRegionException extends RegionException {
    private static final long serialVersionUID = -5066604517461694446L;

    public DuplicateRegionException(RegionBean regionBean) {
        super(DuplicateRegionException.class.getSimpleName()+":"+regionBean.getPath());
    }
    
    public DuplicateRegionException(String message) {
        super(message);
    }

    public DuplicateRegionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateRegionException(Throwable cause) {
        super(cause);
    }
}
