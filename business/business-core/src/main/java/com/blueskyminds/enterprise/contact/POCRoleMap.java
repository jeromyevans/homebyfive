package com.blueskyminds.enterprise.contact;

import com.blueskyminds.homebyfive.framework.framework.AbstractDomainObject;

import javax.persistence.*;

/**
 * Maps a POCRole to a point of contact
 *
 * This is just a mapping class
 *
 * Date Started: 10/06/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
@Entity(name = "POCRoleMap")
public class POCRoleMap extends AbstractDomainObject {

    private PointOfContact pointOfContact;
    private POCRole contactRole;

    public POCRoleMap(PointOfContact pointOfContact, POCRole contactRole) {
        this.pointOfContact = pointOfContact;
        this.contactRole = contactRole;
    }

    /** Default constructor for ORM */
    protected POCRoleMap() {
    }


    @ManyToOne
    @JoinColumn(name="PointOfContactId")
    public PointOfContact getPointOfContact() {
        return pointOfContact;
    }

    public void setPointOfContact(PointOfContact pointOfContact) {
        this.pointOfContact = pointOfContact;
    }

    @Enumerated
    @Column(name="POCRole")
    public POCRole getContactRole() {
        return contactRole;
    }

    public void setContactRole(POCRole contactRole) {
        this.contactRole = contactRole;
    }
}
