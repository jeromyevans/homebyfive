package com.blueskyminds.enterprise.license;

/**
 *
 * An invalid transaction exception means an attempt was made to create a transaction that was illegal
 *  eg. a transaction with only one entry, or an unbalanced transaction
 * 
 * Date Started: 30/04/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class IllegalTransactionException extends Exception {

    public IllegalTransactionException() {
    }

    public IllegalTransactionException(String message) {
        super(message);
    }

    public IllegalTransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalTransactionException(Throwable cause) {
        super(cause);
    }
}
