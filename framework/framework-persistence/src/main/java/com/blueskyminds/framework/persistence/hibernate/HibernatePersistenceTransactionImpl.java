package com.blueskyminds.framework.persistence.hibernate;

import com.blueskyminds.framework.persistence.PersistenceTransaction;
import com.blueskyminds.framework.persistence.PersistenceServiceException;
import org.hibernate.Transaction;
import org.hibernate.HibernateException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *  Implementation of a database transaction that wraps the transaction semantics in hibernate
 *
 * Date Started: 26/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class HibernatePersistenceTransactionImpl implements PersistenceTransaction {

    private static final Log LOG = LogFactory.getLog(HibernatePersistenceTransactionImpl.class);

    /** The wrapped transaction */
    private Transaction transaction;

    // ------------------------------------------------------------------------------------------------------

    /** Create an instance of a transaction */
    public HibernatePersistenceTransactionImpl(Transaction transaction) {
        this.transaction = transaction;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Begin the transaction if not already active
     * @throws PersistenceServiceException if the transaction cannot be commenced */
    public void begin() throws PersistenceServiceException {
        try {
            if (!transaction.isActive()) {
                transaction.begin();
            }
        } catch (HibernateException e) {
            throw new PersistenceServiceException(e);
        }
    }

    /** Flush the associated <tt>Session</tt> and end the unit of work */
    public void commit() throws PersistenceServiceException {
        try {
            if (transaction.isActive()) {
                transaction.commit();
                LOG.debug("=== Committed transaction (Thread:"+Thread.currentThread().getName()+") ===");               
            }
        } catch (HibernateException e) {
            throw new PersistenceServiceException("Failed to commit a transaction", e);
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /** Force the underlying transaction to rollback */
    public void rollback() throws PersistenceServiceException {
        try {
            transaction.rollback();
        } catch (HibernateException e) {
            throw new PersistenceServiceException("Failed to rollback a transaction", e);
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /** Return true if this transaction is currently active */
    public boolean isActive() {
        if (transaction != null) {
            return transaction.isActive();
        } else {
            return false;
        }
    }
}
