package com.blueskyminds.enterprise.pricing.policy;

import com.blueskyminds.enterprise.pricing.InconsistentPricingException;
import com.blueskyminds.enterprise.pricing.Money;
import com.blueskyminds.enterprise.pricing.Price;
import com.blueskyminds.enterprise.pricing.TaxMap;
import com.blueskyminds.homebyfive.framework.core.AbstractDomainObject;
import com.blueskyminds.homebyfive.framework.core.datetime.Interval;
import com.blueskyminds.homebyfive.framework.core.datetime.PeriodTypes;
import com.blueskyminds.homebyfive.framework.core.measurement.Quantity;

import javax.persistence.*;
import java.io.PrintStream;

/**
 * A policy that determines how something is priced (valued) or charged for
 *
 * Supports both fixed prices and prices with recurring components.
 *
 * Implements caching of calculated values on subsequent calls to get the gross price or tax for the
 * same quantity, no of occurrences and timespan.
 *
 * Date Started: 6/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="PolicyType", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("PricingPolicy")
@AssociationOverrides( {
    @AssociationOverride(name="fixedComponent.taxPolicy", joinColumns = @JoinColumn(name = "FixedTaxPolicyId")),
    @AssociationOverride(name="recurringComponent.taxPolicy", joinColumns = @JoinColumn(name = "RecTaxPolicyId")),
    @AssociationOverride(name="perOccurrenceComponent.taxPolicy", joinColumns = @JoinColumn(name = "PerOccTaxPolicyId"))
})
public abstract class PricingPolicy extends AbstractDomainObject {

    /** Last calculated gross price */
    private transient Money cachedGrossPrice;

    /** Last calculated gross tax, mapped by tax policy */
    private transient TaxMap cachedGrossTax;
    private transient Quantity cachedQuantity;
    private transient int cachedNoOfOccurrences;
    private transient Interval cachedInterval;

    // ------------------------------------------------------------------------------------------------------
    /** Returns the fixed component of the pricing policy */
    @Embedded
    @AttributeOverrides( {
        @AttributeOverride(name="money.amount", column = @Column(name="FixedAmount")),
        @AttributeOverride(name="money.currencyCode", column = @Column(name="FixedCurr")),
        @AttributeOverride(name="quantity.amount", column = @Column(name="FixedQty")),
        @AttributeOverride(name="quantity.units", column = @Column(name="FixedQtyUnits"))
        })

    public abstract Price getFixedComponent();

    public abstract void setFixedComponent(Price price);

    // ------------------------------------------------------------------------------------------------------

    /** Returns the recurring component of the pricing policy */
    @Embedded
    @AttributeOverrides( {
            @AttributeOverride(name="money.amount", column = @Column(name="RecAmount")),
            @AttributeOverride(name="money.currencyCode", column = @Column(name="RecCurr")),
            @AttributeOverride(name="quantity.amount", column = @Column(name="RecQty")),
            @AttributeOverride(name="quantity.units", column = @Column(name="RecQtyUnits")),
            @AttributeOverride(name="taxpolicy", column = @Column(name="RecTaxPolicyId"))
            })
    public abstract Price getRecurringComponent();

    public abstract void setRecurringComponent(Price price);

    @Enumerated
    @Column(name="RecPeriod")
    public abstract PeriodTypes getRecurringComponentPeriod();

    public abstract void setRecurringComponentPeriod(PeriodTypes type);

    /** Returns the component that's included for every occurrence.  eg a per-transaction component.
     * This component is not multiplied by the quantity, although the value may be derived from the
     * quantity ordered */
    @Embedded
    @AttributeOverrides( {
            @AttributeOverride(name="money.amount", column = @Column(name="PerOccAmount")),
            @AttributeOverride(name="money.currencyCode", column = @Column(name="PerOccCurr")),
            @AttributeOverride(name="quantity.amount", column = @Column(name="PerOccQty")),
            @AttributeOverride(name="quantity.units", column = @Column(name="PerOccQtyUnits")),
            @AttributeOverride(name="taxpolicy", column = @Column(name="PerOccTaxPolicyId"))
            })
    public abstract Price getPerOccurrenceComponent();

    public abstract void setPerOccurrenceComponent(Price price);

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /** Initialise the pricing policy with default attributes */
    public PricingPolicy() {
        init();
    }

    // ------------------------------------------------------------------------------------------------------

    /** Initialise the pricing policy with default attributes */
    private void init() {
        cachedGrossTax = new TaxMap();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Determines whether this pricing policy has a recurring component or not
     * @return true if there is a recurring component
     */
    public boolean hasRecurringComponent() {
        return getRecurringComponent() != null;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Determines whether this pricing policy has a component that is include per occurrence
     * eg. a per-transaction fee
     *
     * @return true if there is per-occurrence component
     */
    public boolean hasPerOccurrenceComponent() {
        return getPerOccurrenceComponent() != null;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Determines whether this pricing policy has a fixed component or not
     * @return true if there is a fixed component
     */
    public boolean hasFixedComponent() {
        return getFixedComponent() != null;
    }

    // ------------------------------------------------------------------------------------------------------
    /** Updates the cached values for gross price - the grossTax is assumed to be up to date */
    private void updateCachedValues(Quantity quantity, int noOfOccurrences, Interval interval, Money cachedGrossPrice, TaxMap grossTax) {
        this.cachedGrossPrice = cachedGrossPrice;
        this.cachedQuantity = quantity;
        this.cachedNoOfOccurrences = noOfOccurrences;
        this.cachedInterval = interval;
        this.cachedGrossTax = grossTax;
    }

    // ------------------------------------------------------------------------------------------------------    
    // ------------------------------------------------------------------------------------------------------

    /** Returns true if the cached gross price and tax are not valid */
    @Transient
    private boolean isCached(Quantity quantity, int noOfOccurrences, Interval interval) {
        return ((quantity.equals(cachedQuantity)) && (cachedNoOfOccurrences == noOfOccurrences) && (interval.equals(cachedInterval)));
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /**
     * Calculates the gross price and gross tax based on the specified quantity of goods using this pricing policy
     *
     *  Accounts all of the supported pricing components :
     *   Fixed pricing component
     *   Recurring pricing component and
     *   Per-occurrence pricing component
     *
     * The GrossPrice does not include Tax
     *
     * If the prices have been cached then the cached values are returned instead of recalculating
     *
     * */
    protected void calculatePrices(Quantity quantity, int noOfOccurrences, Interval interval) throws InconsistentPricingException {
        Money grossPrice = new Money();
        TaxMap grossTax = new TaxMap();

        if (!isCached(quantity, noOfOccurrences, interval)) {

            // first, check if there's a fixed component
            if (hasFixedComponent()) {
                // ensure the units match
                if (getFixedComponent().getQuantity().getUnits().equals(quantity.getUnits())) {
                    // units match ok - multiply the price by the quantity and sum it
                    Money thisGrossPrice = getFixedComponent().getMoney().multiply(quantity.getAmount());
                    grossPrice.sum(thisGrossPrice);

                    // calculate the tax on this component
                    grossTax.sum(getFixedComponent().getTaxPolicy(), getFixedComponent().getTaxPolicy().calculateTaxAmount(thisGrossPrice));
                }
                else {
                    throw new InconsistentPricingException("Quantity units and Pricing Quantity units for the same item are different");
                }
            }

            // check if there's a recurring component
            if (hasRecurringComponent()) {
                if (interval != null) {
                    // ensure the quantity units match
                    if (getRecurringComponent().getQuantity().getUnits().equals(quantity.getUnits())) {
                        // check that the period of the recurring price and the interval are consistent
                        if (getRecurringComponentPeriod().equals(interval.getPeriodType())) {
                            // get the recurring price quantity * price
                            Money recurringPrice = getRecurringComponent().getMoney().multiply(quantity.getAmount());
                            int periods = interval.getPeriods();
                            // multiply the price by the number of periods
                            Money thisGrossPrice = recurringPrice.multiply(periods);
                            grossPrice.sum(thisGrossPrice);

                            // calculate the tax on this component
                            grossTax.sum(getRecurringComponent().getTaxPolicy(), getRecurringComponent().getTaxPolicy().calculateTaxAmount(thisGrossPrice));
                        }
                        else {
                            // todo: period conversion could be implemented
                            throw new InconsistentPricingException("Recurring period for a period and interval for the same item are different");
                        }
                    }
                    else {
                        throw new InconsistentPricingException("Quantity units and Pricing Quantity units for the same item are different");
                    }
                }
                else {
                    throw new InconsistentPricingException("Item has a recurring price component but no interval was specified");
                }
            }

            // check if there a per-occurrence component - if there is, multiply it by the number of occurrences
            if (hasPerOccurrenceComponent()) {
                Money thisGrossPrice = getPerOccurrenceComponent().getMoney().multiply(noOfOccurrences);
                grossPrice.sum(thisGrossPrice);

                // calculate the tax on this component
                grossTax.sum(getPerOccurrenceComponent().getTaxPolicy(), getPerOccurrenceComponent().getTaxPolicy().calculateTaxAmount(thisGrossPrice));
            }

            // Update the cached values
            updateCachedValues(quantity, noOfOccurrences, interval, grossPrice, grossTax);
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Calculates the gross price and gross tax based on the specified quantity of goods using this pricing policy
     *
     *  Accounts all of the supported pricing components :
     *   Fixed pricing component
     *   Recurring pricing component and
     *   Per-occurrence pricing component
     *
     * The GrossPrice does not include Tax
     *
     * If the prices have been cached (!isDirty) then the cached values are returned instead of recalculating
     *
     * */
    public Money calculateGrossPrice(Quantity quantity, int noOfOccurrences, Interval interval) throws InconsistentPricingException {
        calculatePrices(quantity, noOfOccurrences, interval);
        return getCachedGrossPrice();
    }

    // ------------------------------------------------------------------------------------------------------

    /** Calculates a gross price based on the specified quantity of goods using this pricing policy
     * Accounts only for a:
     *    fixed pricing component
     *
     * If the price has other components then an InconsistentPricingException may be thrown
     **/
    public Money calculateGrossPrice(Quantity quantity) throws InconsistentPricingException {
        if (hasPerOccurrenceComponent()) {
            throw new InconsistentPricingException("Attempted to calculate a gross price for a policy with per-occurrence components without specifying the number of occurrences");
        }
        if (hasRecurringComponent()) {
            throw new InconsistentPricingException("Attempted to calculate a gross price for a policy with recurring components without specifying the timespan");
        }

        return calculateGrossPrice(quantity, 1, null);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Calculates the gross price based on the specified quantity of goods using this pricing policy
     *
     *  Accounts only for a:
     *    fixed pricing component; and
     *    recurrung pricing component
     *
     * If the price has other components then an InconsistentPricingException may be thrown
     *
     * */
    public Money calculateGrossPrice(Quantity quantity, Interval interval) throws InconsistentPricingException {
        if (hasPerOccurrenceComponent()) {
            throw new InconsistentPricingException("Attempted to calculate a gross price for a policy with per-occurrence components without specifying the number of occurrences");
        }

        return calculateGrossPrice(quantity, 1, interval);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Calculates the gross tax on the price based on the specified quantity of goods using this pricing policy
     *
     *  Accounts all of the supported pricing components :
     *   Fixed pricing component
     *   Recurring pricing component and
     *   Per-occurrence pricing component
     *
     * The GrossPrice does not include Tax
     *
     * */
    public TaxMap calculateGrossTax(Quantity quantity, int noOfOccurrences, Interval interval) throws InconsistentPricingException {
        calculatePrices(quantity, noOfOccurrences, interval);

        return getCachedGrossTax();
    }

    // ------------------------------------------------------------------------------------------------------

     /**
     * Calculates the gross tax on the price based on the specified quantity of goods using this pricing policy
     * Accounts only for a:
     *    fixed pricing component
     *
     * If the price has other components then an InconsistentPricingException may be thrown
     **/
    public TaxMap calculateGrossTax(Quantity quantity) throws InconsistentPricingException {
        if (hasPerOccurrenceComponent()) {
            throw new InconsistentPricingException("Attempted to calculate a gross tax for a policy with per-occurrence components without specifying the number of occurrences");
        }
        if (hasRecurringComponent()) {
            throw new InconsistentPricingException("Attempted to calculate a gross tax for a policy with recurring components without specifying the timespan");
        }

        return calculateGrossTax(quantity, 1, null);
    }

    // ------------------------------------------------------------------------------------------------------

   /** Calculates a gross tax on the price based on the specified quantity of goods using this pricing policy
     *
     *  Accounts only for a:
     *    fixed pricing component; and
     *    recurrung pricing component
     *
     * If the price has other components then an InconsistentPricingException may be thrown
     *
     * */
    public TaxMap calculateGrossTax(Quantity quantity, Interval interval) throws InconsistentPricingException {
         if (hasPerOccurrenceComponent()) {
            throw new InconsistentPricingException("Attempted to calculate a gross tax for a policy with per-occurrence components without specifying the number of occurrences");
        }

        return calculateGrossTax(quantity, 1, interval);
    }

    // ------------------------------------------------------------------------------------------------------

    @Transient
    private Money getCachedGrossPrice() {
        return cachedGrossPrice;
    }

    private void setCachedGrossPrice(Money cachedGrossPrice) {
        this.cachedGrossPrice = cachedGrossPrice;
    }

    @Transient
    private TaxMap getCachedGrossTax() {
        return cachedGrossTax;
    }

    private void setCachedGrossTax(TaxMap cachedGrossTax) {
        this.cachedGrossTax = cachedGrossTax;
    }

    @Transient
    private Quantity getCachedQuantity() {
        return cachedQuantity;
    }

    private void setCachedQuantity(Quantity cachedQuantity) {
        this.cachedQuantity = cachedQuantity;
    }

    @Transient
    private int getCachedNoOfOccurrences() {
        return cachedNoOfOccurrences;
    }

    private void setCachedNoOfOccurrences(int cachedNoOfOccurrences) {
        this.cachedNoOfOccurrences = cachedNoOfOccurrences;
    }

    @Transient
    private Interval getCachedTimespan() {
        return cachedInterval;
    }

    private void setCachedTimespan(Interval cachedInterval) {
        this.cachedInterval = cachedInterval;
    }

    public void print(PrintStream out) {
        out.println(getIdentityName()+" "+toString());
    }

}
