package com.blueskyminds.framework.tools.filters;

/**
 * A filter that does nothing (all pass)
 *
 * Date Started: 10/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class AllPassFilter<T> implements Filter<T> {

    public boolean accept(T object) {
        return true;
    }    
}
