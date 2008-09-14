package com.blueskyminds.framework;

/**
 * DomainObject is a layer supertype for the busines domain objects
 *
 * These are persistent entities with an IdentityRef and Status
 *
 * Date Started: 5/05/2006
 *
 * History:
 *
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public interface DomainObject extends HasIdentity, Printable {

    /** returns the unique identifier assigned to this domain object
     * @return a Long identifier, or null if it hasn't been assigned yet.  A null value indicates that the
     *  DomainObject (Entity) is not yet persistent (it's transient)
     **/
    Long getId();

    /**
     * Returns an unique identity for the domain object dervived from the simple class name and the
     * identity
     * @return a unique identifier for the domain object
     */
    String getIdentityName();

    /**
     * Get the status of this AbstractDomainObject
     * @return the status
     */
    DomainObjectStatus getStatus();

    /**
     * Set the status of this AbstractDomainObject
     * @param status of the DomainObject
     */
    void setStatus(DomainObjectStatus status);

    /** Return true if the status of this domain object is 'Deleted'
     * @return true if the status is Deleted
     **/
    boolean isDeleted();

    /** Return true if the status of this domain object is 'Valid'
     * @return true if the status is Valid
     **/
    boolean isValid();

    /** A DomainObject is said to be persistent if it has an Id
     * @return true if the DomainObject has an identity assigned */
    boolean isPersistent();

    /**
     * Get the value for the named property in this domain object, converted to a String
     *
     * @param name
     * @return the property value converted to a string, or null if it's not recognised.
     */
    String getProperty(String name);

}
