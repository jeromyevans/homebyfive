package com.blueskyminds.homebyfive.framework.core.journal;

import com.blueskyminds.homebyfive.framework.core.DomainObject;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * A journal entry for an Added event
 *
 * Date Started: 11/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
@DiscriminatorValue("Added")
public class AddedJournalEntry extends JournalEntry {

     /**
     * The domain object that was created
     **/
    private DomainObject added;

    // ------------------------------------------------------------------------------------------------------

    public AddedJournalEntry(DomainObject actor, DomainObject added, String message) {
        super(actor, message);
        this.added = added;
    }

    /**
     * Default constructor for ORM
     */
    public AddedJournalEntry() {
    }

    // ------------------------------------------------------------------------------------------------------

    public String toString() {
        return getActor().getIdentityName()+" added "+added.getIdentityName()+(getMessage() != null ? getMessage() : "");
    }

    // ------------------------------------------------------------------------------------------------------
}
