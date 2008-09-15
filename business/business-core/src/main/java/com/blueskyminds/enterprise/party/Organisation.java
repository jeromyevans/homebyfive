package com.blueskyminds.enterprise.party;

import com.blueskyminds.framework.DomainObject;
import com.blueskyminds.framework.MergeUnsupportedException;

import javax.persistence.Basic;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.PrintStream;

/**
 * An organisation is a group, company, division etc
 *
 * Date Started: 29/04/2006
 *
 * History:
 *
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
@Entity
@DiscriminatorValue("Organisation")
public class Organisation extends Party {

    /**
     * The organisation name
     */
    private String name;

    // ------------------------------------------------------------------------------------------------------

    /** Create a new Organisation of the specified type and name*/
    public Organisation(PartyTypes type, String name) {
        super(type);
        this.name = name;
    }


    /**
     * Default construtor for use by ORM
     */
    public Organisation(String name) {
        super(PartyTypes.Organisation);
        this.name = name;
    }

    /** Default constructor for ORM */
    public Organisation() {
        super(PartyTypes.Organisation);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the name of the company
     * @return name
     */
    @Basic
    public String getName() {
        return name;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * set the name of the company
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    // ------------------------------------------------------------------------------------------------------

    @Transient
    @Override
    public String getIdentityName() {
        return super.getIdentityName()+" ("+getName()+")";
    }

    // ------------------------------------------------------------------------------------------------------

    public void print(PrintStream out) {
        out.println("Name:"+getName());
        super.print(out);
    }


    /**
     * Merge the properties of another Organisation into this Organisation.
     * <p/>
     * The default implementation throws {@link com.blueskyminds.framework.MergeUnsupportedException}.
     * <p/>
     * An implmentation should copy the non-null values of the other object into this object where the
     * value does not already exist.
     *
     * @param other the object to extract properties from into this object
     * @throws com.blueskyminds.framework.MergeUnsupportedException
     *          when this domain object hasn't implemented the operation
     */
    public <T extends DomainObject> void mergeWith(T other) throws MergeUnsupportedException {
        if (Organisation.class.isAssignableFrom(other.getClass())) {
            super.mergeWith(other);
        } else {
            throw new MergeUnsupportedException(this, other);
        }
    }
}
