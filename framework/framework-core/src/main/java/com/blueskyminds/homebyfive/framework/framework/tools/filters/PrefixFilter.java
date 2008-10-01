package com.blueskyminds.homebyfive.framework.framework.tools.filters;

import org.apache.commons.lang.StringUtils;

/**
 * Accepts strings that don't start with the given prefix (ignoring whitespace)
 *
 * Date Started: 13/03/2008
 * <p/>
 * History:
 */
public class PrefixFilter implements StringFilter {

    private String prefix;

    public PrefixFilter(String prefix) {
        this.prefix = prefix;
    }

    public boolean accept(String string) {
        return !(StringUtils.trim(string).startsWith(prefix));
    }
}
