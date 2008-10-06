package com.blueskyminds.homebyfive.web.core.annotations;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Method-level annotation for Actions to control whether the method can be invoked by an non-XMLHttpRequest
 * Takes precedence over the class-level annotation.
 *
 * @see com.blueskyminds.homebyfive.web.core.annotations.AllowXMLHttpRequest
 * @see com.blueskyminds.homebyfive.web.core.annotations.NonXMLHttpRequestOnly
 * @see com.blueskyminds.homebyfive.web.core.annotations.XMLHttpRequestOnly
 *
 * Date Started: 13/05/2008
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DenyNonXMLHttpRequest {
}