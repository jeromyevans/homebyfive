package com.blueskyminds.homebyfive.framework.core.net;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.Header;

/**
 * Date Started: 27/10/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class RemoteClientException extends Exception {

    private int statusCode;
    private String statusMessage;
    private Integer customStatusCode;
    private String customMessage;

    public RemoteClientException() {
    }

    public RemoteClientException(HttpMethod method) {
        super("Remote server responded with an error status:"+ method.getStatusLine().toString());
        statusCode = method.getStatusCode();
        statusMessage = method.getStatusText();
        Header header = method.getResponseHeader(HTTPHeaders.X_MESSAGE_HEADER);
        if (header != null) {
            customMessage = header.getValue();
        }
        header = method.getResponseHeader(HTTPHeaders.X_STATUS_CODE_HEADER);
        if (header != null) {
            try {
                customStatusCode = Integer.parseInt(header.getValue());
            } catch (NumberFormatException e) {
                // discard
            }
        }        
    }

    public RemoteClientException(String message) {
        super(message);
    }

    public RemoteClientException(String message, int statusCode, String statusMessage) {
        super(message);
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    public RemoteClientException(String message, int statusCode, String statusMessage, Integer customStatusCode, String customMessage) {
        this(message, statusCode, statusMessage);        
        this.customStatusCode = customStatusCode;
        this.customMessage = customMessage;
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

    public Integer getCustomStatusCode() {
        return customStatusCode;
    }

    public void setCustomStatusCode(Integer customStatusCode) {
        this.customStatusCode = customStatusCode;
    }

    public String getCustomMessage() {
        return customMessage;
    }

    public void setCustomMessage(String customMessage) {
        this.customMessage = customMessage;
    }

    public boolean isInvalidRequest() {
        return statusCode == HTTPHeaders.SC_BAD_REQUEST;
    }
}
