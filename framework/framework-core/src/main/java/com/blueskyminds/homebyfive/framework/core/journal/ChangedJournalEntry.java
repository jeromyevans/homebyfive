package com.blueskyminds.homebyfive.framework.core.journal;

import com.blueskyminds.homebyfive.framework.core.DomainObject;

import javax.persistence.Entity;
import javax.persistence.DiscriminatorValue;

/**
 * A journal entry for the Changed action
 *
 * Date Started: 7/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
@DiscriminatorValue("Changed")
public class ChangedJournalEntry extends JournalEntry {

    /**
     * What was changed
     **/
    private DomainObject affectedObject;

    /**
     * The name of the property that changed
     */
    private String propertyName;

    /**
     * From where
     **/
    private DomainObject from;

    /**
     * To where
     **/
    private DomainObject to;


    // ------------------------------------------------------------------------------------------------------

    /** Create a journal entry for a Changed action */
    public ChangedJournalEntry(DomainObject actor, DomainObject affectedObject, String propertyName, DomainObject from, DomainObject to, String message) {
        super(actor, message);
        this.affectedObject = affectedObject;
        this.propertyName = propertyName;
        this.from = from;
        this.to = to;
    }

    /**
     * Default constructor for ORM
     */
    public ChangedJournalEntry() {
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    public String toString() {
        return getActor().getIdentityName()+" changed "+affectedObject.getIdentityName()+"."+propertyName+" from "+(from != null ? from.getIdentityName() : "null")+" to "+to.getIdentityName()+(getMessage() != null ? ":"+getMessage() : "");
    }

    // ------------------------------------------------------------------------------------------------------
}
