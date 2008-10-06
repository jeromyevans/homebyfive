package com.blueskyminds.enterprise.user.services;

/**
 * Date Started: 15/05/2008
 */
public class UnknownUserException extends Exception {

    public UnknownUserException() {
        super();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public UnknownUserException(String message) {
        super(message);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public UnknownUserException(String message, Throwable cause) {
        super(message, cause);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public UnknownUserException(Throwable cause) {
        super(cause);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
