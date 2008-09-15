package com.blueskyminds.enterprise.accounting;

/**
 * The debit/credit rule determines whether debits or credits increase the balance of an account.
 * There's only two possible implementations:
 *   - debits increase the balance and credits decrease it; or
 *   - credits increase the balance and debits decrease it
 *
 * The convention for standard double-entry bookkeeping is:
 *   Assets:          Debit for increase, credit for decrease
 *   Liabilities:     Debit for decrease, credit for increase
 *   Capital:         Debit for decrease, credit for increase
 *   Drawings:        Debit for increase, credit for decrease
 *   Revenue:         Debit for decrease, credit for increase
 *   Expenses:        Debit for increase, credit for decrease
 *
 * Normal balances:
 *   Assets:                     Debit
 *   Liabilities:                       Credit
 *   Owner's Equity - overall           Credit
 *      Capital:                        Credit
 *      Drawings:                Debit
 *      Revenue:                        Credit
 *      Expenses:                Debit
 *
 * Date Started: 19/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class DebitCreditRuleFactory {

    // Current implementation uses singleton instances, but there's no reason why this needs to be enforced
    private static final DebitCreditRule debitRule = new DebitBalanceRule();

    private static final DebitCreditRule creditRule = new CreditBalanceRule();

    // ------------------------------------------------------------------------------------------------------

    /** Factory method to create a rule based on the balance type */
    public static DebitCreditRule createRule(AccountBalanceTypes balanceType) {
        if (balanceType.equals(AccountBalanceTypes.Debit)) {
            return debitRule;
        }
        else {
            if (balanceType.equals(AccountBalanceTypes.Credit)) {
                return creditRule;
            }
            else {
                throw new UnsupportedOperationException("Attempted to create a Debit/Credit for an unsupported balance type");
            }
        }
    }

    // ------------------------------------------------------------------------------------------------------

}
