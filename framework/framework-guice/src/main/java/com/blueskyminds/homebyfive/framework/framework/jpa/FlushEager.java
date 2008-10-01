package com.blueskyminds.homebyfive.framework.framework.jpa;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Designates a method that requires a Flush after the invocation
 *
 * Date Started: 8/03/2008
 * <p/>
 * History:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface FlushEager {
}
