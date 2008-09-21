package com.blueskyminds.struts2.securityplugin.token;

import com.opensymphony.xwork2.ActionInvocation;
import com.blueskyminds.struts2.securityplugin.token.TokenInspector;

import java.util.Map;

/**
 * Read the authentication token from the Session
 *
 * Date Started: 8/05/2008
 */
public class SessionTokenInspector implements TokenInspector {

    private static final String AUTHENTICATION_TOKEN_KEY = "auth.token";

    public String read(ActionInvocation actionInvocation) {
        final Map<String, Object> sessionMap = actionInvocation.getInvocationContext().getSession();
        return (String) sessionMap.get(AUTHENTICATION_TOKEN_KEY);
    }
}
