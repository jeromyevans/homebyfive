package com.blueskyminds.framework.web.struts2.interceptors;

import com.opensymphony.xwork2.interceptor.Interceptor;
import com.opensymphony.xwork2.ActionInvocation;
import com.google.inject.Inject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.EntityTransaction;
import javax.persistence.EntityManager;

/**
 * A Struts2 interceptor that will create an EntityTransaction
 *
 * Date Started: 28/12/2007
 */
public class StandaloneEntityManagerInterceptor implements Interceptor {

    private static final Log LOG = LogFactory.getLog(StandaloneEntityManagerInterceptor.class);

    private EntityManager em;

    public StandaloneEntityManagerInterceptor() {
    }

    public String intercept(ActionInvocation actionInvocation) throws java.lang.Exception {
        String result;
        boolean transactionStarted = false;
        EntityTransaction transaction = null;

        if (em != null) {
            transaction = em.getTransaction();
            if (!transaction.isActive()) {
                LOG.debug("Commencing new EntityTransaction");
                transaction.begin();
                transactionStarted = true;
            } else {
                LOG.debug("EntityTransaction exists");
            }
        }

        try {
            LOG.debug("invoking "+actionInvocation.getAction().getClass().getSimpleName()+"."+actionInvocation.getInvocationContext().getName());
            result = actionInvocation.invoke();

            if (transactionStarted) {
                transaction.commit();
                LOG.debug("Committed EntityTransaction");
            }
        } catch (Exception e) {
            if (transaction != null) {
                try {
                    LOG.info("Rolling back EntityTransaction due to exception", e);
                    transaction.rollback();
                    LOG.info("Rolled back EntityTransaction");
                } catch (Exception e2) {
                    LOG.error("Failed to rollBack EntityTransaction", e2);
                }
            }
            throw e;
        }

        return result;
    }

    public void destroy() {
        //
    }

    public void init() {
        //
    }

    @Inject
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }
}
