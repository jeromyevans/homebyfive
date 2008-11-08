package com.blueskyminds.homebyfive.business.tag.service;

/**
 * Date Started: 8/11/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class InvalidTagException extends Exception {

    public InvalidTagException() {
    }

    public InvalidTagException(String message) {
        super(message);
    }

    public InvalidTagException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTagException(Throwable cause) {
        super(cause);
    }
}
