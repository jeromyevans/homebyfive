package com.blueskyminds.framework;

import com.google.inject.name.Named;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
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
