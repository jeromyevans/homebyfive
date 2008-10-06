package com.blueskyminds.business.accounting;

import com.blueskyminds.business.pricing.Money;

/**
 * The credit balance rule is used by relationships that increase the balance when credits are applied.
 *
 * Date Started: 19/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class CreditBalanceRule implements DebitCreditRule {

    // ------------------------------------------------------------------------------------------------------

    /** Adds the entry to the specified balance and returns the new balance using the conventions of the
     * account balance type*/
    public Money sum(Money balance, AccountEntry entry) {
        if (entry.getType().equals(AccountEntryTypes.Debit)) {
            // subtract the entry
            balance.minus(entry.getAmount());
        }
        else {
            // add the entry
            balance.sum(entry.getAmount());
        }

        return balance;
    }

    // ------------------------------------------------------------------------------------------------------
}
