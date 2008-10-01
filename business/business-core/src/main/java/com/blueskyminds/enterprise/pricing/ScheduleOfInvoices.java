package com.blueskyminds.enterprise.pricing;

import com.blueskyminds.enterprise.Enterprise;
import com.blueskyminds.enterprise.Schedule;
import com.blueskyminds.homebyfive.framework.core.journal.Journal;

import javax.persistence.*;
import java.io.PrintStream;
import java.util.List;

/**
 * A list of invoices for an enterprise
 *
 * Date Started: 14/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
public class ScheduleOfInvoices extends Schedule<Invoice> {

    // ------------------------------------------------------------------------------------------------------

    /** Create a new schedule of invoices */
    public ScheduleOfInvoices(Enterprise enterprise, Journal journal) {
        super(enterprise, journal);
    }

    /** Default constructor for ORM */
    protected ScheduleOfInvoices() {

    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create the specified invoice
     * @return the invoice if added successfully, otherwise null
     */
    protected Invoice createInvoice(Invoice invoice) {
        return super.create(invoice);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Create an invoice for the specified order */
    public Invoice createInitialInvoice(Order order) {
        InvoiceFactory invoiceFactory = new InvoiceFactory();
        Invoice newInvoice = invoiceFactory.createInitialInvoice(order, journal);
        return createInvoice(newInvoice);
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /** Create the invoices for the specified order.  */
    public List<Invoice> createAllInvoices(Order order) {
        InvoiceFactory invoiceFactory = new InvoiceFactory();
        List<Invoice> invoices = invoiceFactory.createInvoices(order, journal);
        for (Invoice invoice : invoices) {
            createInvoice(invoice);
        }
        return invoices;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Deletes the specified invouice
     * The invoice can only be deleted if it has not been actioned.
     *
     * @param invoice
     * @return true if deleted successfully
     */
    public boolean deleteInvoice(Invoice invoice) {

        boolean deleted;

        if (!invoice.isActioned()) {
            // mark as deleted
            invoice.delete();
        }

        deleted = super.delete(invoice);

        return deleted;
    }

    // ------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="ScheduleOfInvoicesEntry",
            joinColumns=@JoinColumn(name="ScheduleOfInvoicesId"),
            inverseJoinColumns = @JoinColumn(name="InvoiceId")
    )
    protected List<Invoice> getInvoices() {
        return super.getDomainObjects();
    }

    protected void setInvoices(List<Invoice> invoices) {
        super.setDomainObjects(invoices);
    }

    // ------------------------------------------------------------------------------------------------------

    @Override
    @OneToOne()
    @JoinColumn(name="EnterpriseId")
    public Enterprise getEnterprise() {
        return super.getEnterprise();
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    public void print(PrintStream out) {
        out.println("--- "+getIdentityName()+" ---");
        for (Invoice invoice : getInvoices()) {
            invoice.print(out);
        }
    }
}
