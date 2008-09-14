package com.blueskyminds.housepad.web.plugin.annotations;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Method-level annotation for Actions to control whether the method can be invoked by a non-XMLHttpRequest
 * Takes precedence over the class-level annotation.
 *
 * @see com.blueskyminds.housepad.web.plugin.annotations.DenyXMLHttpRequest
 * @see com.blueskyminds.housepad.web.plugin.annotations.NonXMLHttpRequestOnly
 * @see com.blueskyminds.housepad.web.plugin.annotations.XMLHttpRequestOnly
 *
 * Date Started: 13/05/2008
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowNonXMLHttpRequest {
}