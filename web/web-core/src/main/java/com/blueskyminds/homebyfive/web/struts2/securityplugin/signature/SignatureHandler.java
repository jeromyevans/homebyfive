package com.blueskyminds.homebyfive.web.struts2.securityplugin.signature;

import com.opensymphony.xwork2.ActionInvocation;

/**
 * Checks whether a signature is valid.
 *
 * Date Started: 9/05/2008
 */
public interface SignatureHandler {

    /**
     * Extract the signature from an ActionInvocation
     * 
     * @param actionInvocation
     * @return the signature, or null if not supplied
     */
    String read(ActionInvocation actionInvocation);

    boolean accept(String signature, ActionInvocation actionInvocation);
}
