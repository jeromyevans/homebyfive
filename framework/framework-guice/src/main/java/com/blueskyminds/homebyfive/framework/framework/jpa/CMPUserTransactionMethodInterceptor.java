package com.blueskyminds.homebyfive.framework.framework.jpa;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.transaction.UserTransaction;

/**
 * An AOP Alliance interceptor that looks up a user transaction
 *
 * Date Started: 8/08/2007
 * <p/>
 * History:
 */
public class CMPUserTransactionMethodInterceptor implements MethodInterceptor {

    private static final Log LOG = LogFactory.getLog(CMPUserTransactionMethodInterceptor.class);

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Object result;
        boolean transactionStarted = false;
        Context context = new InitialContext();

        UserTransaction ut = (UserTransaction) context.lookup("java:comp/UserTransaction");
        if (ut != null) {
            ut.begin();
            transactionStarted = true;
            LOG.info("Started UserTransaction");
        } else {
            LOG.error("Could not obtain a UserTransaction");
        }

         try {
            result = methodInvocation.proceed();

            if (transactionStarted) {
                ut.commit();
                LOG.info("Committed UserTransaction");
            }
        } catch (Exception e) {
            if (ut != null) {
                try {
                    LOG.info("Rolling back UserTransaction due to exception", e);
                    ut.rollback();
                    LOG.info("Rolled back UserTransaction");
                } catch (Exception e2) {
                    LOG.error("Failed to rollBack UserTransaction", e2);
                }
            }
            throw e;
        }
        return result;

    }
}
