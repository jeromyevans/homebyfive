package com.blueskyminds.homebyfive.framework.framework.jpa;

import java.lang.annotation.*;

/**
 * Designates a method that requires a UserTransaction
 *
 * Date Started: 8/08/2007
 * <p/>
 * History:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Transactional {
}
