package com.blueskyminds.framework.persistence.hibernate;

import com.blueskyminds.framework.persistence.PersistenceTransaction;
import com.blueskyminds.framework.persistence.PersistenceServiceException;
import com.blueskyminds.framework.persistence.AbstractPersistenceSession;
import org.hibernate.Session;
import org.hibernate.HibernateException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Implementation of a sesison for the hibernate implementation.
 * Wraps a hibernate Sesss
 *
 * Date Started: 26/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class HibernatePersistenceSessionImpl extends AbstractPersistenceSession<Session> {

    private static final Log LOG = LogFactory.getLog(HibernatePersistenceSessionImpl.class);

    // ------------------------------------------------------------------------------------------------------

    /** Create a new instance of a session */
    public HibernatePersistenceSessionImpl(Session session) {
        super(session);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Attempt to flush and close the session.
     * If the session depth is not zero (that is, this session has been continued), then the session is not closed
     *  and only the depth is reduced */
    public boolean close() throws PersistenceServiceException {
        boolean closed = false;
        try {
            if (session.isOpen()) {
                decrementStack();
                if (transaction != null) {
                    if (transaction.isActive()) {
                        transaction.commit();
                    }
                }
                if (stackEmpty()) {
                    session.close();  // only really close the session if there's no nested sessions
                    LOG.debug("=== Closed session (Thread:"+Thread.currentThread().getName()+") ===");

                    closed = true;
                }
            } else {
                clearStack();
                closed =  true;
            }
        } catch(HibernateException e) {
            throw new PersistenceServiceException("Failed to close a session", e);
        }
        return closed;
    }

    // ------------------------------------------------------------------------------------------------------

     /**
      * Begin a unit of work and return the associated <tt>Transaction</tt> object.
     * If a new underlying transaction is required, begin the transaction. Otherwise
     * continue the new work in the context of the existing underlying transaction. */
    public PersistenceTransaction beginTransaction() throws PersistenceServiceException {
         try {
             if ((transaction == null) || (!transaction.isActive())) {
                 transaction = new HibernatePersistenceTransactionImpl(session.beginTransaction());
                 LOG.debug("=== Starting transaction (Thread:"+Thread.currentThread().getName()+") ===");
             } else {
                 LOG.debug("=== Continuing transaction (Thread:"+Thread.currentThread().getName()+") ===");
             }
            return transaction;
         } catch (HibernateException e) {
             throw new PersistenceServiceException("Failed to begin a transaction", e);
         }
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get a reference to the underlying hibernate session object */
    public Session getSessionImpl() {
        return session;
    }

    /** Get the current transaction for this session.  If there is no transaction a new one is started */
    public PersistenceTransaction currentTransaction() {
        if (!transaction.isActive()) {
            try {
                beginTransaction();
            } catch (PersistenceServiceException e) {
                LOG.error("=== Error starting transaction (Thread:"+Thread.currentThread().getName()+") ===");
                e.printStackTrace();
            }
        }
        return transaction;
    }

    // ------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------

}
