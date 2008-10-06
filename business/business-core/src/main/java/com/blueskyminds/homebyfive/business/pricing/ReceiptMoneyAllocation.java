package com.blueskyminds.homebyfive.business.pricing;

import com.blueskyminds.homebyfive.framework.core.AbstractDomainObject;

import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Embedded;
import javax.persistence.Entity;

/**
 * Represents an allocation of an amount of money to an invoice by a receipt.
 *
 * Date Started: 3/06/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
public class ReceiptMoneyAllocation extends AbstractDomainObject {

    /** The receipt that this allocation is for */
    private Receipt receipt;

    /** The invoice that this allocation applies to */
    private Invoice invoice;

    /** The amount of money applied to the invoice */
    private Money amount;

    // ------------------------------------------------------------------------------------------------------

    public ReceiptMoneyAllocation(Receipt receipt, Invoice invoice, Money amount) {
        this.receipt = receipt;
        this.invoice = invoice;
        this.amount = amount;
    }

    /** Default constructor for ORM */
    protected ReceiptMoneyAllocation() {

    }

    // ------------------------------------------------------------------------------------------------------

    /** The receipt that this allocation is for */
    @ManyToOne
    @JoinColumn(name="ReceiptId")
    public Receipt getReceipt() {
        return receipt;
    }

    protected void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }

    // ------------------------------------------------------------------------------------------------------

    /** The invoice that this allocation applies to */
    @ManyToOne
    @JoinColumn(name="InvoiceId")
    public Invoice getInvoice() {
        return invoice;
    }

    protected void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the amount of money applied to the invoice */
    @Embedded
    public Money getAmount() {
        return amount;
    }

    protected void setAmount(Money amount) {
        this.amount = amount;
    }

    // ------------------------------------------------------------------------------------------------------
}
