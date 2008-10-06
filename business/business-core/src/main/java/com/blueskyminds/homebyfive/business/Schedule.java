package com.blueskyminds.homebyfive.business;

import com.blueskyminds.homebyfive.framework.core.journal.Journal;
import com.blueskyminds.homebyfive.framework.core.DomainObjectList;
import com.blueskyminds.homebyfive.framework.core.DomainObject;
import com.blueskyminds.homebyfive.business.Enterprise;

/**
 * A schedule manages a related collection of Domain Objects
 *
 * The parameter T is the type of domain object
 * This superclass should be extended when implementing a DomainObject that manages a collection of
 *  Domain Objects.  eg. Accounts, Parties, Licenses
 *
 * The difference between the Schedule and the underlying DomainObjectList is that a schedule contains
 *  top-class lists within an Enterprise
 *
 * Date Started: 6/05/2006
 *
 * History:
 *   8 June 06 - separated the list functionality into a DomainObjectList superclass that can be
 *  used for collections of domain objects that don't need to be aware of the Enterprise
 * 
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public abstract class Schedule<T extends DomainObject> extends DomainObjectList<T> {

   /** The Enterpise that this schedule is for */
    private Enterprise enterprise;

    /** The journal to use for actions performed by this Schedule */
    protected Journal journal;


    // ------------------------------------------------------------------------------------------------------

    /** Initialise a new schedule */
    public Schedule(Enterprise enterprise, Journal journal) {
        this.enterprise = enterprise;
        this.journal = journal;
    }

    /** Default constructor for ORM */
    protected Schedule() {

    }

    // ------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------

    public Journal getJournal() {
        return journal;
    }

    // ------------------------------------------------------------------------------------------------------
    /** Get the enterprise that this schedule was created for */
    public Enterprise getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
}
