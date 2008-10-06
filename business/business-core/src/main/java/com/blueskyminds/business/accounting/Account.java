package com.blueskyminds.business.accounting;

import com.blueskyminds.business.pricing.Money;
import com.blueskyminds.homebyfive.framework.core.AbstractHierarchicalDomainObject;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * A generalised financial account.
 * Implements the Hierarchy interface (Accounts are hierarchical)
 *
 * Date Started: 15/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="AccountClass", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("Account")
public abstract class Account extends AbstractHierarchicalDomainObject<Account> {

    /** The chart of relationships that this account belongs to */
    private ChartOfAccounts chartOfAccounts;

    /** The name given to this account */
    private String name;

    /** The type of this account */
    private AccountTypes type;

    /** The Normal Balance type for this account */
    private AccountBalanceTypes balanceType;

    /** The rule thats used when applying debits or credits to this account, in terms of how the balance is
     * calculated.  This is derived from the balanceType.
     */
    private DebitCreditRule debitCreditRule;

    /** Entries in this account */
    private List<AccountEntry> entries;

    /** The default currency for the account */
    private Currency defaultCurrency;

    // ------------------------------------------------------------------------------------------------------

    public Account(ChartOfAccounts chartOfAccounts, String name, AccountTypes type, Currency defaultCurrency) {
        this.chartOfAccounts = chartOfAccounts;
        this.name = name;
        this.type = type;
        this.defaultCurrency = defaultCurrency;
        init();
    }

    /** Default constructor for ORM */
    protected Account() {

    }

    // ------------------------------------------------------------------------------------------------------

    /** Initialise the account with default attributes */
    private void init() {
        entries = Collections.synchronizedList(new LinkedList<AccountEntry>());

        // setup rules based on the account type
        switch (type) {
            case Asset:
            case Drawings:
            case Expense:
                balanceType = AccountBalanceTypes.Debit;
                debitCreditRule = DebitCreditRuleFactory.createRule(balanceType);
                break;
            case Capital:
            case Liability:
            case Revenue:
                balanceType = AccountBalanceTypes.Credit;
                debitCreditRule = DebitCreditRuleFactory.createRule(balanceType);
                break;
            default:
                throw new UnsupportedOperationException("AccountType is not supported - unable to initialise balance type and debit/credit rules");
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the balance of this account
     */
    @Embedded
    @AttributeOverrides( {
            @AttributeOverride(name="amount", column = @Column(name="BalanceAmt")),
            @AttributeOverride(name="currencyCode", column = @Column(name="BalanceCurr"))
    })
    public abstract Money getBalance();

    protected abstract void setBalance(Money balance);

    // ------------------------------------------------------------------------------------------------------

    /** Make an entry into this account.
     * Entries can only be made against Detail and Memo relationships (not summary relationships) */
    public abstract boolean makeEntry(AccountEntry entry) throws AccountEntryException;

    // ------------------------------------------------------------------------------------------------------

    /** Make a reverse entry into this account.
     * The reverse entry uses the negative of the specified amount.
     * This method of reverse is intended to rollback changes.  */
    public abstract boolean reverseEntry(AccountEntry entry) throws AccountEntryException;

    // ------------------------------------------------------------------------------------------------------

    /** Adds the specified entry to this account.
     * The entry is assumed to have passed its validation already, however it is asserted that the entry
     * is for this account, and that it hasn't already been added to this account. */
    protected boolean addEntry(AccountEntry entry) throws AccountEntryException {
        if (entry == null) {
            throw new AccountEntryException("Attempted to add a null entry to an account");
        }
        if (entry.getAccount() != this) {
            throw new AccountEntryException("Attempted to add an entry to an account with an account mismatch");
        }
        if (entries.contains(entry)) {
            throw new AccountEntryException("Attempted to add an entry to an account when the same entry already exists in the account");
        }
        boolean added = entries.add(entry);

        if (added) {
            // make a journal entry
            chartOfAccounts.getJournal().added(this, entry, null);
        }
        return added;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Deletes the last entry from this account, where the last entry must be the entry this is specified.
     *
     * IMPORTANT: This method is intended for use only within a short transaction to roll-back changes.
     *
     * @return true if removed successfully, or if there were no entries to remove, but false if the
     * last entry does not match the entry specified
     *
     * @throws AccountEntryException if the removal of the entry was attempted (because it matched), but the
     * entry that was actually removed does not equal the entry requested, or if the index of the last entry
     * becomes out-of-bounds during the method execution
     */
    protected boolean removeLastEntry(AccountEntry entry) throws AccountEntryException {
        boolean removed = false;
        int lastIndex = entries.size() - 1;
        if (lastIndex >= 0) {
            // assert that the last entry is the specified entry - if not, return false
            if (entries.get(lastIndex).equals(entry)) {
                try {
                    AccountEntry removedEntry = entries.remove(lastIndex);
                    if (removedEntry.equals(entry)) {
                        // removed successfully
                        removed = true;
                    }
                    else {
                        // something was removed, but it wasn't the requested entry
                        throw new AccountEntryException("Attempted to remove an entry from an Account, the remove operation was performed but the entry removed does not match the entry to be removed");
                    }
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    // the lastIndex was incorrect - this shouldn't be possible as the list of entries is supposed to be
                    // synchronized
                    throw new AccountEntryException("Attempted to remove an entry from an Account but the index of the last entry was out-of-bounds");
                }
            }
        }
        else {
            // no entries
            removed = true;
        }

        if (removed) {
            // make a journal entry
            chartOfAccounts.getJournal().deleted(this, entry, null);
        }

        return removed;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get a list of the entries in this account */
    @OneToMany(mappedBy = "account")
    public abstract List<AccountEntry> getEntries();

    protected void setEntries(List<AccountEntry> entries) {
        this.entries = entries;
    }

    protected List<AccountEntry> entries() {
        return entries;
    }

    // ------------------------------------------------------------------------------------------------------

    @Transient
    public Currency getDefaultCurrency() {
        return defaultCurrency;
    }

    /** Access the currency code for ORM */
    @Basic
    @Column(name="DefaultCurrencyCode", length=4)
    protected String getDefaultCurrencyCode() {
        return defaultCurrency.getCurrencyCode();
    }

    protected void setDefaultCurrencyCode(String currencyCode) {
        defaultCurrency = Currency.getInstance(currencyCode);
    }

    // ------------------------------------------------------------------------------------------------------

    @Enumerated
    @Column(name="Type")
    public AccountTypes getType() {
        return type;
    }

    protected void setType(AccountTypes type) {
        this.type = type;
    }

    // ------------------------------------------------------------------------------------------------------

    @Enumerated
    @Column(name="BalanceType")
    public AccountBalanceTypes getBalanceType() {
        return balanceType;
    }

    public void setBalanceType(AccountBalanceTypes balanceType) {
        this.balanceType = balanceType;
    }

    // ------------------------------------------------------------------------------------------------------

    @Transient
    protected DebitCreditRule getDebitCreditRule() {
        return debitCreditRule;
    }

    // ------------------------------------------------------------------------------------------------------

    @Basic
    @Column(name="Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // ------------------------------------------------------------------------------------------------------

    @ManyToOne
    @JoinColumn(name="ChartOfAccountsId")
    public ChartOfAccounts getChartOfAccounts() {
        return chartOfAccounts;
    }

    protected void setChartOfAccounts(ChartOfAccounts chartOfAccounts) {
        this.chartOfAccounts = chartOfAccounts;
    }

    // ------------------------------------------------------------------------------------------------------

    @ManyToOne
    @JoinColumn(name="ParentId")
    public Account getParent() {
        return super.getParent();
    }

    public void setParent(Account parent) {
        super.setParent(parent);
    }

    // ------------------------------------------------------------------------------------------------------

    @OneToMany(mappedBy="parent", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public Set<Account> getChildren() {
        return super.getChildren();
    }

    protected void setChildren(Set<Account> children) {
        super.setChildren(children);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Print the ledger for this account
     *
     */
    public void printLedger(PrintStream out) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        out.println("--- Ledger: "+getName()+" ---");
        out.println(" Date      | Party             |  Debit  |  Credit |");
        Collection<AccountEntry> unsortedEntries = getEntries();
        Object[] sortedEntries = unsortedEntries.toArray();
        Arrays.sort(sortedEntries, new Comparator() {

            public int compare(Object o1, Object o2) {
                if ((o1 instanceof AccountEntry) && (o2 instanceof AccountEntry)) {
                    AccountEntry a1 = (AccountEntry) o1;
                    AccountEntry a2 = (AccountEntry) o2;

                    return (a1.getDate().compareTo(a2.getDate()));
                }
                else {
                    return -1;
                }
            }
        }
        );

        for (Object sortedEntry : sortedEntries) {
            AccountEntry entry = (AccountEntry) sortedEntry;

            out.print(dateFormat.format(entry.getDate()) + " ");
            out.print(StringUtils.rightPad(entry.getParty().getIdentityName(), 20));
            if (entry.getType().equals(AccountEntryTypes.Debit)) {
                out.print(entry.getAmount().toString());
            } else {
                out.print(StringUtils.leftPad(entry.getAmount().toString(), 20));
            }
            out.println();
        }

    }
}
