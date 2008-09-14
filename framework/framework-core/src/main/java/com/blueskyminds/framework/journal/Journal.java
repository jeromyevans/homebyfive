package com.blueskyminds.framework.journal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.LinkedList;
import java.io.PrintStream;

import com.blueskyminds.framework.journal.DeletedJournalEntry;
import com.blueskyminds.framework.journal.CreatedJournalEntry;
import com.blueskyminds.framework.journal.JournalEntry;
import com.blueskyminds.framework.AbstractDomainObject;
import com.blueskyminds.framework.DomainObject;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * A Journal that may be used to record the actions affecting DomainObjects
 *
 * Date Started: 5/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
//@Entity
public class Journal extends AbstractDomainObject {

    private static final Log LOG = LogFactory.getLog(Journal.class);

    /** The list of journal entries */
    private List<JournalEntry> journalEntries;

    // ------------------------------------------------------------------------------------------------------

    public Journal() {
        init();
    }

    private void init() {
        journalEntries = new LinkedList<JournalEntry>();
    }

    // ------------------------------------------------------------------------------------------------------


    /**
     * Creates a journal entry for the Created action
     * @param actor
     * @param created
     * @param message
     */
    public void created(DomainObject actor, DomainObject created, String message) {
        CreatedJournalEntry entry = new CreatedJournalEntry(actor, created, message);
        addEntry(entry);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Creates a journal entry for the Deleted action
     * @param actor
     * @param deletedObject
     * @param message
     */
    public void deleted(DomainObject actor, DomainObject deletedObject, String message) {
        DeletedJournalEntry entry = new DeletedJournalEntry(actor, deletedObject, message);
        addEntry(entry);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Creates a journal entry for the Transferred action
     * @param actor
     * @param transferred object
     * @param from object
     * @param to object
     * @param message
     */
    public void transferred(DomainObject actor, DomainObject transferred, DomainObject from, DomainObject to, String message) {
        TransferJournalEntry entry = new TransferJournalEntry(actor, transferred, from, to, message);
        addEntry(entry);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Creates a journal entry for the Changed action (changed some from to)
     * @param actor
     * @param affectedObject - the object affected by the change
     * @param propertyName - the name of the object's property that was affected
     * @param from object
     * @param to object
     * @param message
     */
    public void changed(DomainObject actor, DomainObject affectedObject, String propertyName, DomainObject from, DomainObject to, String message) {
        ChangedJournalEntry entry = new ChangedJournalEntry(actor, affectedObject, propertyName, from, to, message);
        addEntry(entry);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Creates a journal entry for the Added action
     * @param actor
     * @param addedObject
     * @param message
     */
    public void added(DomainObject actor, DomainObject addedObject, String message) {
        AddedJournalEntry entry = new AddedJournalEntry(actor, addedObject, message);
        addEntry(entry);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Adds the specified entry to this journal
     * @param entry
     */
    private void addEntry(JournalEntry entry) {
        journalEntries.add(entry);
        LOG.info(entry);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the entries in this journal - for use by ORM */
    @ManyToOne
    protected List<JournalEntry> getJournalEntries() {
        return journalEntries;
    }

    protected  void setJournalEntries(List<JournalEntry> journalEntries) {
        this.journalEntries = journalEntries;
    }

    // ------------------------------------------------------------------------------------------------------
    
    public void print(PrintStream out) {
        out.println("--- Journal ---");
        for (JournalEntry entry : getJournalEntries()) {
            entry.print(out);
        }
    }
}
