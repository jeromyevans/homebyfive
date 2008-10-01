package com.blueskyminds.homebyfive.framework.framework.tools.filters;

/**
 * A filter used to accept a string or not
 *
 * Date Started: 1/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public interface StringFilter extends Filter<String> {
    
    boolean accept(String string);
}
