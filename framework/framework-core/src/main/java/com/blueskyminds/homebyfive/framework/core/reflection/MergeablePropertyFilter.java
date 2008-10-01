package com.blueskyminds.homebyfive.framework.core.reflection;

import com.blueskyminds.homebyfive.framework.core.tools.filters.Filter;
import com.blueskyminds.homebyfive.framework.core.IgnoreMerge;

import java.lang.reflect.Method;

/**
 * Filters Methods that are Properties that can be used in the DomainObject merge operation
 * Properties can be merged automatically if:
 *   - they are not transient
 * ' - they are not annotated with IgnoreMerge
 *   - they do not return a collection
 *
 * Collections need to be merged manually
 *
 * Date Started: 6/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class MergeablePropertyFilter extends SimplePropertyFilter implements Filter<Method> {

    /**
     * Overrides the default implemetation to also examine the IgnoreMerge annotation
     *
     * @param method
     * @return
     */
    @Override
    protected boolean isNotAnnotated(Method method) {

        boolean notTransientFlag = super.isNotAnnotated(method);

        IgnoreMerge ignoreMerge = method.getAnnotation(IgnoreMerge.class);
        boolean notIgnoreMerge = (ignoreMerge == null) || (!ignoreMerge.enabled());

        return (notTransientFlag && notIgnoreMerge);
    }


}
