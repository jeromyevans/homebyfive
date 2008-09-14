package com.blueskyminds.struts2.securityplugin.session;

import com.google.inject.Inject;

import javax.servlet.ServletContext;

/**
 * Date Started: 10/05/2008
 */
public class SessionRegistryInitializer {

    /**
     * When the SessionRegistry is created, get the sole instance place it into the static variable
     * @param sessionRegistry
     */
    @Inject
    public void setSessionRegistry(SessionRegistry sessionRegistry) {
        SessionRegistry.injectSoleInstance(sessionRegistry);
    }

}