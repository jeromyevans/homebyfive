package com.blueskyminds.framework.journal;

import com.blueskyminds.framework.AbstractDomainObject;
import com.blueskyminds.framework.DomainObject;

import javax.persistence.*;
import java.util.Date;

/**
 * An entry into the Journal for auditing actions
 *
 * Date Started: 5/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
//@Entity
//@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name="EntryType", discriminatorType = DiscriminatorType.STRING)
//@DiscriminatorValue("JournalEntry")
public abstract class JournalEntry extends AbstractDomainObject {

    /**
     * The datetime of this journal entry
     */
    private Date dateApplied;

    /**
     * The DomainObject that performed the action
     */
    private DomainObject actor;

    /**
     * An optional message for this journal entry
     **/
    private String message;

    // --------------------------------------------------------------------------------------------------------

    public JournalEntry(DomainObject actor, String message) {
        this.actor = actor;
        this.message = message;
        this.dateApplied = new Date();
    }

    /** Default constructor for ORM */
    protected JournalEntry() {
    }

    // --------------------------------------------------------------------------------------------------------
    /** Get the date of this journal entry */
    public Date getDateApplied() {
        return dateApplied;
    }

    // --------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------------------

    /** Get the date of this journal entry */
    public void setDateApplied(Date dateApplied) {
        this.dateApplied = dateApplied;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // --------------------------------------------------------------------------------------------------------

    public DomainObject getActor() {
        return actor;
    }

    public void setActor(AbstractDomainObject actor) {
        this.actor = actor;
    }

    // --------------------------------------------------------------------------------------------------------

    public String toString() {
        return getActor().getIdentityName()+" "+getClass().getSimpleName()+(getMessage() != null ? ":"+getMessage() : "");
    }

    // --------------------------------------------------------------------------------------------------------

}
