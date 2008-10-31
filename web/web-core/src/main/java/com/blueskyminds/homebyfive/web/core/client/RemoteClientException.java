package com.blueskyminds.homebyfive.web.core.client;

/**
 * Date Started: 27/10/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class RemoteClientException extends Exception {

    private int statusCode;
    private String statusMessage;

    public RemoteClientException() {
    }

    public RemoteClientException(String message) {
        super(message);
    }

    public RemoteClientException(String message, int statusCode, String statusMessage) {
        super(message);
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    public RemoteClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemoteClientException(Throwable cause) {
        super(cause);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
