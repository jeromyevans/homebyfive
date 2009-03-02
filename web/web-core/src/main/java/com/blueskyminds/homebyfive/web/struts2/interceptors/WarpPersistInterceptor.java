package com.blueskyminds.homebyfive.web.struts2.interceptors;

import com.opensymphony.xwork2.interceptor.Interceptor;
import com.opensymphony.xwork2.ActionInvocation;
import com.wideplay.warp.persist.Transactional;
import com.wideplay.warp.persist.WorkManager;
import com.google.inject.Inject;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 *
 * An interceptor that starts a transaction for the view
 *
 * This implemention uses a workManager directly so it can control whether the connection is readonly or not
 *
 * Date Started: 30/04/2008
 */
public class WarpPersistInterceptor implements Interceptor {

    private static final Log LOG = LogFactory.getLog(WarpPersistInterceptor.class);
    
    @Inject
    private EntityManager em;

    public void destroy() {
    }

    public void init() {
    }

    @Transactional
    public String intercept(ActionInvocation invocation) throws Exception {

        Session session = (Session) em.getDelegate();

        if (em.isOpen()) {
                String targetMethod = invocation.getProxy().getMethod();
            if (("create".equals(targetMethod)) || ("update".equals(targetMethod)) || ("destroy".equals(targetMethod))) {
                LOG.info("Allowing JDBC read-write JDBC connection");
                session.connection().setReadOnly(false);
            } else {
                LOG.info("Setting JDBC connection to read-only");
                session.connection().setReadOnly(true);
            }
        } else {
            LOG.warn("EntityManager is closed ("+em.toString()+")");
        }
        LOG.info("JDBC URL:"+ session.connection().getMetaData().getURL());
        LOG.info("em: "+em.toString());
        LOG.info("conn: "+ session.connection().toString());
        LOG.info("   EntityCount: "+session.getStatistics().getEntityCount());
        LOG.info("   CollectionCount: "+session.getStatistics().getCollectionCount());
        return invocation.invoke();
    }
}
