package com.blueskyminds.enterprise.pricing;

import com.blueskyminds.enterprise.Enterprise;
import com.blueskyminds.enterprise.ProductAccountMap;
import com.blueskyminds.enterprise.TaxAccountMap;
import com.blueskyminds.enterprise.accounting.FinancialJournal;
import com.blueskyminds.enterprise.accounting.FinancialTransaction;
import com.blueskyminds.enterprise.accounting.FinancialTransactionException;
import com.blueskyminds.enterprise.accounting.Journalisable;
import com.blueskyminds.enterprise.pricing.terms.PrepaidInFull;
import com.blueskyminds.enterprise.pricing.terms.PrepaidRecurring;
import com.blueskyminds.enterprise.taxpolicy.TaxPolicy;
import com.blueskyminds.homebyfive.framework.framework.journal.Journal;

import javax.persistence.*;
import java.io.PrintStream;
import java.util.Date;
import java.util.Iterator;

/**
 *
 * An invoice for an order
 *
 * Date Started: 11/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
@PrimaryKeyJoinColumn(name="CommercialDocumentId")
public class Invoice extends CommercialDocument implements Journalisable {    

    /** The order that this invoice corresponds to */
    private Order order;

    /** The period of the order that this invoice corresponds to */
    private Integer orderPeriod;

    /** Derived from the terms, this is the due date of the invoice */
    private Date dateDue;

    // ------------------------------------------------------------------------------------------------------

    /** Create a new empty invoice without terms set
     *
     * @param order
     * @param orderPerod
     * @param journal
     */
    public Invoice(Order order, Integer orderPerod, Journal journal) {
        super(order.getParty(), order.getContract(), null, journal);
        this.order = order;
        this.orderPeriod = orderPerod;
        init();
    }

    /** Default constructor for ORM */
    protected Invoice() {

    }

    // ------------------------------------------------------------------------------------------------------

    /** Initialise the invoice with default attributes */
    private void init() {
        // attempt to calculate the date due from the terms
        if ((getTerms() instanceof PrepaidInFull) || (getTerms() instanceof PrepaidRecurring)) {
            dateDue = getDateApplied();
        }

    }

    // ------------------------------------------------------------------------------------------------------

    public void print(PrintStream out) {
        int itemNo = 1;
        try {
            out.println("--- "+getIdentityName()+" ---");
            out.println("For order:"+order.getIdentityName()+(orderPeriod != null ? " period: "+orderPeriod : ""));
            out.println("Date:"+(getDateApplied() != null ? getDateApplied() : " no date set"));

            for (CommercialDocumentItem item :getItems()) {
                out.println(itemNo+". "+item.getProduct()+" "+item.getPrice()+ " "+getItemQuantity(item)+" "+getItemGrossValue(item));
                itemNo++;
            }
            out.println("GrossValue:"+grossValue());
            out.println("Terms:"+getTerms());
            out.println("PaymentDue:"+getDateDue());
        }
        catch (InconsistentPricingException e) {
            out.println("Unable to print:"+e.getMessage());
        }
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /** Get the order that this invoice is for */
    @ManyToOne
    @JoinColumn(name="OrderId")
    public Order getOrder() {
        return order;
    }

    protected void setOrder(Order order) {
        this.order = order;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the period of the order that the invoice is applicable for */
    @Basic
    @Column(name="OrderPeriod")
    public Integer getOrderPeriod() {
        return orderPeriod;
    }

    protected void setOrderPeriod(Integer orderPeriod) {
        this.orderPeriod = orderPeriod;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the calculated due date for the invoice, dervived from the terms */
    @Temporal(TemporalType.DATE)
    @Column(name="DateDue")
    public Date getDateDue() {
        return dateDue;
    }

    public void setDateDue(Date dateDue) {
        this.dateDue = dateDue;
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /**
     * Create and transaction and commit it to the financial journal for the chart of relationships
     * The transaction is multi-legged based on the relationships for the items.
     *
     * There's lots that can go wrong in this method - should throw an exception?
     *
     * @return the transaction that was created in the journal, or null if it wasn't possible to create
     */
    public FinancialTransaction journalise(Enterprise enterprise, String note) throws FinancialTransactionException {
        FinancialTransaction financialTransaction;
        FinancialJournal financialJournal;
        ProductAccountMap productAccountMap;
        TaxAccountMap taxAccountMap;
        Iterator<TaxPolicy> taxIterator;
        TaxMap grossTax;


        financialJournal = enterprise.getFinancialJournal();
        productAccountMap = enterprise.getProductAccountMap();
        taxAccountMap = enterprise.getTaxAccountMap();
        financialTransaction = financialJournal.createMultileggedTransaction(getDateApplied(), note, getParty());

        try {
            // add an A/R debit entry for the gross value
            financialTransaction.addDebitEntry(enterprise.getAccountsReceivable(), grossValue());

            // add an A/R debit entry for each of the taxes
            grossTax = grossTax();
            taxIterator = grossTax.iterator();
            while (taxIterator.hasNext()) {
                TaxPolicy taxPolicy = taxIterator.next();
                financialTransaction.addDebitEntry(enterprise.getAccountsReceivable(), grossTax.getAmount(taxPolicy));
            }

            // prepare the entries for the other revemue/liability relationships

            // create revenue-account entries for the relationships referenced by the items in this invoice
            Iterator<CommercialDocumentItem> iterator = getItems().iterator();
            boolean okay = true;
            while ((iterator.hasNext()) && (okay)) {
                CommercialDocumentItem item = iterator.next();

                // update is used instead of create so only one entry is created per account (all the amounts are summed
                // per account)
                okay = financialTransaction.updateCreditEntry(getParty(), getDateApplied(), productAccountMap.getRevenueAccount(item.getProduct()), getItemGrossValue(item));
                if (okay) {
                    // update the liability for each of the taxes collected
                    grossTax = getItemGrossTax(item);
                    taxIterator = grossTax.iterator();
                    while ((taxIterator.hasNext()) && (okay)) {
                        TaxPolicy taxPolicy = taxIterator.next();
                        okay = financialTransaction.updateCreditEntry(getParty(), getDateApplied(), taxAccountMap.getLiabilityAccount(taxPolicy), grossTax.getAmount(taxPolicy));
                    }
                }
            }

            if (okay) {
                // commit the transaction to the journal
                financialJournal.commitTransaction(financialTransaction);
            }
        }
        catch (InconsistentPricingException e) {
            throw new FinancialTransactionException("Pricing for the transaction could not be completed", e);
        }

        return financialTransaction;
    }

    // ------------------------------------------------------------------------------------------------------

}

