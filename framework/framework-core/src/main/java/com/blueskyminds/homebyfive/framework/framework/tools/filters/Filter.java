package com.blueskyminds.homebyfive.framework.framework.tools.filters;

/**
 * General purpose pass-only filter
 *
 * Date Started: 2/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public interface Filter<O> {

    /**
     * Determines whether the object can be accepted
     *
     * @param object   object to check
     * @return true if the object is accepted by the filter
     */
    boolean accept(O object);
}
