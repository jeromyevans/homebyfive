package com.blueskyminds.homebyfive.web.struts2.securityplugin.interceptors;

import com.opensymphony.xwork2.interceptor.Interceptor;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.inject.Inject;
import com.blueskyminds.homebyfive.web.struts2.securityplugin.token.TokenInspector;
import com.blueskyminds.homebyfive.web.struts2.securityplugin.token.TokenRegistry;
import com.blueskyminds.homebyfive.business.user.services.UserAccountService;
import com.blueskyminds.homebyfive.business.user.model.UserAccount;
import com.blueskyminds.homebyfive.web.struts2.securityplugin.filter.RoleRequestWrapper;
import com.google.inject.Provider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.StrutsStatics;

import javax.servlet.http.HttpServletRequest;

/**
 * Checks the HttpServletRequest header for an AuthToken.  If present, modifies the request to include
 *  principal and role information provided by the AuthorizationService
 *
 * Date Started: 5/06/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class TokenAuthorizationInterceptor implements Interceptor {

    private static final Log LOG = LogFactory.getLog(CheckSignatureInterceptor.class);

    private TokenInspector tokenInspector;
    private TokenRegistry tokenRegistry;
    private Provider<UserAccountService> userAccountService;

    public String intercept(ActionInvocation invocation) throws Exception {
        if (tokenInspector != null) {
            if (tokenRegistry != null) {

                String token = tokenInspector.read(invocation);

                if (token != null && token.length() > 0) {
                    Long userId = (Long) tokenRegistry.get(token);
                    if (userId != null) {
                        HttpServletRequest request = (HttpServletRequest) invocation.getInvocationContext().get(StrutsStatics.HTTP_REQUEST);

                        if (request != null) {
                            // augment the request with principal and role information
                            UserAccount userAccount = userAccountService.get().lookup(userId);
                            request.setAttribute(RoleRequestWrapper.USERACCOUNT_ATTRIBUTE, userAccount);
                        }
                    }
                }
            } else {
                LOG.error("An AuthenticationService is not available");
            }
        } else {
            LOG.error("An AuthenticationTokenInspector is is not available");
        }
        return invocation.invoke();
    }

    public void destroy() {
    }

    public void init() {
    }

    @Inject("default")
    public void setTokenInspector(TokenInspector tokenInspector) {
        this.tokenInspector = tokenInspector;
    }

    @Inject
    public void setTokenRegistry(TokenRegistry tokenRegistry) {
        this.tokenRegistry = tokenRegistry;
    }

    @com.google.inject.Inject
    public void setUserAccountService(Provider<UserAccountService> userAccountService) {
        this.userAccountService = userAccountService;
    }
}
