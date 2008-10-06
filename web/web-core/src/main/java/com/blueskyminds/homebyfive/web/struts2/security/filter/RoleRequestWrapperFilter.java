package com.blueskyminds.homebyfive.web.struts2.security.filter;

import com.blueskyminds.homebyfive.web.struts2.security.filter.RoleRequestWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * A filter that decorates the HttpServletRequest to allow the current principal's roles to be modified programatically.
 *
 * Date Started: 5/06/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class RoleRequestWrapperFilter implements javax.servlet.Filter {
    
    /** Wrap the request with one that contains a Principal loaded programmatically */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        RoleRequestWrapper decoratored = new RoleRequestWrapper((HttpServletRequest) request);
        chain.doFilter(decoratored, response);
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public void destroy() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
