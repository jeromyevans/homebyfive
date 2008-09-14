package com.blueskyminds.framework.persistence.jpa;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.blueskyminds.framework.persistence.PersistenceServiceException;
import com.blueskyminds.framework.persistence.PersistenceTransaction;

import javax.persistence.EntityTransaction;

/**
 * Implementation of a database transaction that wraps the transaction semantics in JPA
 *
 * Date Started: 6/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class JpaPersistenceTransactionImpl implements PersistenceTransaction {

     private static final Log LOG = LogFactory.getLog(JpaPersistenceTransactionImpl.class);

    /** The wrapped transaction */
    private EntityTransaction transaction;

    // ------------------------------------------------------------------------------------------------------

    /** Create an instance of a transaction */
    public JpaPersistenceTransactionImpl(EntityTransaction transaction) {
        this.transaction = transaction;
    }

    /** Begin the transaction if not already active
     * @throws PersistenceServiceException if the transaction cannot be commenced */
    public void begin() throws PersistenceServiceException {
        try {
            if (!transaction.isActive()) {
                transaction.begin();
            }
        } catch (IllegalStateException e) {
            throw new PersistenceServiceException(e);
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /** Flush the associated <tt>Session</tt> and end the unit of work */
    public void commit() throws PersistenceServiceException {
        try {
            if (transaction.isActive()) {
                transaction.commit();
                LOG.debug("=== Committed transaction (Thread:"+Thread.currentThread().getName()+") ===");
            }
        } catch (Exception e) {
            throw new PersistenceServiceException("Failed to commit a transaction", e);
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /** Force the underlying transaction to rollback */
    public void rollback() throws PersistenceServiceException {
        try {
            transaction.rollback();
        } catch (IllegalStateException e) {
            throw new PersistenceServiceException("Failed to rollback a transaction", e);
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /** Return true if this transaction is currently active */
    public boolean isActive() {
        if (transaction != null) {
            return (transaction.isActive());
        } else {
            return false;
        }
    }

    // ------------------------------------------------------------------------------------------------------
}
