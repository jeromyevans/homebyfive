package com.blueskyminds.homebyfive.business.accounting;

import com.blueskyminds.homebyfive.business.pricing.Money;

import javax.persistence.*;
import java.util.List;
import java.util.LinkedList;

/**
 *
 * A financial summary account.
 * The balance of a summary account is the sum of the balance of its children
 *
 * Date Started: 16/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
@DiscriminatorValue("Summary")
public class SummaryAccount extends Account {

    // ------------------------------------------------------------------------------------------------------

    public SummaryAccount(ChartOfAccounts chartOfAccounts, String name, AccountTypes type) {
        super(chartOfAccounts, name, type, chartOfAccounts.getDefaultCurrency());
    }

    /** Default constructor for ORM */
    public SummaryAccount() {

    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the balance of this account.
     * For this summary account, the balance is the sum of the balance of the child relationships
     */
    @Transient // mapped by superclass
    public Money getBalance() {
        Money balance = new Money();
        for (Account account : getChildren()) {
            balance.sum(account.getBalance());
        }
        return balance;
    }

    protected void setBalance(Money balance) {
        // ignored - balance is always calculated
    }

    // ------------------------------------------------------------------------------------------------------

    /** Entries cannot be made into summary relationships.
     *
     * @param entry
     * @return
     * @throws AccountEntryException always
     */
    public boolean makeEntry(AccountEntry entry) throws AccountEntryException {
        throw new AccountEntryException("Attempted to make an entry against a Summary Account");
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns true, emulating that a reversal was successful, even though no entries can actually be
     * made against this account.
     * eg. makeEntry is attempted, fails, transaction rollsback, reverseEntry exits silently */
    public boolean reverseEntry(AccountEntry entry) throws AccountEntryException {
        return true;
    }

    // ------------------------------------------------------------------------------------------------------

    /** A summary account needs to recurse into the entries in the details relationships */
    @Transient // mapped by superclass
    public List<AccountEntry> getEntries() {
        List<AccountEntry> entries = new LinkedList<AccountEntry>();

        for (Account account : getChildren()) {
            entries.addAll(account.getEntries());
        }

        return entries;
    }

    // ------------------------------------------------------------------------------------------------------
}
