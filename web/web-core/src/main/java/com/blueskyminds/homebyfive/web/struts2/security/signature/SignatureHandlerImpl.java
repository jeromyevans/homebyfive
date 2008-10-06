package com.blueskyminds.homebyfive.web.struts2.security.signature;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionContext;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.StrutsStatics;

/**
 * Date Started: 9/05/2008
 */
public class SignatureHandlerImpl implements SignatureHandler {

    private static final String DEFAULT_TOKEN_NAME = "sig";

    private String tokenName = DEFAULT_TOKEN_NAME;

    /**
     * Asserts that the principal and 
     * @param signature
     * @param actionInvocation
     * @return
     */
    public boolean accept(String signature, ActionInvocation actionInvocation) {
        return false; 
    }

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
