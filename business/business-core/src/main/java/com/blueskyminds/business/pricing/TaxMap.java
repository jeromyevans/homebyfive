package com.blueskyminds.business.pricing;

import com.blueskyminds.business.taxpolicy.TaxPolicy;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Represents amounts of tax mapped to one or more tax policies
 *
 * Date Started: 23/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class TaxMap {

    /** Map between tax policies and balances */
    private Map<TaxPolicy, Money> taxMap;

    // ------------------------------------------------------------------------------------------------------

    /** Craete a new tax map ready to map tax amounts to tax policies */
    public TaxMap() {
        init();
    }

    // ------------------------------------------------------------------------------------------------------

    /** Initialise the taxmap with default attributes */
    private void init() {
        taxMap = new HashMap<TaxPolicy, Money>();
    }

    // ------------------------------------------------------------------------------------------------------

    /** Sums the taxAmount to the grossTax for the specified taxPolicy.  If the TaxPolicy is not in the
     * map a new instance is created with an initial balance of the taxAmount
     *
     * @param taxPolicy
     * @param taxAmount
     * @return the updated amount for that taxPolicy
     */
    public Money sum(TaxPolicy taxPolicy, Money taxAmount) {
        Money currentAmount = taxMap.get(taxPolicy);
        if (currentAmount == null) {
            currentAmount = new Money();
        }
        // add the tax to the amount so far
        Money updatedAmount = currentAmount.sum(taxAmount);

        // update the map
        taxMap.put(taxPolicy, updatedAmount);

        return updatedAmount;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Sums the given taxMap to this taxMap - adds the amount for each taxPolicy to the current amount for
     * each tax policy in this taxmap.
     *
     * @param otherTaxMap
     * @return the updated amount
     */
    public TaxMap sum(TaxMap otherTaxMap) {
        if (otherTaxMap != null) {
            Iterator<TaxPolicy> iterator = otherTaxMap.iterator();
            while (iterator.hasNext()) {
                TaxPolicy taxPolicy = iterator.next();
                sum(taxPolicy, otherTaxMap.getAmount(taxPolicy));
            }
        }
        return this;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Clear the tax map entirely - no tax records */
    public void clear() {
        taxMap.clear();
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the amount of tax for the given tax policy.

     * @param taxPolicy
     * @return Returns null if the policy does not exist in this map at all, otherwise returns the amount
     */
    public Money getAmount(TaxPolicy taxPolicy) {
        return taxMap.get(taxPolicy);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get an iterator on all the taxpolicies in this tax map
     * @return iterator
     */
    public Iterator<TaxPolicy> iterator() {
        return taxMap.keySet().iterator();
    }

    // ------------------------------------------------------------------------------------------------------

}
