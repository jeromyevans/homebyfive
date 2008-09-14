package com.blueskyminds.housepad.web.plugin.interceptors;

/**
 * Date Started: 21/04/2008
 */
public interface XMLHttpRequestAware {

    /**
     * Sets the flag to true if this request originated from an XMLHttpRequest according to the ServletHttpRequest
     * header.
     *
     * @see com.blueskyminds.housepad.web.plugin.interceptors.XMLHttpRequestInterceptor
     *
     * @param isXMLHttpRequest
     */
    void setXMLHttpRequest(boolean isXMLHttpRequest);
}
