package com.blueskyminds.struts2.securityplugin.annotations;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Identifies an action that can only be invoked if the Principal is the Owner of the Model
 * @see com.opensymphony.xwork2.ModelDriven
 *
 * Date Started: 13/05/2008
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ModelOwner {
}
