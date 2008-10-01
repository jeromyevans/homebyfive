package com.blueskyminds.enterprise.accounting;

import com.blueskyminds.enterprise.party.Party;
import com.blueskyminds.enterprise.pricing.Money;
import com.blueskyminds.homebyfive.framework.core.AbstractDomainObject;

import javax.persistence.*;
import java.util.*;

/**
 * A FinancialTransaction applies entries to Accounts
 * A FinancialTransiaction is multi-legged - it may contain two or more entries.
 *
 * The total amounts for credit and debit entries must be balanced within a FinancialTransaction
 * A FinancialTransaction must be atomic.
 *
 * Date Started: 16/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
public class FinancialTransaction extends AbstractDomainObject {

    /** The journal that this transaction belongs to */
    private FinancialJournal financialJournal;

    /** The party that this transaction is associated with */
    private Date dateTime;

    /** The party that this transaction is associated with */
    private Party party;

    /** The note attached to this transaction */
    private String note;

    /** A flag indicating if the transaction has been committed */
    private boolean committed;

    /** The ordered list of entries for this transaction.
     * Althrough order of the entries should have no meaning within a single transaction, the list is
     * ordered for consistency.
     */
    private List<AccountEntry> entries;

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a financial transaction on the specified date consisting of a single debit and credit.
     *
     * It is standard practice to perform the debit first followed by the the credit.
     * The debit and credit amount are equal
     * A note may be included with the transaction
     * The transaction must be associated with a party.
     *
     * The transaction is assigned in ID by the Journal

     * @param dateTime
     * @param debitAccount
     * @param creditAccount
     * @param amount
     * @param note
     * @param party
     */
    public FinancialTransaction(FinancialJournal financialJournal,  Date dateTime, Account debitAccount, Account creditAccount, Money amount, String note, Party party)
            throws FinancialTransactionException {

        init();
        this.financialJournal = financialJournal;
        this.dateTime = dateTime;
        this.note = note;
        this.party = party;

        // create a debit entry
        addEntry(createDebitEntry(party, dateTime, debitAccount, amount));

        // create a credit entry
        addEntry(createCreditEntry(party, dateTime, creditAccount, amount));

        // apply entries
        commit();
    }

    /** Default constructor for ORM */
    protected FinancialTransaction() {

    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a new financial transaction on the specified date without any entries yet - the entries
     * must be added with subsequent calls to addEntry and finally a call to commit
     *
     * It is standard practice to perform the debit first followed by the the credit.
     * The debit and credit amount must be equal
     * A note may be included with the transaction
     * The transaction must be associated with a party.
     *
     * The transaction is assigned in ID by the Journal

     * @param dateTime
     */
    public FinancialTransaction(FinancialJournal financialJournal, Date dateTime, String note, Party party)
            throws FinancialTransactionException {

        init();
        this.financialJournal = financialJournal;
        this.dateTime = dateTime;
        this.note = note;
        this.party = party;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns true if the transaction has been committed
     * @return true if committed
     */
    @Transient
    public boolean isCommitted() {
        return committed;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Add a new debit entry to the transaction
     * This method can only be used with a transaction that hasn't been committed
     */
    public boolean addDebitEntry(Account account, Money amount) throws FinancialTransactionException {
        if (!isCommitted()) {
            return addEntry(createDebitEntry(party, dateTime, account, amount));
        }
        else {
            return false;
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Add a new credit entry to the transaction
     * This method can only be used with a transaction that hasn't been committed
     */
    public boolean addCreditEntry(Account account, Money amount) throws FinancialTransactionException {
        if (!isCommitted()) {
            return addEntry(createCreditEntry(party, dateTime, account, amount));
        }
        else {
            return false;
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Commits the entries in this transaction
     */
    public void commitTransaction() throws FinancialTransactionException {
        commit();
    }

    // ------------------------------------------------------------------------------------------------------

    /** Adds the specified entry to this transaction, if it's not already addded */
    private boolean addEntry(AccountEntry entry) {
        if (!entries.contains(entry)) {
            return entries.add(entry);
        }
        else {
            return false;
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the transaction with default attributes
     */
    private void init() {
        entries = Collections.synchronizedList(new LinkedList<AccountEntry>());
        committed = false;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Creates an instance of an AccountEntry to apply a credit to the specified account.  The entry is not
     * applied to the account.
     *
     * @param party
     * @param date
     * @param account
     * @return the AccountEntry, ready to be applied
     */
    private AccountEntry createCreditEntry(Party party, Date date, Account account, Money amount) throws FinancialTransactionException {
        return new AccountEntry(this, party, account, date, AccountEntryTypes.Credit, amount);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Creates an instance of an AccountEntry to apply a debit to the specified account.  The entry is not
     * applied to the account.
     *
     * @param party
     * @param date
     * @param account
     * @return the AccountEntry, ready to be applied
     */
    private AccountEntry createDebitEntry(Party party, Date date, Account account, Money amount) throws FinancialTransactionException {
        return new AccountEntry(this, party, account, date, AccountEntryTypes.Debit, amount);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Updates an existing instance of an AccountEntry to apply a credit or debit to the specified account.
     *
     * If the entry already exists, the value of the entry is increased by the specified amount.
     *
     * The entry is not applied to the account.
     *
     * @param account
     * @return the AccountEntry if updated, or null if not found
     */
    private AccountEntry updateEntry(Account account, Money amount, AccountEntryTypes type) throws FinancialTransactionException {
        AccountEntry accountEntry = null;
        boolean found = false;
        Iterator<AccountEntry> iterator = entries.iterator();

        // iterate trying to find a matching entry in this transaction
        while ((iterator.hasNext()) && (!found)) {
            AccountEntry entry = iterator.next();

            if ((entry.getAccount().equals(account)) && (entry.getType().equals(type))) {
                // an entry exists for this account already - update it
                accountEntry = entry;
                // add to the amount
                accountEntry.addToAmount(amount);
                found = true;
            }
        }

        return accountEntry;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Creates or updates an instance of an AccountEntry to apply a credit to the specified account.
     *
     * If the entry already exists (same type, same account), the value of the entry is increased by the specified amount.
     *
     * The entry is not applied to the account.
     *
     * @param party
     * @param date
     * @param account
     * @return the AccountEntry, ready to be applied
     */
    public boolean updateCreditEntry(Party party, Date date, Account account, Money amount) throws FinancialTransactionException {
        boolean okay = false;
        AccountEntry entry;

        if (!isCommitted()) {
            entry = updateEntry(account, amount, AccountEntryTypes.Credit);
            if (entry == null) {
                // the entry didn't exist - create a new one
               okay = addEntry(createCreditEntry(party, dateTime, account, amount));
            }
            else {
                // updated ok
                okay = true;
            }
        }

        return okay;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Creates or updates an instance of an AccountEntry to apply a debit to the specified account.
     *
     * If the entry already exists (same type, same account), the value of the entry is increased by the specified amount.
     *
     * The entry is not applied to the account.
     *
     * @param party
     * @param date
     * @param account
     * @return the AccountEntry, ready to be applied
     */
    public boolean updateDebitEntry(Party party, Date date, Account account, Money amount) throws FinancialTransactionException {
        boolean okay = false;
        AccountEntry entry;

        if (!isCommitted()) {
            entry = updateEntry(account, amount, AccountEntryTypes.Debit);
            if (entry == null) {
                // the entry didn't exist - create a new one
               okay = addEntry(createCreditEntry(party, dateTime, account, amount));
            }
            else {
                // updated ok
                okay = true;
            }
        }

        return okay;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Applies the entries in this FinancialTransaction.  The entries are applied atomically.
     *
     * @throws FinancialTransactionException
     */
    private void commit() throws FinancialTransactionException {
        boolean okay = true;
        Exception cause = null;
        // stack for the entries applied so far
        Stack<AccountEntry> entriesApplied = new Stack<AccountEntry>();
        Iterator<AccountEntry> iterator = entries.iterator();

        // iterate through all of the entries, but break out if one of them fails to commit
        while ((okay) && (iterator.hasNext())) {

            AccountEntry entry = iterator.next();
            try {
                // attempt to commit the entry
                okay = commitEntry(entry);

                // record entries applied on the stack
                if (okay) {
                    entriesApplied.push(entry);
                    // update the commited flag
                    committed = true;
                }
            }
            catch (AccountEntryException e) {
                // failed to commit the change with an exception - need to cause a rollback
                okay = false;
                // rememeber the cause - it's reported with within an exception later
                cause = e;
            }
        }

         if (!okay) {
            // transaction failed - rollback changes applied so far
            try {
                if (rollback(entriesApplied)) {
                    // rolled-back successfully - still throw an exception though
                    throw new FinancialTransactionException("Committing a financial transaction failed.  The rollback was successfully completed.", cause);
                }
                else {
                    // failed to roll-back the transaction as well - this is a bad result
                    throw new FinancialTransactionException("Committing a financial transaction FAILED and the ROLLBACK could NOT complete.", cause);
                }
            }
            catch (AccountEntryException e) {
                // failed to roll-back the transaction due to an account entry exception - the cause is reported as well
                throw new FinancialTransactionException("Committing a financial transaction FAILED and the ROLLBACK could NOT complete.", e);
            }
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Attempts to rollback a stack of entries
     * @param entryStack
     * @return true if all rollback attempts were successful, false if something failed preventing rollback
     */
    private boolean rollback(Stack<AccountEntry> entryStack) throws AccountEntryException {
        boolean okay = true;
        while (!entryStack.empty() && (okay)) {
            // pop the next entry off the stack
            AccountEntry entry = entryStack.pop();
            okay = rollbackEntry(entry);
        }

        return okay;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Commits this entry for the financial transaction, causing a change in the balance of the referenced account
     * @return true if the entry was committed successfully
     */
    private boolean commitEntry(AccountEntry entry) throws AccountEntryException {
        return entry.getAccount().makeEntry(entry);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Attempts to rollback an entry for the financial transaction, undoing a change in the balance of the referenced account.
     * @return true if the entry was committed successfully
     */
    private boolean rollbackEntry(AccountEntry entry) throws AccountEntryException {
        return entry.getAccount().reverseEntry(entry);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the note against this transaction */
    @Basic
    @Column(name = "Note")
    public String getNote() {
        return note;
    }

    protected void setNote(String note) {
        this.note = note;
    }

    // ------------------------------------------------------------------------------------------------------

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="Date")
    /** Get the datetime for this transaction */
    public Date getDate() {
        return dateTime;
    }

    protected void setDate(Date dateTime) {
        this.dateTime = dateTime;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the entries in this transaction */
    @OneToMany(mappedBy="financialTransaction", cascade = CascadeType.ALL)
    public List<AccountEntry> getEntries() {
        return entries;
    }

    protected void setEntries(List<AccountEntry> entries) {
        this.entries = entries;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the party that this transaction was created for */
    @ManyToOne
    @JoinColumn(name="PartyId")
    public Party getParty() {
        return party;
    }

    protected void setParty(Party party) {
        this.party = party;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the financial journal that this transaction belongs to */
    @ManyToOne
    @JoinColumn(name="FinancialJournalId")
    public FinancialJournal getFinancialJournal() {
        return financialJournal;
    }

    protected void setFinancialJournal(FinancialJournal financialJournal) {
        this.financialJournal = financialJournal;
    }
}

