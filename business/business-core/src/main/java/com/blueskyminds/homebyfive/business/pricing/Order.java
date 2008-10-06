package com.blueskyminds.homebyfive.business.pricing;

import com.blueskyminds.homebyfive.business.Enterprise;
import com.blueskyminds.homebyfive.business.accounting.FinancialTransaction;
import com.blueskyminds.homebyfive.business.accounting.FinancialTransactionException;
import com.blueskyminds.homebyfive.business.party.Party;
import com.blueskyminds.homebyfive.business.pricing.terms.Terms;
import com.blueskyminds.homebyfive.framework.core.datetime.Interval;
import com.blueskyminds.homebyfive.framework.core.journal.Journal;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import java.io.PrintStream;
import java.util.Date;

/**
 * An order for one or more products
 *
 * This implementation does not contain literal values for prices - only a reference to the price in the
 * contract applied at the time of the order
 *
 * Date Started: 8/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity(name="Orders")
@PrimaryKeyJoinColumn(name="CommercialDocumentId")
public class Order extends CommercialDocument {

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a new empty simple order for the specified party
     * @param journal
     */
    public Order(Party party, Contract contract, Terms terms, Journal journal) {
        super(party, contract, terms, journal);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a new standing order.  A standing order has recurring components and a interval
     */
    public Order(Party party, Contract contract, Date startDate, Interval interval, Terms terms, Journal journal) {
        super(party, contract, terms, journal);
        setDateApplied(startDate);
        setTimespan(interval);
    }

    /** Default Constructor for ORM */
    protected Order() {
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------    
    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    public void print(PrintStream out) {
        int itemNo = 1;
        try {
            out.println("--- "+getIdentityName()+" ---");
            out.println("Date:"+getDateApplied());
            out.println("Interval:"+getTimespan());
            for (CommercialDocumentItem item :getItems()) {
                out.println(itemNo+". "+item.getProduct()+" "+item.getPrice()+ " "+getItemQuantity(item)+" "+getItemGrossValue(item));
                itemNo++;
            }
            out.println("GrossValue:"+grossValue());
            out.println("Terms:"+getTerms());
        }
        catch (InconsistentPricingException e) {
            out.println("Unable to print:"+e.getMessage());
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /** An order has no impact on the financial journal */
    public FinancialTransaction journalise(Enterprise enterprise, String note) throws FinancialTransactionException {
        return null;
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
}
