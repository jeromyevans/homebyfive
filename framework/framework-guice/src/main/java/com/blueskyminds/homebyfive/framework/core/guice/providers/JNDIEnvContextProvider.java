package com.blueskyminds.homebyfive.framework.core.guice.providers;

import com.google.inject.Provider;
import com.google.inject.Inject;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Provides the java:comp/env context
 *
 * Date Started: 08/03/2009
 */
public class JNDIEnvContextProvider implements Provider<Context> {

    @Inject
    private InitialContext initialContext;

    public JNDIEnvContextProvider() {
    }

    public Context get() {
        try {
            return (Context) initialContext.lookup("java:comp/env");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
}
