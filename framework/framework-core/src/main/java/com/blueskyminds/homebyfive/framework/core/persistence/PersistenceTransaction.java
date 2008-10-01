package com.blueskyminds.homebyfive.framework.core.persistence;

/**
 * A common interface to a database transaction, separated from the persistence layer implementation.
 *
 * Date Started: 26/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public interface PersistenceTransaction {

    /** Begin the transaction if not already active
     * @throws PersistenceServiceException if the transaction cannot be commenced */
    void begin() throws PersistenceServiceException;

    /** Flush the associated <tt>Session</tt> and end the unit of work */
    void commit() throws PersistenceServiceException;

    /** Force the underlying transaction to rollback */
    void rollback() throws PersistenceServiceException;

    /** Return true if this transaction is currently active */
    boolean isActive();
}
