package com.blueskyminds.framework.web.struts2.interceptors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.transaction.UserTransaction;

import com.opensymphony.xwork2.interceptor.Interceptor;
import com.opensymphony.xwork2.ActionInvocation;

/**
 * Looks up the UserTransaction in a container managed persistence environment
 *
 * Date Started: 7/08/2007
 * <p/>
 * History:
 */
public class CMPUserTransactionInterceptor implements Interceptor {

    private static final Log LOG = LogFactory.getLog(CMPUserTransactionInterceptor.class);

    public String intercept(ActionInvocation actionInvocation) throws java.lang.Exception {
        String result;
        boolean transactionStarted = false;
        Context context = new InitialContext();
        UserTransaction tx = (UserTransaction) context.lookup("java:comp/UserTransaction");
        if (tx != null) {
            tx.begin();
            transactionStarted = true;
            LOG.info("Started UserTransaction");
        } else {
            LOG.error("Could not obtain a UserTransaction");
        }

        try {
            result = actionInvocation.invoke();

            if (transactionStarted) {
                tx.commit();
                LOG.info("Committed UserTransaction");
            }
        } catch (Exception e) {
            if (tx != null) {
                try {
                    LOG.info("Rolling back UserTransaction due to exception");
                    tx.rollback();
                    LOG.info("Rolled back UserTransaction");
                } catch (Exception e2) {
                    LOG.error("Failed to rollBack UserTransaction");
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
}
