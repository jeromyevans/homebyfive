package com.blueskyminds.business.accounting;

import com.blueskyminds.business.pricing.Money;

/**
 * The debit balance rule is used by relationships that increase the balance when debits are applied.
 *
 * Date Started: 19/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class DebitBalanceRule implements DebitCreditRule {

    // ------------------------------------------------------------------------------------------------------

    /** Adds the entry to the specified balance and returns the new balance using the conventions of the
     * account balance type*/
    public Money sum(Money balance, AccountEntry entry) {
        if (entry.getType().equals(AccountEntryTypes.Debit)) {
            // add the entry
            balance.sum(entry.getAmount());
        }
        else {
            // subtract the entry
            balance.minus(entry.getAmount());
        }

        return balance;
    }

    // ------------------------------------------------------------------------------------------------------
}
