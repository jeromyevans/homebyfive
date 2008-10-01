package com.blueskyminds.homebyfive.framework.framework;

import java.util.List;
import java.util.LinkedList;
import java.io.PrintStream;

/**
 *
 * A simple list contains a related collection of Domain Objects
 *
 * The parameter T is the type of domain object
 * This superclass should be extended when implementing a DomainObject that manages a collection of
 *  Domain Objects.
 *
 * Date Started: 8/06/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */

public abstract class DomainObjectList<T extends DomainObject> extends AbstractDomainObject {

    /** The ordered list of DomainObjects */
    private List<T> domainObjects;

    /**
     * Collection of domainObjects that were deleted
     */
    private List<T> deletedDomainObjects;

    // ------------------------------------------------------------------------------------------------------

    /** Initialise a new schedule */
    public DomainObjectList() {
        init();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the Schedule with default attributes
     */
    private void init() {
        domainObjects = new LinkedList<T>();
        deletedDomainObjects = new LinkedList<T>();
    }

    // ------------------------------------------------------------------------------------------------------


    // ------------------------------------------------------------------------------------------------------

    /**
     * Create the domain object specified by including it in this schedule
     *
     * @param domainObject to create by adding to ths schedule
     * @return the new DomainObject, or null if it failed
     */
    protected T create(T domainObject) {

        boolean added = false;

        if (!domainObjects.contains(domainObject)) {
            if (domainObjects.add(domainObject)) {
                added = true;
            }
        }

        if (added) {
            return domainObject;
        }
        else {
            return null;
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Deletes the specified domainObject.
     *
     * @param domainObject
     * @return true if deleted successfully
     */
    protected boolean delete(T domainObject) {

        boolean deleted;

        deleted = remove(domainObject);
        if (deleted) {
            // record the journal entry
        }

        return deleted;
    }

    // ------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------

    /**
     * Removes the specified domainObject from this Schedule
     * The status of the domain object is actually changed to deleted, and the domain object moved to the
     * collection of deleted objects for this schedule.
     *
     * @param domainObject
     * @return true if added
     */
    private boolean remove(T domainObject) {
        boolean removed = false;

        if (domainObjects.contains(domainObject)) {
            removed = domainObjects.remove(domainObject);
            if (removed) {
                domainObject.setStatus(DomainObjectStatus.Deleted);
                deletedDomainObjects.add(domainObject);
            }
        }
        return removed;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Return the number of domain objects in the schedule
     * @return the nmber of domain objects in the schedule
     */
    protected int size() {
        return domainObjects.size();
    }


    // ------------------------------------------------------------------------------------------------------

    /**
     * Completely erases the contents of the schedule, including the domain objects marked as deleted.
     * All state and history is lost.
     */
    protected void clear() {
        domainObjects.clear();
        deletedDomainObjects.clear();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns a list of all the domain objects in this schedule (excluding the deleted ones)
     * @return the List of domain objects
     */
    protected List<T> getDomainObjects() {
        return domainObjects;
    }

    protected void setDomainObjects(List<T> domainObjects) {
        this.domainObjects = domainObjects;
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    public void print(PrintStream out) {
        out.println("--- "+getClass().getSimpleName()+" ---");

        for (T item : getDomainObjects()) {
            item.print(out);
        }
    }

    // ------------------------------------------------------------------------------------------------------
}
