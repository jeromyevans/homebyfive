package com.blueskyminds.homebyfive.web.core.ajax;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.HashMap;

/**
 * Date Started: 2/11/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class AJAXTools {

    private static final String PATTERN = "XMLHttpRequest";
    private static final String HEADER = "X-Requested-With";
    private static final String XHR_PARAM = "_xhr";

    /**
     * Inspect the header of a request to determine if it was via AJAX
     * (using the X-Requested-With header)
     *
     * @param request
     * @return
     */
    public static boolean isAJAXRequest(HttpServletRequest request) {
        String xRequestedWith = request.getHeader(HEADER);
        if (PATTERN.equals(xRequestedWith)) {
            return true;
        } else {

            // if the method is GET, check the override parameter that can be included in redirections
            if (request.getMethod().equals("GET")) {
                return  (request.getParameter(XHR_PARAM) != null);
            }
        }
        return false;
    }

    /**
     * Generates a map that contains the parameter to simulate an XHR request
     * @return
     */
    public static Map<String, String> generateXHRParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(XHR_PARAM, "");
        return params;
    }
}
