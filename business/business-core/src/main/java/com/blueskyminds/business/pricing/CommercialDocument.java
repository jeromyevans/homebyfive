package com.blueskyminds.business.pricing;

import com.blueskyminds.business.party.Party;
import com.blueskyminds.business.pricing.terms.Terms;
import com.blueskyminds.business.taxpolicy.TaxPolicy;
import com.blueskyminds.homebyfive.framework.core.datetime.Interval;
import com.blueskyminds.homebyfive.framework.core.journal.Journal;
import com.blueskyminds.homebyfive.framework.core.measurement.Quantity;
import com.blueskyminds.homebyfive.framework.core.recurrence.Recurrence;
import com.blueskyminds.homebyfive.framework.core.recurrence.constraint.NoEarlierThan;
import com.blueskyminds.homebyfive.framework.core.recurrence.constraint.NoLaterThan;

import javax.persistence.*;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Abstract implementation of a CommercialDocument with commonly used methods and properties
 *
 * Date Started: 11/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity(name="CommercialDocument")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class CommercialDocument extends JournalisableDocument {

    /** The Contract that this document was created for */
    private Contract contract;

    /** The list of order items in this order */
    private List<CommercialDocumentItem> items;

    /** The terms for the order */
    private Terms terms;

    /** The optional start date for this document */
    private Date dateApplied;

    /** The optional interval/duration of this document - important for recurring terms */
    private Interval interval;

    /** A journal to use for recording events */
    private Journal journal;

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a new empty document for the specified accountHolder
     * @param party
     * @param terms
     * @param journal
     */
    public CommercialDocument(Party party, Contract contract, Terms terms, Journal journal) {
        super(party);
        this.journal = journal;
        this.terms = terms;
        this.contract = contract;
        init();
    }

    /** Default constructor for ORM */
    protected CommercialDocument() {

    }

    // ------------------------------------------------------------------------------------------------------

    /** Initialise the order with default attributes */
    private void init() {
        dateApplied = getDateCreated(); // assumed to be at the time it's created, but can be changed
        items = new LinkedList<CommercialDocumentItem>();
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /**
     * Attempts to add the specified product to this CommercialDocument
     *
     * There is no check to see if the product is actually available.
     *
     * Does check that the product has a PricingPolicy under the current contract.
     *
     * @param product
     * @return true if added to this order
     */
    @SuppressWarnings({"unchecked"})
    public boolean addProduct(Product product, Quantity quantity) {
        boolean added = false;

        // assert that the license is available under this contract
        if (contract.isProductAvailable(product)) {
            CommercialDocumentItem item = new CommercialDocumentItem(this);
            item.setProduct(product);

            item.setQuantity(quantity);
            // lookup the price from the account's contract
            item.setPrice(contract.lookupPricingPolicy(product));

            added = addItem(item);
         }

        return added;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Adds the specified item to this document
     *
     * @param item
     * @return true if added ok
     */
    public boolean addItem(CommercialDocumentItem item) {
        boolean added = items.add(item);
        if (added) {
            getJournal().added(this, item.getProduct(), null);
        }
        return added;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Copy the specified item to this CommercialDocument */
    @SuppressWarnings({"unchecked"})
    public boolean copyItem(CommercialDocumentItem item) {
        CommercialDocumentItem newItem = item.copy();
        newItem.setDocument(this);
        return addItem(newItem);
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns the total value of this CommercialDocument
     *
     * This implementation delegates to the getItemGrossValue method for each item and sums the results
     *
     * The gross value does not include tax
     *
     * @return the total value of this CommercialDocument
     */
    public Money grossValue() throws InconsistentPricingException {
        Money grossValue = new Money();

        for (CommercialDocumentItem item : getItems()) {
            grossValue.sum(getItemGrossValue(item));
        }

        return grossValue;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns the total tax on this CommercialDocument
     *
     * This implementation delegates to the getItemGrossTax method for each item and sums the results
     *
     * @return the total tax on the items in this CommercialDocument
     */
    public TaxMap grossTax() throws InconsistentPricingException {
        TaxMap grossTax = new TaxMap();

        for (CommercialDocumentItem item : getItems()) {
            grossTax.sum(getItemGrossTax(item));
        }

        return grossTax;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns the sum value of this CommercialDocument, tax inclusive
     * @return the gross value plus gross tax amounts
     */
    public Money grossValueIncTax() throws InconsistentPricingException {
        Money sum = grossValue();
        TaxMap tax = grossTax();
        
        // sum all of the taxes
        Iterator<TaxPolicy> iterator = tax.iterator();
        while (iterator.hasNext()) {
            TaxPolicy taxPolicy = iterator.next();
            sum.sum(tax.getAmount(taxPolicy));
        }

        return sum;
    }

    // ------------------------------------------------------------------------------------------------------

    /** The MAX_OCCURRENCES is used to limit the estimated number of transactions */
    private static final int MAX_OCCURRENCES = 104;

    // ------------------------------------------------------------------------------------------------------

    /**
     * Estimates the total number of transactions associated with this CommercialDocument.
     * Derived from the interval and terms.
     * @return estimated total number of transactions
     */
    private int estimateTotalTransactions() {
        int noOfTransactions = terms.recurrence(dateApplied).occurrences(dateApplied, getTimespan(), MAX_OCCURRENCES);
        if (noOfTransactions == -1) {
            // maximum exceeded  - set to one for information
            noOfTransactions = 1;
        }
        return noOfTransactions;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns the quantity of the specified item in this CommercialDocument.
     * The quantity is obtained directly from the item itself, but may be updated within the context
     * of this CommercialDocument.
     *
     * eg. if the item is charged only per transaction, then the quantity is derived from the estimated
     *  number of transactions in this interval (based on the terms of payment)
     *
     * @param item
     * @return quantity
     */
    @Transient
    public Quantity getItemQuantity(CommercialDocumentItem item) {
        Quantity quantity;

         if (item.getPrice().hasPerOccurrenceComponent()) {
            // estimate the number of transactions
             quantity = item.getQuantity().multiply(estimateTotalTransactions());
        }
        else {
            // simply get the quantity from the item
            quantity = item.getQuantity();
        }

        return quantity;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns the gross value of the specified item in the context of this CommercialDocument.
     *
     * The price is obtained directly from the item itself but for certain types of items may be updated
     *  in the context of this commercial document.
     *
     * eg. an item who's price depends on interval or the number of transactions.
     *
     * @param item
     * @return quantity
     */
    @Transient
    public Money getItemGrossValue(CommercialDocumentItem item) throws InconsistentPricingException {

        Money grossValue;

        if (item.getPrice().hasPerOccurrenceComponent()) {
            // estimate the number of transactions
            grossValue = item.grossValue(estimateTotalTransactions(), getTimespan());
        }
        else {
            // simply add the value of this item
            grossValue = item.grossValue(getTimespan());
        }

        return grossValue;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns the gross tax of the specified item in this CommercialDocument.
     *
     * @param item
     * @return amount of tax (as Money) on the item
     */
    @Transient
    public TaxMap getItemGrossTax(CommercialDocumentItem item) throws InconsistentPricingException {

        TaxMap grossTax;

        if (item.getPrice().hasPerOccurrenceComponent()) {
            // estimate the number of transactions
            grossTax = item.grossTax(estimateTotalTransactions(), getTimespan());
        }
        else {
            // simply return the tax on this item
            grossTax = item.grossTax(getTimespan());
        }

        return grossTax;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns the due date for this commercial document.  This is derived from the Terms.
     * If it's not applicable, then it will be equal to the dateApplied
     *
     * @return the date due for this commercial document.
     */
    @Transient
    public Date getDateDue() {
        return terms.getDateDue(dateApplied);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Convenience method that passes straight through to the items to determine if this CommercialDocument contains
     * any items with a recurring component
     *
     * @return true if there is a recurring component
     */
    public boolean hasRecurringComponent() {
        boolean found = false;

        for (CommercialDocumentItem item : getItems()) {
            if (item.hasRecurringComponent()) {
                found = true;
                break;
            }
        }
        return found;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Derives a representation of the Recurrence defined in this CommercialDocument.
     * The Recurrence is obtained directly from the PaymentTerms
     *
     * @return recurrence
     */
    public Recurrence recurrence() {

        Recurrence recurrence = terms.recurrence(dateApplied);

        // add the start and end-date constraints
        recurrence.addConstraint(new NoEarlierThan(dateApplied, recurrence));
        recurrence.addConstraint(new NoLaterThan(interval.getDateOfFinalPeriod(dateApplied), recurrence));

        return recurrence;
    }

    // ------------------------------------------------------------------------------------------------------

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL)
    public List<CommercialDocumentItem> getItems() {
        return items;
    }

    protected void setItems(List<CommercialDocumentItem> items) {
        this.items = items;
    }

    // ------------------------------------------------------------------------------------------------------

    @Transient
    public Journal getJournal() {
        return journal;
    }

    // ------------------------------------------------------------------------------------------------------

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="TermsId")
    public Terms getTerms() {
        return terms;
    }

    protected void setTerms(Terms terms) {
        this.terms = terms;
    }

// ------------------------------------------------------------------------------------------------------

    /**
     * The date the document was applied
     *
     * If the date is null then the document has not been persisted 
     *
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="DateApplied")
    public Date getDateApplied() {
        return dateApplied;
    }

    protected void setDateApplied(Date dateApplied) {
        this.dateApplied = dateApplied;
    }

    // ------------------------------------------------------------------------------------------------------

    @Embedded
    public Interval getTimespan() {
        return interval;
    }

    protected void setTimespan(Interval interval) {
        this.interval = interval;
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    @ManyToOne
    @JoinColumn(name="ContractId")
    public Contract getContract() {
        return contract;
    }

    protected void setContract(Contract contract) {
        this.contract = contract;
    }

    // ------------------------------------------------------------------------------------------------------

}
