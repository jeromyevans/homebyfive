package com.blueskyminds.struts2.securityplugin.utils;

import org.apache.struts2.interceptor.PrincipalProxy;

import java.security.Principal;

import com.blueskyminds.business.user.model.Owned;

/**
 * Date Started: 19/04/2008
 */
public class Security {

    /**
     * Asserts whether the Principal is the Owner of the given object.
     * This will be true if the owner implements {@link Owned} and the Principal of the owner
     *  equals the Principal supplied
     *
     * @param o
     * @param principalProxy
     * @return
     */
    public static boolean isOwner(Object o, PrincipalProxy principalProxy) {
        if ((o != null) && (principalProxy != null)) {
            if (o instanceof Owned) {
                Principal ownerPrincipal = ((Owned) o).getOwnerPrincipal();
                if (ownerPrincipal != null) {
                    // check if they match
                    return ownerPrincipal.equals(principalProxy.getUserPrincipal());
                } else {
                    // no owner
                    return true;
                }
            } else {
                // not owned
                return true;
            }
        } else {
            return false;
        }
    }

    public static boolean isUserInRole(String roleName, PrincipalProxy principalProxy) {
        if (principalProxy != null) {
            return principalProxy.isUserInRole(roleName);
        } else {
            return false;
        }
    }

    public static boolean isUserInRoleAndOwner(String roleName, Object o, PrincipalProxy principalProxy) {
        return isUserInRole(roleName, principalProxy) && isOwner(o, principalProxy);
    }
}
