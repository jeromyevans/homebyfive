package com.blueskyminds.homebyfive.web.struts2.security.interceptors;

import com.opensymphony.xwork2.interceptor.Interceptor;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ModelDriven;
import com.blueskyminds.homebyfive.web.struts2.security.annotations.ModelOwner;
import com.blueskyminds.homebyfive.web.struts2.security.utils.Security;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.StrutsStatics;
import org.apache.struts2.servlet.interceptor.ServletPrincipalProxy;

/**
 * Allow allows an invocation or result to proceed if the current Principal is the Owner of the Model in a
 *  ModelDriven action
 *
 * Date Started: 13/05/2008
 */
public class ModelOwnerInterceptor implements Interceptor {

    private static final Log LOG = LogFactory.getLog(ModelOwnerInterceptor.class);

    public String intercept(ActionInvocation invocation) throws Exception {
        boolean proceed = false;
        Object action = invocation.getAction();

        Class actionClass = action.getClass();

        if (action instanceof ModelDriven) {
            if (actionClass.isAnnotationPresent(ModelOwner.class)) {
                Object model = ((ModelDriven) action).getModel();
                if (model != null) {
                    HttpServletRequest request = (HttpServletRequest) invocation.getInvocationContext().get(StrutsStatics.HTTP_REQUEST);
                    proceed = Security.isOwner(model, new ServletPrincipalProxy(request));
                } else {
                    // no model
                    proceed = true;
                }
            } else {
                // no restrictions
                proceed = true;
            }
        } else {
            // no restrictions
            proceed = true;
        }

        String methodName = invocation.getProxy().getMethod();

        if (proceed) {
            LOG.info("Action invocation granted ("+action.getClass().getSimpleName()+"."+methodName+")");
            return invocation.invoke();
        } else {
            LOG.info("Action invocation denied ("+action.getClass().getSimpleName()+"."+methodName+")");
        }

        return "unauthorized";
    }

    public void destroy() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void init() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
