package com.blueskyminds.framework.journal;

import com.blueskyminds.framework.AbstractDomainObject;
import com.blueskyminds.framework.DomainObject;

import javax.persistence.Entity;
import javax.persistence.DiscriminatorValue;

/**
 *
 * A JournalEntry for the Created action
 *
 * Date Started: 5/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
@DiscriminatorValue("Created")
public class CreatedJournalEntry extends JournalEntry {

    /**
     * The domain object that was created
     **/
    private DomainObject created;

    // ------------------------------------------------------------------------------------------------------

    public CreatedJournalEntry(DomainObject actor, DomainObject created, String message) {
        super(actor, message);
        this.created = created;
    }

    /**
     * Default constructor for ORM
     */
    public CreatedJournalEntry() {
    }

    // ------------------------------------------------------------------------------------------------------

    public String toString() {
        return getActor().getIdentityName()+" created "+created.getIdentityName()+(getMessage() != null ? getMessage() : "");
    }

    // ------------------------------------------------------------------------------------------------------
}
