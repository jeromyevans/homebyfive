package com.blueskyminds.enterprise.party;

import com.blueskyminds.homebyfive.framework.framework.DomainObject;
import com.blueskyminds.homebyfive.framework.framework.MergeUnsupportedException;

import javax.persistence.Basic;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.PrintStream;

/**
 * A type of organisation.  A legal entity
 *
 * Date Started: 29/04/2006
 *
 * History:
 *
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
@Entity
@DiscriminatorValue("Company")
public class Company extends Organisation {

    /**
     * The Australian business number for the company
     */
    private String abn;

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a new company with the specified name
     * @param name
     */
    public Company(String name) {
        super(PartyTypes.Company, name);
    }

    /** Default constructor for ORM */
    public Company() {
        setType(PartyTypes.Company);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * get the name of the company
     * @return name
     */
    @Basic
    public String getAbn() {
        return abn;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * set the abn for the company
     * @param abn
     */
    public void setAbn(String abn) {
        this.abn = abn;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Merge the properties of another Company into this Company
     * <p/>     
     *
     * @param other the object to extract properties from into this object
     * @throws com.blueskyminds.homebyfive.framework.framework.MergeUnsupportedException
     *          when this domain object hasn't implemented the operation
     */
    public <T extends DomainObject> void mergeWith(T other) throws MergeUnsupportedException {
        if (Organisation.class.isAssignableFrom(other.getClass())) {
            super.mergeWith(other);
        } else {
            throw new MergeUnsupportedException(this, other);
        }
    }

    // ------------------------------------------------------------------------------------------------------

    public void print(PrintStream out) {
        out.println("Name:"+getName()+" ABN:"+getAbn());
        super.print(out);
    }
}
