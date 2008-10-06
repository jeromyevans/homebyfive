package com.blueskyminds.business.pricing.policy;

import com.blueskyminds.business.pricing.Price;
import com.blueskyminds.homebyfive.framework.core.datetime.PeriodTypes;

import javax.persistence.Entity;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Transient;

/**
 * A pricing policy for simple Transaction Fees
 *
 * This implementation has a simple fixed price per transaction (per occurrence)
 *
 * Date Started: 14/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
@DiscriminatorValue("Transaction")
public class TransactionFee extends PricingPolicy {

     /**
     * The price value
     */
    private Price price;

    // ------------------------------------------------------------------------------------------------------

    public TransactionFee(Price price) {
        this.price = price;
    }

    /** Default constructor for ORM */
    protected TransactionFee() {

    }

    // ------------------------------------------------------------------------------------------------------

    /** There is no fixed component  */
    @Transient // superclass contains mapping
    public Price getFixedComponent() {
       return null;
    }

    public void setFixedComponent(Price price) {
        // ignored
    }

    // ------------------------------------------------------------------------------------------------------

    /** There is no recurring component */
    @Transient // superclass contains mapping
    public Price getRecurringComponent() {
        return null;
    }

    public void setRecurringComponent(Price price) {
        // ignored
    }

    // ------------------------------------------------------------------------------------------------------

    /** Returns the period of the recurring component */
    @Transient // superclass contains mapping
    public PeriodTypes getRecurringComponentPeriod() {
        return null;
    }

    public void setRecurringComponentPeriod(PeriodTypes type) {
        // ignored
    }

    // ------------------------------------------------------------------------------------------------------

    /** Returns the price of this transaction fee.  This is a fixed amount per occurrence */
    @Transient // superclass contains mapping
    public Price getPerOccurrenceComponent() {
        return price;
    }

    public void setPerOccurrenceComponent(Price price) {
        this.price = price;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Returns the price and period */
    public String toString() {
        return price+"/Transaction";
    }

// ------------------------------------------------------------------------------------------------------

}
