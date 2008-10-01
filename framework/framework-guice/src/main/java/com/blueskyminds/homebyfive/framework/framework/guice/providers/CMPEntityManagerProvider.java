package com.blueskyminds.homebyfive.framework.framework.guice.providers;

import com.google.inject.Provider;

import javax.persistence.EntityManager;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A Guice provider of the current EntityManager, obtained via JNDI
 *
 * NOTE: it's essential that the EntityManager is available in this components environment naming context.
 * This is setup either be including a @PersistenceContext annotation in managed class, or
 * by defining the dependency in web.xml. In the case of this interceptor, we need a definition in web.xml
 * The name used in this interceptor must match the name defined in web.xml
 *
 * Date Started: 7/08/2007
 * <p/>
 * History:
 */
public class CMPEntityManagerProvider implements Provider<EntityManager> {

    private static final Log LOG = LogFactory.getLog(CMPEntityManagerProvider.class);

    public CMPEntityManagerProvider() {
    }

    public EntityManager get() {
        EntityManager em = null;
        try {
            Context context = new InitialContext();
            em = (EntityManager) context.lookup("java:comp/env/entityManager");
        } catch(NamingException e) {
            LOG.error("Failed to lookup EntityManager", e);
        }
        return em;
    }
}
