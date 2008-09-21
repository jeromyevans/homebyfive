package com.blueskyminds.struts2.securityplugin.token;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionContext;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.StrutsStatics;

/**
 * Inspects the headers of the HTTPServletRequest for a header named "X-AuthToken" and uses its value as the token
 *
 * Date Started: 8/05/2008
 */
public class HttpHeaderTokenInspector implements TokenInspector {

    private static final String AUTH_TOKEN = "X-AuthToken";

    public String read(ActionInvocation actionInvocation) {
        String token = null;
        final ActionContext actionContext = actionInvocation.getInvocationContext();

        HttpServletRequest request = (HttpServletRequest) actionContext.get(StrutsStatics.HTTP_REQUEST);

        if (request != null) {
            token = request.getHeader(AUTH_TOKEN);
        }

        return token;
    }
}
