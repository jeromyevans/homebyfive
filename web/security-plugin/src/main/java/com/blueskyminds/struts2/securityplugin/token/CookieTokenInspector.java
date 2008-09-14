package com.blueskyminds.struts2.securityplugin.token;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Cookie;

import org.apache.struts2.StrutsStatics;

/**
 * Inspects the HTTPServletRequest for a cookie named "X-AuthToken" and uses its value as the token
 *
 * Date Started: 6/05/2008
 */
public class CookieTokenInspector implements TokenInspector {

    private static final String AUTH_TOKEN = "X-AuthToken";

    public String read(ActionInvocation actionInvocation) {
        String token = null;
        final ActionContext actionContext = actionInvocation.getInvocationContext();

        HttpServletRequest request = (HttpServletRequest) actionContext.get(StrutsStatics.HTTP_REQUEST);

        if (request != null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (AUTH_TOKEN.equals(cookie.getName())) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }
        }

        return token;
    }
}