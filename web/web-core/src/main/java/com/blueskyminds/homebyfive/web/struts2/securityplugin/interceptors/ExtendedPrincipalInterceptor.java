package com.blueskyminds.homebyfive.web.struts2.securityplugin.interceptors;

import com.opensymphony.xwork2.interceptor.Interceptor;
import com.opensymphony.xwork2.ActionInvocation;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.StrutsStatics;

/**
 *
 * Decorates the request Principal with a UserAccount if available
 *
 * Date Started: 13/05/2008
 */
public class ExtendedPrincipalInterceptor implements Interceptor {

    public String intercept(ActionInvocation invocation) throws Exception {
        HttpServletRequest request = (HttpServletRequest) invocation.getInvocationContext().get(StrutsStatics.HTTP_REQUEST);
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void destroy() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void init() {
        //To change body of implemented methods use File | Settings | File Templates.
    }


}
