package com.blueskyminds.homebyfive.framework.framework.tasks;

/**
 * This exception can be thrown by a LongIterativeTask to indicate that the work cannot be completed and all
 *  subsequent processing can be aborted.
 *
 * Date Started: 27/06/2007
 * <p/>
 * History:
 */
public class CannotCompleteException extends Exception { 

    public CannotCompleteException(String message) {
        super(message);
    }

    public CannotCompleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public CannotCompleteException(Throwable cause) {
        super(cause);
    }
}
