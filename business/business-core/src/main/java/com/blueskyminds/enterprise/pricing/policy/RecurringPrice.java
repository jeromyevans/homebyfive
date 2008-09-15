package com.blueskyminds.enterprise.pricing.policy;

import com.blueskyminds.framework.datetime.PeriodTypes;
import com.blueskyminds.enterprise.pricing.policy.PricingPolicy;
import com.blueskyminds.enterprise.pricing.Price;

import javax.persistence.*;

/**
 * A recurring pricing policy
 * eg. price per month
 *
 * Date Started: 6/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
@DiscriminatorValue("Recurring")              
public class RecurringPrice extends PricingPolicy {

    /**
     * The recurring price value
     */
    private Price price;

    /**
     * When the price recurs
     */
    private PeriodTypes periodType;

    // ------------------------------------------------------------------------------------------------------

    public RecurringPrice(Price price, PeriodTypes periodType) {
        this.price = price;
        this.periodType = periodType;
    }

    /** Default constructor for ORM */
    protected RecurringPrice() {
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

    /** Returns the recurring component of the pricing policy */
    @Transient // superclass contains mapping
    public Price getRecurringComponent() {
        return price;
    }

    public void setRecurringComponent(Price price) {
        this.price = price;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Returns the recurring period for this price */
    @Transient // superclass contains mapping
    public PeriodTypes getRecurringComponentPeriod() {
        return periodType;
    }

    public void setRecurringComponentPeriod(PeriodTypes type) {
        this.periodType = type;
    }

    // ------------------------------------------------------------------------------------------------------

    /** There is no per-occurrence component for a simple recurruring price */
    @Transient // superclass contains mapping
    public Price getPerOccurrenceComponent() {
        return null;
    }

    public void setPerOccurrenceComponent(Price price) {
        // ignored
    }

    // ------------------------------------------------------------------------------------------------------

    /** Returns the price and period */
    public String toString() {
        return price+"/"+periodType;
    }

}
