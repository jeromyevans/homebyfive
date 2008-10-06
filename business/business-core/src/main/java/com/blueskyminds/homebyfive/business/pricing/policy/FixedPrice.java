package com.blueskyminds.homebyfive.business.pricing.policy;

import com.blueskyminds.homebyfive.framework.core.datetime.PeriodTypes;
import com.blueskyminds.homebyfive.business.pricing.policy.PricingPolicy;
import com.blueskyminds.homebyfive.business.pricing.Price;

import javax.persistence.*;

/**
 * A fixed one-off price.
 *
 * Date Started: 6/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
@DiscriminatorValue("Fixed")
public class FixedPrice extends PricingPolicy {

    /** The price value */
    private Price price;

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a new fixed price value
     **/
    public FixedPrice(Price price) {
        this.price = price;
    }

    /** Default constructor for ORM */
    protected FixedPrice() {
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /** Returns the fixed component of the pricing policy */
//    @Embedded
//    @AttributeOverrides( {
//        @AttributeOverride(name="money.amount", column = @Column(name="FixedAmount")),
//        @AttributeOverride(name="money.currencyCode", column = @Column(name="FixedCurr")),
//        @AttributeOverride(name="quantity.amount", column = @Column(name="FixedQty")),
//        @AttributeOverride(name="quantity.units", column = @Column(name="FixedQtyUnits"))
//        })
    @Transient
    public Price getFixedComponent() {
        return price;
    }

    public void setFixedComponent(Price price) {
        this.price = price;
    }

    // ------------------------------------------------------------------------------------------------------

    /** There is no recurring component for a FixedPrice  */
    @Transient
    public Price getRecurringComponent() {
        return null;
    }

    public void setRecurringComponent(Price price) {
        // ignored
    }

    // ------------------------------------------------------------------------------------------------------

    /** Return's once-off */
    @Transient
    public PeriodTypes getRecurringComponentPeriod() {
        return PeriodTypes.OnceOff;
    }

    public void setRecurringComponentPeriod(PeriodTypes type) {
        // ignored
    }

    // ------------------------------------------------------------------------------------------------------

    /** There is no per-occurrence component for a simple fixed price */
    @Transient
    public Price getPerOccurrenceComponent() {
        return null;
    }

    public void setPerOccurrenceComponent(Price price) {
        // ignored
    }

    // ------------------------------------------------------------------------------------------------------

    /** Returns the price and period */
    public String toString() {
        return price.toString();
    }

    // ------------------------------------------------------------------------------------------------------
        
}
