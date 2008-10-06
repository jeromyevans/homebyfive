package com.blueskyminds.homebyfive.web.struts2.securityplugin.token;

import com.opensymphony.xwork2.ActionInvocation;

/**
 * Gets the authentication token from an ActionInvocation
 *
 * Date Started: 8/05/2008
 */
public interface TokenInspector {

    String read(ActionInvocation actionInvocation);
}
