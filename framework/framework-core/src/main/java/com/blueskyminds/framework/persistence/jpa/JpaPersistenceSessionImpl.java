package com.blueskyminds.framework.persistence.jpa;

import com.blueskyminds.framework.persistence.AbstractPersistenceSession;
import com.blueskyminds.framework.persistence.PersistenceServiceException;
import com.blueskyminds.framework.persistence.PersistenceTransaction;

import javax.persistence.EntityManager;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * Date Started: 6/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class JpaPersistenceSessionImpl extends AbstractPersistenceSession<EntityManager> {

    private static final Log LOG = LogFactory.getLog(JpaPersistenceSessionImpl.class);

     // ------------------------------------------------------------------------------------------------------

    /** Create a new instance of a session */
    public JpaPersistenceSessionImpl(EntityManager entityManager) {
        super(entityManager);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the JpaPersistenceSessionImpl with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------


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
                    LOG.debug("=== Closed EntityManager (Thread:"+Thread.currentThread().getName()+") ===");

                    closed = true;
                } else {
                    // the session is not closed, so we need another transaction
                    beginTransaction();
                }
            } else {
                clearStack();
                closed =  true;
            }
        } catch(Exception e) {
            throw new PersistenceServiceException("Failed to close a session", e);
        }
        return closed;
    }

    // ------------------------------------------------------------------------------------------------------

     /**
      * Begin a unit of work and return the associated <tt>Transaction</tt> object.
     * If a new underlying transaction is required, begin the transaction. Otherwise
     * continue the new work in the context of the existing underlying transaction.
      * */
    public PersistenceTransaction beginTransaction() throws PersistenceServiceException {
         try {
             if ((transaction == null) || (!transaction.isActive())) {
                 transaction = new JpaPersistenceTransactionImpl(session.getTransaction());
                 transaction.begin();
                 LOG.debug("=== Starting transaction (Thread:"+Thread.currentThread().getName()+") ===");
             } else {
                 LOG.debug("=== Continuing transaction (Thread:"+Thread.currentThread().getName()+") ===");
             }
            return transaction;
         } catch (Exception e) {
             throw new PersistenceServiceException("Failed to begin a transaction", e);
         }
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get a reference to the underlying JPA EntityManager object */
    public EntityManager getSessionImpl() {
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
}
