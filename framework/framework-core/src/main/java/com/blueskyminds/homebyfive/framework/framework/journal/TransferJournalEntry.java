package com.blueskyminds.homebyfive.framework.framework.journal;

import com.blueskyminds.homebyfive.framework.framework.journal.JournalEntry;
import com.blueskyminds.homebyfive.framework.framework.DomainObject;

import javax.persistence.Entity;
import javax.persistence.DiscriminatorValue;

/**
 * A journal entry for the Transfer action
 *
 * Date Started: 5/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
@DiscriminatorValue("Transferred")
public class TransferJournalEntry extends JournalEntry {

    /**
     * The domain object that was transfered
     **/
    private DomainObject transfered;

    /**
     * From where
     **/
    private DomainObject from;

    /**
     * To where
     **/
    private DomainObject to;


    // ------------------------------------------------------------------------------------------------------

    public TransferJournalEntry(DomainObject actor, DomainObject transfered, DomainObject from, DomainObject to, String message) {
        super(actor, message);
        this.transfered = transfered;
        this.from = from;
        this.to = to;
    }

    /**
     * Default constructor for ORM
     */
    public TransferJournalEntry() {
    }

    // ------------------------------------------------------------------------------------------------------

    public String toString() {
        return getActor().getIdentityName()+" transferred "+transfered.getIdentityName()+" from "+from.getIdentityName()+" to "+to.getIdentityName()+(getMessage() != null ? ":"+getMessage() : "");
    }

    // ------------------------------------------------------------------------------------------------------

}
