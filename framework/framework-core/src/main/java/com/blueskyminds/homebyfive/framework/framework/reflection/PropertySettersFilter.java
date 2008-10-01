package com.blueskyminds.homebyfive.framework.framework.reflection;

import com.blueskyminds.homebyfive.framework.framework.tools.filters.Filter;

import java.lang.reflect.Method;

/**
 * Filters Methods of a class that are Properties and have a setter:
 *
 *   - they are not transient
 *   - they do not return a collection
 *
 * Date Started: 13/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class PropertySettersFilter implements Filter<Method> {

    private static final String SET_PREFIX = "set";

    public boolean accept(Method method) {
        return isSetter(method) && isOneArgument(method);
    }

    protected boolean isSetter(Method method) {
        String name = method.getName();
        return name.startsWith(SET_PREFIX);
    }

    /**
     * Returns true if the method is not annotated as Transient
     *
     * @param method
     * @return
     */
    protected boolean isOneArgument(Method method) {
        return method.getParameterTypes().length == 1;
    }
}