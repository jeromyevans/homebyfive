package com.blueskyminds.business.user.services;

/**
 * Date Started: 4/06/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class UserAccountServiceException extends Exception {

    public UserAccountServiceException(String message) {
        super(message);
    }

    public UserAccountServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAccountServiceException(Throwable cause) {
        super(cause);
    }
}
