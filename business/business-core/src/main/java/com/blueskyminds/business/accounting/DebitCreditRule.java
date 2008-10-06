package com.blueskyminds.business.accounting;

import com.blueskyminds.business.pricing.Money;

/**
 *
 * Interface to rules that dictate whether the balance of an account increases with debits, or with credits.
 *
 * Date Started: 19/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public interface DebitCreditRule {

    /** Adds the entry to the specified balance and returns the new balance using the conventions of the
     * account balance type*/
    public Money sum(Money balance, AccountEntry entry);
}
