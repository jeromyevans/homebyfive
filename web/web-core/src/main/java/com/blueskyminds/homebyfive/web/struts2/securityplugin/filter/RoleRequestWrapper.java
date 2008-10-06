package com.blueskyminds.homebyfive.web.struts2.securityplugin.filter;

import com.blueskyminds.homebyfive.business.user.model.UserAccount;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.security.Principal;

/**
 * Stores a UserAccount as a request attributes and augments the isUserInRole method to check the roles
 *  of the UserAccount
 *
 * Date Started: 5/06/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class RoleRequestWrapper extends HttpServletRequestWrapper {

    public static final String USERACCOUNT_ATTRIBUTE = "struts.securityplugin.useraccount";

    public RoleRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    /**
     * The default behavior of this method is to return isUserInRole(String role)
     * on the wrapped request object.
     */
    public boolean isUserInRole(String role) {
        UserAccount userAccount = getUserAccount();

        if (userAccount != null) {
            return userAccount.isUserInRole(role);
        } else {
            return super.isUserInRole(role);
        }
    }

    /**
     * Provides the UserAccount if defined, otherwise delgates to the wrapped request object's getUserPrincipal
     */
    public Principal getUserPrincipal() {
        UserAccount userAccount = getUserAccount();
        if (userAccount != null) {
            return userAccount;
        } else {
            return super.getUserPrincipal();
        }
    }

    private UserAccount getUserAccount() {
        return (UserAccount) getAttribute(USERACCOUNT_ATTRIBUTE);
    }

    public void setUserAccount(UserAccount userAccount) {
        setAttribute(USERACCOUNT_ATTRIBUTE, userAccount);
    }
}
