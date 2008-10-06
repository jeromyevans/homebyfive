package com.blueskyminds.business.contact;

import com.blueskyminds.homebyfive.framework.core.TypedRelationship;
import com.blueskyminds.homebyfive.framework.core.AbstractEntity;
import com.blueskyminds.business.party.Party;

import javax.persistence.*;

/**
 * Association between a party and one of their points-of-contact.
 *
 * The relationship is typed (implements TypedRelationship)
 *
 * Date Started: 4/05/2006
 *
 * History:
 *
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
@Entity
@Table(name="PartyPOC")
public class PartyPOC extends AbstractEntity implements TypedRelationship<Party, PointOfContact, POCType>, POCMap {

    /**
     * To the party that has this contact point relationship
     */
    private Party party;

    /**
     * The point of contact
     */
    private PointOfContact pointOfContact;

    /**
     * The type of relationship
     */
    private POCType type;

    // ------------------------------------------------------------------------------------------------------

    /**
     * Creates a new point of contact relationship between the party and point of contact.
     * The type is used to identify the relationship type. eg. that the POC is an EmailAddress
     * @param party
     * @param pointOfContact
     * @param type
     */
    public PartyPOC(Party party, PointOfContact pointOfContact, POCType type) {
        this.party = party;
        this.pointOfContact = pointOfContact;
        this.type = type;
    }

    /** Default constructor for use by ORM */
    protected PartyPOC() {
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the party that has this contact point relationship
     */
    @Transient
    public Party getPrimary() {
        return party;
    }

    protected void setPrimary(Party party) {
        this.party = party;
    }

    @ManyToOne
    @JoinColumn(name="PartyId")
    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    // ------------------------------------------------------------------------------------------------------

    /** The reference int he TypedRelationship is the PointOfContact */
    @Transient
    public PointOfContact getReference() {
        return pointOfContact;
    }

    protected void setReference(PointOfContact pointOfContact) {
        this.pointOfContact = pointOfContact;
    }

    /**
     * Get the point of contact
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="POCID")
    public PointOfContact getPointOfContact() {
        return pointOfContact;
    }

    protected void setPointOfContact(PointOfContact pointOfContact) {
        this.pointOfContact = pointOfContact;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
    * Get the type of relationship
    **/
    @Enumerated(EnumType.ORDINAL)
    @Column(name="POCType")
    public POCType getType() {
      return type;
    }

    /**
    * Set the type, for use by ORM
    * @param type
    */
    protected void setType(POCType type) {
      this.type = type;
    }

    // ------------------------------------------------------------------------------------------------------
}
