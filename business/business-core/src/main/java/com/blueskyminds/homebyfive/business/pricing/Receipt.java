package com.blueskyminds.homebyfive.business.pricing;

import com.blueskyminds.homebyfive.business.Enterprise;
import com.blueskyminds.homebyfive.business.accounting.FinancialTransaction;
import com.blueskyminds.homebyfive.business.accounting.FinancialTransactionException;
import com.blueskyminds.homebyfive.business.accounting.Journalisable;
import com.blueskyminds.homebyfive.business.party.Party;

import javax.persistence.*;
import java.io.PrintStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * A Receipt identifies an actual receipt of money in a transaction - a transfer of real Money
 *
 * Date Started: 14/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
public class Receipt extends JournalisableDocument implements Journalisable {

    /** A map of invoices and the amount applied to each invoice */
    private List<ReceiptMoneyAllocation> receiptMoneyAllocations;

    private Date dateApplied;

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /**
     * Records a payment for the specified date
     *
     * Call applyAmount to setup the amount to apply to an invoice(s)
     *
     * @param dateApplied date of the payment
     */
    public Receipt(Date dateApplied, Party party) {
        super(party);
        init();
        this.dateApplied = dateApplied;
    }

    /** Default constructor for ORM */
    protected Receipt() {

    }

    // ------------------------------------------------------------------------------------------------------

    /** Initialise the receipt with default attributes */
    private void init() {
        receiptMoneyAllocations = new LinkedList<ReceiptMoneyAllocation>();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Apply the specified amount of money in this payment to the selected invoice
     * Prior allocations to that invoice by this payment are replaced (it is not summed)
     *
     * The gross value of this payment is updated if appropriate
     *
     * @param invoice
     * @param amount
     */
    public boolean applyAmount(Invoice invoice, Money amount) {
        return receiptMoneyAllocations.add(new ReceiptMoneyAllocation(this, invoice,  amount));
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Apply the value including tax of the selected invoice in this receipt
     *
     * The gross value of this payment is updated if appropriate
     *
     * @param invoice
     */
    public boolean applyAmount(Invoice invoice) {
        try {
            return applyAmount(invoice,  invoice.grossValueIncTax());
        }
        catch (InconsistentPricingException e) {
            return false;
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the total amount of this payment - equal to the balance of the invoice
     *
     * @return the total amount
     */
    public Money grossValue() {
        Money grossValue = new Money();

        for (ReceiptMoneyAllocation receiptMoneyAllocation : receiptMoneyAllocations) {
            grossValue.sum(receiptMoneyAllocation.getAmount());
        }

        return grossValue;
    }

    // ------------------------------------------------------------------------------------------------------

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="DateApplied")
    public Date getDateApplied() {
        return dateApplied;
    }

    protected void setDateApplied(Date dateApplied) {
        this.dateApplied = dateApplied;
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /** Get the allocations of money to invoices by this receipt */
    @OneToMany(mappedBy="receipt", cascade = CascadeType.ALL)
    public List<ReceiptMoneyAllocation> getReceiptMoneyAllocations() {
        return receiptMoneyAllocations;
    }

    protected void setReceiptMoneyAllocations(List<ReceiptMoneyAllocation> receiptMoneyAllocations) {
        this.receiptMoneyAllocations = receiptMoneyAllocations;
    }

    // ------------------------------------------------------------------------------------------------------

    public void print(PrintStream out) {
        out.println("--- "+getIdentityName()+" ---");
        out.println("Date:"+getDateApplied());
        out.println("Amount:"+grossValue());
        for (ReceiptMoneyAllocation receiptMoneyAllocation : getReceiptMoneyAllocations()) {
            out.println(receiptMoneyAllocation.getInvoice().getIdentityName()+" "+receiptMoneyAllocation.getAmount());
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a transaction and commit it to the financial journal for the chart of relationships in the
     * given Enterprise.
     *
     * @return the transaction that was created in the journal, or null if it wasn't possible to create
     */
    public FinancialTransaction journalise(Enterprise enterprise, String note) throws FinancialTransactionException {

        return enterprise.getFinancialJournal().createSimpleTransaction(getDateApplied(), enterprise.getChequeAccount(), enterprise.getAccountsReceivable(), grossValue(), note, getParty());
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
}
