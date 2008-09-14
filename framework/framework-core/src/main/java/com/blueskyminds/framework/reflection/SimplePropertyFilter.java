package com.blueskyminds.framework.reflection;

import com.blueskyminds.framework.tools.filters.Filter;

import javax.persistence.Transient;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * Filters Methods of a class that are Properties and:
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
public class SimplePropertyFilter implements Filter<Method> {

    private static final String GET_PREFIX = "get";
    private static final String IS_PREFIX = "is";
    private static final String GET_CLASS_PREFIX = "getClass";

    public boolean accept(Method method) {
        return isProperty(method) && isNotAnnotated(method) && !returnsCollection(method);
    }

    protected boolean isProperty(Method method) {
        String name = method.getName();
        return (name.startsWith(GET_PREFIX) && (!name.startsWith(GET_CLASS_PREFIX))) || name.startsWith(IS_PREFIX);
    }

    /**
     * Returns true if the method is not annotated as Transient
     *
     * @param method
     * @return
     */
    protected boolean isNotAnnotated(Method method) {
        return method.getAnnotation(Transient.class) == null;
    }

    protected boolean returnsCollection(Method method) {
        return Collection.class.isAssignableFrom(method.getReturnType());
    }


    // ------------------------------------------------------------------------------------------------------
}
