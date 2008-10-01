package com.blueskyminds.homebyfive.framework.framework.tools.filters;

import org.apache.commons.lang.StringUtils;

/**
 * Filter that accepts non-blank strings
 *
 * Date Started: 1/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class NonBlankFilter implements StringFilter {

    public boolean accept(String string) {
        return StringUtils.isNotBlank(string);
    }
}
