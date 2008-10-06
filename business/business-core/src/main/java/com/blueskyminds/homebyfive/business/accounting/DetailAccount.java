package com.blueskyminds.homebyfive.business.accounting;

import com.blueskyminds.homebyfive.business.pricing.Money;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.List;

/**
 *
 * A detail account is an implementation of a Financial Account where the balance is the sum of entries in
 *  the account
 *
 * Date Started: 16/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
@DiscriminatorValue("Detail")
public class DetailAccount extends Account {

    // ------------------------------------------------------------------------------------------------------

    public DetailAccount(ChartOfAccounts chartOfAccounts, String name, AccountTypes type) {
        super(chartOfAccounts, name, type, chartOfAccounts.getDefaultCurrency());
    }

    /** Default constructor for ORM */
    protected DetailAccount() {

    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the balance of this detail account.
     * The balance is the sum of the entries in the account
     * @return balance
     */
    @Transient // mapped in superclass
    public Money getBalance() {
        Money balance = new Money();

        for (AccountEntry entry : getEntries()) {
            // use the debit/credit rule of this account to sum the balance
            balance = getDebitCreditRule().sum(balance, entry);
        }

        if (balance.isNull()) {
            balance = new Money(0.00, getDefaultCurrency());
        }

        return balance;
    }

    protected void setBalance(Money balance) {
        // ignored - balance is always calculated
    }

    // ------------------------------------------------------------------------------------------------------

    /** Make an entry into this account.
     * Entries can only be made against Detail and Memo relationships (not summary relationships) */
    public boolean makeEntry(AccountEntry entry) throws AccountEntryException {
        return addEntry(entry);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Reverse an entry made into this account.
     * The reverse entry simply REMOVES the original entry - this method of reverse is intended only to
     * rollback changes within the context of a single FinancialTransaction.
     * The entry can only be reversed if it was the last entry in this account.
     * */
    public boolean reverseEntry(AccountEntry entry) throws AccountEntryException {
        return removeLastEntry(entry);
    }

    // ------------------------------------------------------------------------------------------------------

    /** A detail account can simply return its own entries */
    @Transient // mapped by superclass
    public List<AccountEntry> getEntries() {
        return entries();
    }

}
