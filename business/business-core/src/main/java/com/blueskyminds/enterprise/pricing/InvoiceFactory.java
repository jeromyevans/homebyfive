package com.blueskyminds.enterprise.pricing;

import com.blueskyminds.enterprise.pricing.terms.PrepaidInFull;
import com.blueskyminds.enterprise.pricing.terms.PrepaidRecurring;
import com.blueskyminds.homebyfive.framework.core.datetime.Interval;
import com.blueskyminds.homebyfive.framework.core.journal.Journal;
import com.blueskyminds.homebyfive.framework.core.recurrence.Recurrence;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * A Factory to create an Invoice for Orders
 *
 * The type of invoice created depends on the type of order and the terms attached to the order.
 *
 * Date Started: 11/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class InvoiceFactory {

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /** Derive terms for the invoice from the terms on the order. */

    private void deriveTermsFromOrder(Invoice invoice, Order order) {
        // derive the terms for the invoice from the order
        if (order.getTerms() instanceof PrepaidInFull) {
            invoice.setTerms(new PrepaidInFull());
        }
        else {
            if (order.getTerms() instanceof PrepaidRecurring) {
                // prepaid in full, but only for one of the recurring periods
                invoice.setTimespan(new Interval(1, ((PrepaidRecurring) order.getTerms()).getPeriodType()));
                invoice.setTerms(new PrepaidInFull());
            }
            else {
                throw new UnsupportedOperationException("Terms on an order could not be converted to terms on an invoice");
            }
        }

    }
    // ------------------------------------------------------------------------------------------------------

    /**
     * Create an INITIAL invoice for the specified order
     * For a standing order, the initial invoice is the first order and may include one-off items.
     * For a simple order, the initial invoice is identical to a standard invoice.
     *
     **/
    public Invoice createInitialInvoice(Order order, Journal journal) {
        Invoice invoice = new Invoice(order, 0, journal);

        deriveTermsFromOrder(invoice, order);

        // transfer the items from the order to the invoice
        for (CommercialDocumentItem item : order.getItems()) {
            invoice.copyItem(item);
        }

        return invoice;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create an invoice for the recurring component of an invoice.
     *
     * For a standing order, the on-going invoice is the invoice created for the 2nd and subsequent periods.
     * For a non-standing order, there is no ongoing invoice defined.
     *
     * Only items that have a PricingPolicy with a recurring component are included.
     *
     **/
    public Invoice createOngoingInvoice(Order order, int periodNo, Date dateApplied, Journal journal) {
        Invoice invoice = new Invoice(order, periodNo, journal);

        deriveTermsFromOrder(invoice, order);

        invoice.setDateApplied(dateApplied);

        // transfer the items from the order to the invoice
        for (CommercialDocumentItem item : order.getItems()) {
            // if the item is applicable for the requested period
            if ((item.getPrice().hasRecurringComponent()) || (item.getPrice().hasPerOccurrenceComponent())) {
                invoice.copyItem(item);
            }
        }

        return invoice;
    }


    // ------------------------------------------------------------------------------------------------------

    /**
     * Generates a list of the invoices for the specified order.
     *
     * For a simple order there will be only one result, but for a standing order an invoice is created for
     * every period for the duration of the order.
     *
     **/
    public List<Invoice> createInvoices(Order order, Journal journal) {

        List<Invoice> invoices = new LinkedList<Invoice>();
        int periodNo = 0;

        // check if there's any other invoices...
        if (!order.hasRecurringComponent()) {
            // there is only one initial invoice
            invoices.add(createInitialInvoice(order, journal));
        }
        else {
            // a list of invoices will be created
            Recurrence recurrence = order.recurrence();

            Iterator<Date> dateIterator = recurrence.getDateIterator(order.getDateApplied());
            boolean firstDate = true;

            // iterate through all of the dates defined by the recurrence
            while (dateIterator.hasNext()) {
                // get the next date
                Date thisDate = dateIterator.next();
                if (firstDate) {
                    // add the initial invoice
                    invoices.add(createInitialInvoice(order, journal));
                    firstDate = false;
                }
                else {
                    // add one of the on-going invoices
                    invoices.add(createOngoingInvoice(order, periodNo,  thisDate, journal));
                }
                periodNo++;
            }
        }

        return invoices;
    }

    // ------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------
}
