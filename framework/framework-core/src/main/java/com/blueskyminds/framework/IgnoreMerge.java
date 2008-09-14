package com.blueskyminds.framework;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

/**
 * This annotation identiies properties in an DomainObject that are ignored by the DomainObject's mergeWith operation
 *
 * Date Started: 6/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreMerge {

    /**
     * True if the mergeWith operation should ignore this property
     * @return true if the property should be ignored
     */
    boolean enabled() default true;
}
