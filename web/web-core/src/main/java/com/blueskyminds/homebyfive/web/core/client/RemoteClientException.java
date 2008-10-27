package com.blueskyminds.homebyfive.web.core.client;

/**
 * Date Started: 27/10/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class RemoteClientException extends Exception {
    public RemoteClientException() {
    }

    public RemoteClientException(String message) {
        super(message);
    }

    public RemoteClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemoteClientException(Throwable cause) {
        super(cause);
    }
}
