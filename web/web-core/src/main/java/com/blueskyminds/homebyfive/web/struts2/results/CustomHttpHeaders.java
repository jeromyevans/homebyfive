package com.blueskyminds.homebyfive.web.struts2.results;

import org.apache.struts2.rest.DefaultHttpHeaders;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;

import com.blueskyminds.homebyfive.framework.core.net.HTTPHeaders;

/**
 * Based on the HttpHeaders provided by the Struts2 REST plugin
 *
 * Date Started: 14/12/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class CustomHttpHeaders extends DefaultHttpHeaders {

    private String customMessage;
    private Integer customStatusCode;
    private Map<String, String> customHeaders;

    public CustomHttpHeaders() {
    }

    public CustomHttpHeaders(String result) {
        super(result);
    }

    public CustomHttpHeaders withHeader(String name, String value) {
        if (customHeaders == null) {
            customHeaders = new HashMap<String, String>();
        }
        customHeaders.put(name, value);
        return this;
    }

    public CustomHttpHeaders withCustomMessage(int customStatusCode, String message) {
        this.customStatusCode = customStatusCode;
        this.customMessage = message;
        return this;
    }

    @Override
    public String apply(HttpServletRequest request, HttpServletResponse response, Object target) {
        String result = super.apply(request, response, target);

        if (customHeaders != null) {
            for (Map.Entry<String, String> entry : customHeaders.entrySet()) {
                response.setHeader(entry.getKey(), entry.getValue());
            }
        }

        if (customMessage != null) {
            response.setHeader(HTTPHeaders.X_MESSAGE_HEADER, customMessage);
            response.setHeader(HTTPHeaders.X_STATUS_CODE_HEADER, customStatusCode.toString());
        }

        return result;
    }

    @Override
    public CustomHttpHeaders withStatus(int code) {
        super.withStatus(code);
        return this;
    }

    @Override
    public CustomHttpHeaders withETag(Object etag) {
        super.withETag(etag);
        return this;
    }

    @Override
    public CustomHttpHeaders withNoETag() {
        super.withNoETag();
        return this;
    }

    @Override
    public CustomHttpHeaders renderResult(String code) {
        super.renderResult(code);
        return this;
    }

    @Override
    public DefaultHttpHeaders setLocationId(Object id) {
        super.setLocationId(id);
        return this;
    }

    @Override
    public CustomHttpHeaders setLocation(String loc) {
        super.setLocation(loc);
        return this;
    }

    @Override
    public CustomHttpHeaders lastModified(Date date) {
        super.lastModified(date);
        return this;
    }

    @Override
    public CustomHttpHeaders disableCaching() {
        super.disableCaching();
        return this;
    }
}
