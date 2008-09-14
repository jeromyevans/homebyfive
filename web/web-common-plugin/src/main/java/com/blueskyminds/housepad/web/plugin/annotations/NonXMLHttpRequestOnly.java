package com.blueskyminds.housepad.web.plugin.annotations;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation for action class to indicate that it can only be accessed via non-XMLHttpRequests
 * Can be overridden by method annotations:
 * @see DenyXMLHttpRequest
 * @see AllowXMLHttpRequest
 * Date Started: 13/05/2008
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NonXMLHttpRequestOnly {
}