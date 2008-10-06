package com.blueskyminds.business.accounting;

/**
 * An exception identifying an error when applying an entry to an account
 *
 * Date Started: 16/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class AccountEntryException extends Exception {
    public AccountEntryException() {
        super("Attempted to create an illegal entry for an Account");
    }

    public AccountEntryException(String message) {
        super(message);
    }

    public AccountEntryException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountEntryException(Throwable cause) {
        super(cause);
    }
}
