package com.blueskyminds.homebyfive.web.struts2.interceptors;

import com.opensymphony.xwork2.interceptor.Interceptor;
import com.opensymphony.xwork2.ActionInvocation;
import com.wideplay.warp.persist.Transactional;

/**
 *
 * An interceptor that starts a transaction for the view
 *
 * Date Started: 30/04/2008
 */
public class WarpPersistInterceptor implements Interceptor {

    public void destroy() {
    }

    public void init() {
    }

    @Transactional
    public String intercept(ActionInvocation invocation) throws Exception {
        return invocation.invoke();
    }
}
