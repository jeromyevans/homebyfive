package com.blueskyminds.framework.persistence.spooler;

/**
 * Indicates that an error occured during spooling/processing
 *
 * Date Started: 11/06/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class SpoolerException extends Exception {

    public SpoolerException() {
    }

    public SpoolerException(String message) {
        super(message);
    }

    public SpoolerException(String message, Throwable cause) {
        super(message, cause);
    }

    public SpoolerException(Throwable cause) {
        super(cause);
    }

}
