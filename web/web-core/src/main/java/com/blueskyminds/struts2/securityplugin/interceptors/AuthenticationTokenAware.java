package com.blueskyminds.struts2.securityplugin.interceptors;

/**
 * Date Started: 8/05/2008
 */
public interface AuthenticationTokenAware {    
    void setAuthenticationToken(String token);
    void setAuthenticationSignature(String signature);
    void setAuthenticated(boolean isAuthenticated);
}
