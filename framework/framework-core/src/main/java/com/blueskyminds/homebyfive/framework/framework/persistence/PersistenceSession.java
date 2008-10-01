package com.blueskyminds.homebyfive.framework.framework.persistence;

/**
 * A common interface to a database session, separated from the persistence layer implementation.
 *
 * Date Started: 26/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public interface PersistenceSession<S> {

     /** Begin a unit of work and return the associated <tt>Transaction</tt> object.
	 * If a new underlying transaction is required, begin the transaction. Otherwise
	 * continue the new work in the context of the existing underlying transaction. */
    PersistenceTransaction beginTransaction() throws PersistenceServiceException;

    /** Attempt to flush and close the session
     * @return true if closed */
    boolean close() throws PersistenceServiceException;

    /** Get a reference to the underlying session mechanism */
    S getSessionImpl();

    /** Returns the current transaction in use in this session */
    PersistenceTransaction currentTransaction();

    /** Indicates to the current unit of work that the conversation is being continued. */
    void continueConversation();

}
