package com.blueskyminds.enterprise.pricing;

import com.blueskyminds.enterprise.accounting.Journalisable;
import com.blueskyminds.enterprise.accounting.FinancialTransaction;
import com.blueskyminds.enterprise.accounting.FinancialTransactionException;
import com.blueskyminds.homebyfive.framework.framework.AbstractDomainObject;
import com.blueskyminds.homebyfive.framework.framework.DomainObjectStatus;
import com.blueskyminds.enterprise.party.Party;
import com.blueskyminds.enterprise.Enterprise;

import javax.persistence.*;

/**
 * An abstract superclass for documents that can be journalised into a FinancialJournal
 *
 * Date Started: 3/06/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@MappedSuperclass
public abstract class JournalisableDocument extends AbstractDomainObject implements Journalisable {

    /** The party this document was created for */
    private Party party;

    /** The state of this commercial document */
    private JournalisableDocumentStates state;

    /** The financial transaction associated with this document */
    private FinancialTransaction financialTransaction;

    // ------------------------------------------------------------------------------------------------------

    public JournalisableDocument(Party party) {
        this.party = party;
        init();
    }

    /** Default constructor for ORM */
    protected JournalisableDocument() {

    }
    
    // ------------------------------------------------------------------------------------------------------

    /** Initialise the document with default attributes */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /** Get the party that the document was created for */
    @ManyToOne
    @JoinColumn(name = "PartyId")
    public Party getParty() {
        return party;
    }

    protected void setParty(Party party) {
        this.party = party;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the state of the Document */
    @Enumerated
    @Column(name = "State")
    public JournalisableDocumentStates getState() {
        return state;
    }

    protected void setState(JournalisableDocumentStates state) {
        this.state = state;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
    * Determines if any actions have been taken with the CommercialDocument.
    * If an action has been taken then it's probable that it can't be deleted.
    *
    * The document has been actioned if it's state is anything other then OPEN
    *
    * @return true if action has been taken
    */
    @Transient
    public boolean isActioned() {
        return (!isOpen());
    }

    // ------------------------------------------------------------------------------------------------------

     /**
     * Determines if this CommercialDocument is open.
     *
     * An OPEN state means no action has been taken.
     *
     * @return true if action has been taken
     */
    @Transient
    public boolean isOpen() {
        return JournalisableDocumentStates.Open.equals(getState());
    }

    // ------------------------------------------------------------------------------------------------------

     /**
     * Determines if this CommercialDocument is closed.
     *
     * @return true if action has been taken
     */
    @Transient
    public boolean isClosed() {
        return JournalisableDocumentStates.Closed.equals(getState());
    }

    // ------------------------------------------------------------------------------------------------------

     /**
     * Changes the state of this CommercialDocument to Closed
     */
    public void close() {
        setState(JournalisableDocumentStates.Closed);
    }

    // ------------------------------------------------------------------------------------------------------

     /**
     * Changes the state of this CommercialDocument to Actioned, indicating work is in-progress
     * based on this document
     */
    public void action() {
        setState(JournalisableDocumentStates.Actioned);
    }

    // ------------------------------------------------------------------------------------------------------

     /**
     * Changes the state of this CommercialDocument to Deleted
     *
     * Also updates the Status of this DomainObject to Deleted
     */
    public void delete() {
        setState(JournalisableDocumentStates.Deleted);
        setStatus(DomainObjectStatus.Deleted);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Ge the financial transaction
     * @return FinancialTransaction
     */
    @Transient
    public FinancialTransaction getFinancialTransaction() {
        return financialTransaction;
    }

    protected void setFinancialTransaction(FinancialTransaction financialTransaction) {
        this.financialTransaction = financialTransaction;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a transaction and commit it to the financial journal for the chart of relationships in the
     * given Enterprise.
     *
     * @return the transaction that was created in the journal, or null if it wasn't possible to create
     */
    public abstract FinancialTransaction journalise(Enterprise enterprise, String note) throws FinancialTransactionException;
}
