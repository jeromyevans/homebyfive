package com.blueskyminds.struts2.securityplugin.token;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionContext;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.StrutsStatics;

/**
 * Read the authentication token from a request parameter.
 *
 * Date Started: 8/05/2008
 */
public class RequestParamTokenInspector implements TokenInspector {

    private static final String DEFAULT_TOKEN_NAME = "token";

    private String tokenName = DEFAULT_TOKEN_NAME;

    public String read(ActionInvocation actionInvocation) {
        String token = null;
        final ActionContext actionContext = actionInvocation.getInvocationContext();

        HttpServletRequest request = (HttpServletRequest) actionContext.get(StrutsStatics.HTTP_REQUEST);

        if (request != null) {
            token = request.getParameter(tokenName);
        }
        return token;
    }
}
