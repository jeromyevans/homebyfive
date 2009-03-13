package com.blueskyminds.homebyfive.framework.core.guice.providers;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotion where the JNDI Environment Context should be injected
 *
 * Date Started: 08/03/2009
 */
@Retention(RetentionPolicy.RUNTIME)
@BindingAnnotation
public @interface EnvContext {
}
