package com.blueskyminds.business.accounting;

/**
 * An exception relating to the creation or application of a financial transaction.
 *
 * Date Started: 16/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class FinancialTransactionException extends Exception {

    public FinancialTransactionException() {
        super("An exception occured while applying a financial transaction");
    }

    public FinancialTransactionException(String message) {
        super(message);
    }

    public FinancialTransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public FinancialTransactionException(Throwable cause) {
        super(cause);
    }
}
