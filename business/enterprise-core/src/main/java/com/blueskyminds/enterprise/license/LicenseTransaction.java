package com.blueskyminds.enterprise.license;

import com.blueskyminds.enterprise.party.Party;
import com.blueskyminds.framework.AbstractDomainObject;

import javax.persistence.*;
import java.util.*;


/**
 * A LicenseTransaction performs the exchange of a License between two relationships
 *
 * A License Transaction performs the inclusion and removal of licenses from relationships
 *
 * The transaction applies constraints on the entries:
 *   there are always at least two entries - removal from on account, inclusion in another
 *
 * As each entry is performed the LicenseEntryJournal is updated.
 *
 * Date Started: 30/04/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
public class LicenseTransaction extends AbstractDomainObject {

    /** The journal that this transaction belongs to */
    private LicenseJournal licenseJournal;

    /** Datetime of the transaction */
    private Date dateTime;

    /** The party the transaction was performed for */
    private Party party;

    /** An optional note attached to the transaction */
    private String note;

    /** A flag indicating if the transaction has been committed */
    private boolean committed;

     /**
     * The entries in this transaction
     **/
    private List<LicenseEntry> entries;

    // -------------------------------------------------------------------------------------------------------

    /**
     * Start a new two-entry transaction
     * The entries must be balanced - that is, one must be defined as the contra of the other
     */
    protected LicenseTransaction(LicenseJournal licenseJournal, Date dateTime, String note, Party party) {
        this.licenseJournal = licenseJournal;
        this.dateTime = dateTime;
        this.note = note;
        this.party = party;
        init();
    }

    /** Default constructor for ORM */
    protected LicenseTransaction() {

    }

    // -------------------------------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise a default transaction
     **/
    private void init() {
        entries = Collections.synchronizedList(new LinkedList<LicenseEntry>());
        committed = false;
    }

    // -------------------------------------------------------------------------------------------------------

    /** Add an entry to this transaction to reserve the specified license.
     *
     * Will not add the entry if the transaction has already been committed.
     **/
    public boolean addReserveEntry(LicenseAccount account, License license) {
        if (!isCommitted()) {
            return addEntry(createReserveEntry(account, license));
        }
        else {
            return false;
        }
    }

    // -------------------------------------------------------------------------------------------------------

    /** Add an entry to this transaction to release the specified license */
    public boolean addReleaseEntry(LicenseAccount account, License license) {
        if (!isCommitted()) {
            return addEntry(createReleaseEntry(account, license));
        }
        else {
            return false;
        }
    }

    // -------------------------------------------------------------------------------------------------------

    /** Add an entry to this transaction to allocate the specified license */
    public boolean addAllocateEntry(LicenseAccount account, License license) {
        if (!isCommitted()) {
            return addEntry(createAllocateEntry(account, license));
        }
        else {
            return false;
        }
    }

    // -------------------------------------------------------------------------------------------------------

    /**
     * Adds the specified entry to this transaction.
     *
     * This method is private as adding an entry to the transaction can cause the transaction to be unbalanced
     * It needs to be managed through a strategy.
     * @param entry
     */
    protected boolean addEntry(LicenseEntry entry) {
        if (entry != null) {
            return entries.add(entry);
        }
        else {
            return false;
        }
    }

    // -------------------------------------------------------------------------------------------------------

    /** Create an entry to release the specified license from the account.
     * This will result in an entry to release (unallocate) or unreserve the license, depending on the
     * current allocation of the license.
     *
     * Doesn't check if the action is permitted and only creates the entry to perform the action - it is not
     * actually committed.
     **/
    private LicenseEntry createReleaseEntry(LicenseAccount account, License license) {
        LicenseEntry licenseEntry = null;
        if (account.hasLicenseAllocated(license)) {
            licenseEntry = new LicenseEntry(this, account, license,  LicenseEntryTypes.Release);
        }
        else {
            if (account.hasLicenseReserved(license)) {
                licenseEntry = new LicenseEntry(this, account, license,  LicenseEntryTypes.Unreserve);
            }
        }

        return licenseEntry;
    }

    // -------------------------------------------------------------------------------------------------------

    /**
     * Create an entry to reserve the specified license in the account.
     *
     * Doesn't check if the action is permitted and only creates the entry to perform the action - it is not
     * actually committed.
     **/
    private LicenseEntry createReserveEntry(LicenseAccount account, License license) {
        return new LicenseEntry(this, account, license,  LicenseEntryTypes.Reserve);
    }

    // -------------------------------------------------------------------------------------------------------

    /**
     * Create an entry to allocate the specified license in the account.
     *
     * Doesn't check if the action is permitted and only creates the entry to perform the action - it is not
     * actually committed.
     **/
    private LicenseEntry createAllocateEntry(LicenseAccount account, License license) {
        return new LicenseEntry(this, account, license,  LicenseEntryTypes.Include);
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

    // -------------------------------------------------------------------------------------------------------

    /**
     * Attempts to commit this transaction.
     *
     *  Calls upon each entry to apply its changes.
     */
    public void commit() throws IllegalTransactionException {
        boolean okay = true;
        Exception cause = null;
        // stack for the entries applied so far
        Stack<LicenseEntry> entriesApplied = new Stack<LicenseEntry>();
        Iterator<LicenseEntry> iterator = entries.iterator();

        // iterate through all of the entries, but break out if one of them fails to commit
        while ((okay) && (iterator.hasNext())) {

            LicenseEntry entry = iterator.next();

            // attempt to commit the entry
            okay = commitEntry(entry);

            // record entries applied on the stack
            if (okay) {
                entriesApplied.push(entry);
                // update the commited flag
                committed = true;
            }
        }

         if (!okay) {
            // transaction failed - rollback changes applied so far
            if (rollback(entriesApplied)) {
                // rolled-back successfully - still throw an exception though
                throw new IllegalTransactionException("Committing a license transaction failed.  The rollback was successfully completed.", cause);
            }
            else {
                // failed to roll-back the transaction as well - this is a bad result
                throw new IllegalTransactionException("Committing a license transaction FAILED and the ROLLBACK could NOT complete.", cause);
            }
        }

//        Iterator<LicenseEntry> iterator = entries.iterator();
//        // iterate through the entries - exit if any failures
//        while ((commitOk) && (iterator.hasNext())) {
//            entry = iterator.next();
//            commitOk = entry.doCommit();
//            if (commitOk) {
//                // make a journal entry
//                LicenseEntryJournal.addEntry(entry);
//            }
//        }

        //return commitOk;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Attempts to rollback a stack of entries
     * @param entryStack
     * @return true if all rollback attempts were successful, false if something failed preventing rollback
     */
    private boolean rollback(Stack<LicenseEntry> entryStack) {
        boolean okay = true;
        while (!entryStack.empty() && (okay)) {
            // pop the next entry off the stack
            LicenseEntry entry = entryStack.pop();
            okay = rollbackEntry(entry);
        }

        return okay;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Commits this entry for the license transaction, causing a change in the referenced license account
     * @return true if the entry was committed successfully
     */
    private boolean commitEntry(LicenseEntry entry) {
        return entry.doCommit();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Attempts to rollback an entry for the financial transaction, undoing a change in the referenced account.
     * @return true if the entry was committed successfully
     */
    private boolean rollbackEntry(LicenseEntry entry) {
        return entry.doReverse();
    }

    // -------------------------------------------------------------------------------------------------------

    @ManyToOne
    @JoinColumn(name="LicenseJournalId")
    public LicenseJournal getLicenseJournal() {
        return licenseJournal;
    }

    protected void setLicenseJournal(LicenseJournal licenseJournal) {
        this.licenseJournal = licenseJournal;
    }

    // -------------------------------------------------------------------------------------------------------

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="Date")
    public Date getDate() {
        return dateTime;
    }

    protected void setDate(Date dateTime) {
        this.dateTime = dateTime;
    }

    // -------------------------------------------------------------------------------------------------------

    @ManyToOne
    @JoinColumn(name="PartyId")
    public Party getParty() {
        return party;
    }

    protected void setParty(Party party) {
        this.party = party;
    }

    // -------------------------------------------------------------------------------------------------------
    @Basic
    @Column(name="Note")
    public String getNote() {
        return note;
    }

    protected void setNote(String note) {
        this.note = note;
    }

    // -------------------------------------------------------------------------------------------------------

    @OneToMany(mappedBy = "licenseTransaction", cascade=CascadeType.ALL)
    public List<LicenseEntry> getEntries() {
        return entries;
    }

    protected void setEntries(List<LicenseEntry> entries) {
        this.entries = entries;
    }
}
