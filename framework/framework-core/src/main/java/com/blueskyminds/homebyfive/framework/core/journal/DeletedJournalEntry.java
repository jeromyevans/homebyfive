package com.blueskyminds.homebyfive.framework.core.journal;

import com.blueskyminds.homebyfive.framework.core.DomainObject;

import javax.persistence.Entity;
import javax.persistence.DiscriminatorValue;

/**
 *
 * A JournalEntry for the Deleted action
 *
 * Date Started: 5/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
@DiscriminatorValue("Deleted")
public class DeletedJournalEntry extends JournalEntry {

    /**
     * The domain object that was created
     **/
    private DomainObject deleted;

    // ------------------------------------------------------------------------------------------------------

    public DeletedJournalEntry(DomainObject actor, DomainObject deleted, String message) {
        super(actor, message);
        this.deleted = deleted;
    }

    /**
     * Default constructor for ORM
     */
    public DeletedJournalEntry() {
    }

    // ------------------------------------------------------------------------------------------------------

    public String toString() {
        return getActor().getIdentityName()+" deleted "+deleted.getIdentityName()+(getMessage() != null ? getMessage() : "");
    }

    // ------------------------------------------------------------------------------------------------------
}
