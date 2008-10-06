package com.blueskyminds.homebyfive.web.struts2.security.services;

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
