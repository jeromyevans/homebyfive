package com.blueskyminds.homebyfive.framework.framework.jpa;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * A simple AOP Alliance interceptor that starts an EntityTransaction, invokes the method,
 *  and then commits the transaction.
 *
 * Date Started: 23/10/2007
 * <p/>
 * History:
 */
public class StandaloneEntityManagerMethodInterceptor implements MethodInterceptor {

    private static final Log LOG = LogFactory.getLog(StandaloneEntityManagerMethodInterceptor.class);

    private Provider<EntityManager> em;

    public StandaloneEntityManagerMethodInterceptor() {
    }

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Object result;
        boolean transactionStarted = false;
        EntityTransaction transaction = null;

        if (em != null) {

            transaction = em.get().getTransaction();
            if (!transaction.isActive()) {
                LOG.debug("Commencing new EntityTransaction");
                transaction.begin();
                transactionStarted = true;
            } else {
                LOG.debug("EntityTransaction exists");
            }
        }

        try {
            LOG.debug("invoking "+methodInvocation.getThis().getClass().getSimpleName()+"."+methodInvocation.getMethod().getName());

            boolean flushEager = methodInvocation.getMethod().isAnnotationPresent(FlushEager.class);            

            result = methodInvocation.proceed();

            if (flushEager) {
                em.get().flush();
            }

            if (transactionStarted) {
                transaction.commit();
                LOG.debug("Committed EntityTransaction");
            }
        } catch (Exception e) {
            if (transaction != null) {
                if (transactionStarted) {
                    try {
                        LOG.info("Rolling back EntityTransaction due to exception", e);
                        transaction.rollback();
                        LOG.info("Rolled back EntityTransaction");
                    } catch (Exception e2) {
                        LOG.error("Failed to rollBack EntityTransaction", e2);
                    }
                }
            }
            throw e;
        }

        return result;

    }

    /**
     * The provider must be injected to ensure we're getting the instance for this thread
     *
     * @param em
     */
    @Inject
    public void setEm(Provider<EntityManager> em) {
        this.em = em;
    }
}
