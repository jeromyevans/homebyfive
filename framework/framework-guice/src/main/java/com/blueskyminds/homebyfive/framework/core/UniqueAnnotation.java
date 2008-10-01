package com.blueskyminds.homebyfive.framework.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Date Started: 24/10/2007
 * <p/>
 * History:
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueAnnotation  {

    static final AtomicInteger count = new AtomicInteger();
    
    int i = 0;

    String name();
}
