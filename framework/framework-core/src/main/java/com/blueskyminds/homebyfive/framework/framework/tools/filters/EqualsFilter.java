package com.blueskyminds.homebyfive.framework.framework.tools.filters;

/**
 * A filter that accepts a string if it equals a certain value
 *
 * Date Started: 2/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class EqualsFilter implements StringFilter {

    private String value;
    private boolean ignoreCase = false;

    public EqualsFilter(String value) {
        this.value = value;
    }

    public EqualsFilter(String value, boolean ignoreCase) {
        this.value = value;
        this.ignoreCase = ignoreCase;
    }

    public boolean accept(String string) {
        if (value == null) {
            return (string == null);
        } else {
            if (!ignoreCase) {
                return value.equals(string);
            } else {
                return value.equalsIgnoreCase(string);
            }
        }
    }

    // ------------------------------------------------------------------------------------------------------
}
