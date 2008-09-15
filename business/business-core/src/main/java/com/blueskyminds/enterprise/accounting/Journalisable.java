package com.blueskyminds.enterprise.accounting;

import com.blueskyminds.enterprise.Enterprise;
import com.blueskyminds.enterprise.pricing.JournalisableDocumentStates;

/**
 * Interface for domain objects that can be journalised in the FinancialJournal
 *
 * That is, they apply transactions.
 *
 * Date Started: 25/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public interface Journalisable {

    /**
     * Get the current state of the journalisable document.  The current state affects the operations that
     * can be performed on the document.  eg. an actioned document cannot be deleted
     */
    public JournalisableDocumentStates getState();

    /**
     * Create a transaction and commit it to the financial journal for the chart of relationships in the
     * given Enterprise.
     *
     * @return the transaction that was created in the journal, or null if it wasn't possible to create
     */
    public FinancialTransaction journalise(Enterprise enterprise, String note) throws FinancialTransactionException;


    /**
    * Determines if any actions have been taken with the CommercialDocument.
    * If an action has been taken then it's probable that it can't be deleted.
    *
    * The document has been actioned if it's state is anything other then OPEN
    *
    * @return true if action has been taken
    */
    public boolean isActioned();

    /**
     * Determines if this CommercialDocument is open.
     *
     * An OPEN state means no action has been taken.
     *
     * @return true if action has been taken
     */
    public boolean isOpen();


    /**
     * Determines if this CommercialDocument is closed.
     *
     * @return true if action has been taken
     */
    public boolean isClosed();




    /**
     * Ge the financial transaction
     * @return FinancialTransaction
     */
    public FinancialTransaction getFinancialTransaction();
}

