package com.blueskyminds.homebyfive.framework.framework.persistence;

/**
 * Indicates an error accessing the persistence layer
 *
 * Date Started: 26/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class PersistenceServiceException extends Exception {

    public PersistenceServiceException() {
        super("Error accessing the persistence service");
    }

    public PersistenceServiceException(String message) {
        super(message);
    }

    public PersistenceServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersistenceServiceException(Throwable cause) {
        super(cause);
    }
}
