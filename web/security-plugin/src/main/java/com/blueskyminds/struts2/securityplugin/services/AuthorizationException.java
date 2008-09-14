package com.blueskyminds.struts2.securityplugin.services;

/**
 * Date Started: 9/05/2008
 */
public class AuthorizationException extends Exception {

    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthorizationException(Throwable cause) {
        super(cause);
    }
}
