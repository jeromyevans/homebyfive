package com.blueskyminds.homebyfive.business.party;

import com.blueskyminds.homebyfive.business.contact.*;
import com.blueskyminds.homebyfive.business.tag.*;
import com.blueskyminds.homebyfive.business.address.Address;
import com.blueskyminds.homebyfive.business.region.tag.RegionTagMap;
import com.blueskyminds.homebyfive.framework.core.AbstractDomainObject;
import com.blueskyminds.homebyfive.framework.core.DomainObject;
import com.blueskyminds.homebyfive.framework.core.MergeUnsupportedException;
import com.blueskyminds.homebyfive.framework.core.tools.filters.Filter;
import com.blueskyminds.homebyfive.framework.core.tools.filters.FilterTools;
import com.blueskyminds.homebyfive.framework.core.transformer.Transformer;

import javax.persistence.*;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.Cascade;

/**
 * A party is a generalisation of a person or organisation
 *
 * Date Started: 26/04/2006
 *
 * History:
 *
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="Impl", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("Party")
public class Party extends AbstractDomainObject implements Taggable, Contactable {

    /**
     * The type of party
     */
    private PartyTypes type;

    /**
     * Points-of-contact for this party
     */
    private Collection<PartyPOC> pointsOfContact;

    /**
     * Individuals associated with this party
     */
    private Collection<IndividualRelationship> people;

    /**
     * Tags assigned to this Party
     */
    private Set<PartyTagMap> tagMaps;

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a new party of the specified type
     * @param type
     */
    public Party(PartyTypes type) {
        setType(type);
        init();
    }

    /** Default constructor for use by ORM */
    protected Party() {
    }

    /**
     * Initialise a party with default attributes
     */
    private void init() {
        pointsOfContact = new HashSet<PartyPOC>();
        people = new HashSet<IndividualRelationship>();
        tagMaps = new HashSet<PartyTagMap>();
    }

    /**
     * Adds a new point of contact of the specified type.
     *
     * @param poc
     * @param type
     * @return true if added ok
     */
    public PointOfContact addPointOfContact(PointOfContact poc, POCType type) {
        boolean added = false;

        PartyPOC pocr = new PartyPOC(this, poc, type);
        if (!pointsOfContact.contains(pocr)) {
            added = pointsOfContact.add(pocr);
        }

        if (added) {
            return poc;
        } else {
            return null;
        }
    }

    /**
     * Adds a new point of contact of the specified type.
     *
     * @param pocRelationship relationship containing the type and PointOfContact. The
     *  IdentityRef and back-reference are ignored
     *
     * @return true if added ok
     */
    protected PointOfContact addPointOfContact(PartyPOC pocRelationship) {
        return addPointOfContact(pocRelationship.getReference(), pocRelationship.getType());
    }

    /**
     * Adds an email address for this Party
     *
     * @param emailAddress
     * @return true if added ok
     */
    public EmailAddress addEmailAddress(EmailAddress emailAddress) {
        return (EmailAddress) addPointOfContact(emailAddress, POCType.EmailAddress);
    }

    /**
     * Adds an email address for this Party
     *
     * @param emailAddress
     * @return true if added ok
     */
    public EmailAddress addEmailAddress(String emailAddress, POCRole role) {
        EmailAddress address = new EmailAddress(emailAddress, role);
        return (EmailAddress) addPointOfContact(address, POCType.EmailAddress);
    }

    /**
     * Adds an address for this Party
     *
     * @param streetAddress
     * @return true if added ok
     */
    public ContactAddress addStreetAddress(ContactAddress streetAddress) {
        return (ContactAddress) addPointOfContact(streetAddress, POCType.Address);
    }

    /**
     * Adds a phone number for this Party
     *
     * @param phoneNumber
     * @return true if added ok
     */
    public PhoneNumber addPhoneNumber(PhoneNumber phoneNumber) {
        return (PhoneNumber) addPointOfContact(phoneNumber, POCType.PhoneNumber);
    }

     /**
     * Adds a phone number for this Party
     *
     * @param phoneNumber
     * @return true if added ok
     */
    public PhoneNumber addPhoneNumber(String phoneNumber, POCRole role, PhoneNumberTypes type) {
        return (PhoneNumber) addPointOfContact(new PhoneNumber(phoneNumber, role, type), POCType.PhoneNumber);
    }

    /**
     * Adds a website for this Party
     *
     * @param website
     * @return true if added ok
     */
    public Website addWebsite(Website website) {
        return (Website) addPointOfContact(website, POCType.Website);
    }

    /**
     * Associates an individual with this party
     *
     * @param individualRelationship
     * @return true if added ok
     */
    public boolean addIndividualRelationship(IndividualRelationship individualRelationship) {
        return people.add(individualRelationship);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Determines if this Party has a relationship with the specified Individual.  If so, returns it.
     *
     * @param individual        individual to find, matched by IdentityRef
     * @return zero or more relationships with the Individual
     */
    public Set<IndividualRelationship> getIndividualRelationships(final Individual individual) {
        List<IndividualRelationship> matches = FilterTools.getMatching(people, new Filter<IndividualRelationship>() {
            public boolean accept(IndividualRelationship relationship) {
                return relationship.getReference().equals(individual);
            }
        });
        return new HashSet<IndividualRelationship>(matches);
    }

    // ------------------------------------------------------------------------------------------------------



    // ------------------------------------------------------------------------------------------------------

    /** Get the type of the party */
    @Enumerated
    @Column(name="Type")
    public PartyTypes getType() {
        return type;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Set the type of this party.  For use by ORM */
    protected void setType(PartyTypes type) {
        this.type = type;
    }

    // ------------------------------------------------------------------------------------------------------

    /** get the points of contact for this party */
    @OneToMany(mappedBy="party", cascade = CascadeType.ALL)
    public Collection<PartyPOC> getPointsOfContact() {
        return pointsOfContact;
    }

    /** set the points of contact for this party - for use by ORM */
    protected void setPointsOfContact(Collection<PartyPOC> pointsOfContact) {
        this.pointsOfContact = pointsOfContact;
    }

    /** Get all the points of contact for this party of the specified type */
    @Transient
    public Set<PointOfContact> getPointsOfContactOfType(final POCType type) {
        // filter to the type and extract out each PointOfContact instance
        List<PointOfContact> matches = FilterTools.getMatchingTransformed(pointsOfContact, new Filter<PartyPOC>() {
            public boolean accept(PartyPOC partyPOC) {
                return (partyPOC.getType().equals(type));
            }
        }, new Transformer<PartyPOC,PointOfContact>() {
            public PointOfContact transform(PartyPOC fromObject) {
                return fromObject.getPointOfContact();
            }
        });
        return new HashSet<PointOfContact>(matches);
    }
    // ------------------------------------------------------------------------------------------------------

    /**
     * Determine if this Party has a point of contact of a specified type
     *
     * @param pointOfContact    Point of Contact to find, matched by IdentityRef
     * @param type
     * @return true if the Party has the PointOfContact
     */
    public boolean hasPointOfContact(final PointOfContact pointOfContact, final POCType type) {
        PartyPOC match = FilterTools.getFirstMatching(pointsOfContact, new Filter<PartyPOC>() {
            public boolean accept(PartyPOC relationship) {
                return (relationship.getType().equals(type)) && (relationship.getPointOfContact().equals(pointOfContact));
            }
        });
        return match != null;
    }

    /**
     * Determine if this Party has at least one point of contact of the specified type
     *
     * This can be useful to check whether the Party has an email address
     *
     * @param type
     * @return true if the Party has the PointOfContact
     */
    public boolean hasPointOfContactOfType(final POCType type) {
        PartyPOC match = FilterTools.getFirstMatching(pointsOfContact, new Filter<PartyPOC>() {
            public boolean accept(PartyPOC relationship) {
                return (relationship.getType().equals(type));
            }
        });
        return match != null;
    }

    /**
     * Determine if this Party has a point of contact of a specified type
     *
     * @param pocRelationship   identifies the PointOfContact and Type.  The identity and back reference are ignored
     * @return true             if the Party has the PointOfContact with that Type
     */
    protected boolean hasPointOfContact(PartyPOC pocRelationship) {
        return hasPointOfContact(pocRelationship.getReference(), pocRelationship.getType());
    }

    /**
     * Gets an instance of the mapping of the specified PointOfContact to this party
     **/
    public PartyPOC getPointOfContactMapping(final PointOfContact pointOfContact) {
        PartyPOC match = FilterTools.getFirstMatching(pointsOfContact, new Filter<PartyPOC>() {
            public boolean accept(PartyPOC partyPOC) {
                return (partyPOC.getPointOfContact().equals(pointOfContact));
            }
        });
        return match;
    }

    /** Get the people associated with this party */
    @OneToMany(mappedBy="party", cascade = CascadeType.ALL)
    public Collection<IndividualRelationship> getPeople() {
        return people;
    }

    /** set the people associated with this party - for use by ORM */
    void setPeople(Collection<IndividualRelationship> people) {
        this.people = people;
    }

    public void print(PrintStream out) {
        out.println(getIdentityName()+" Type:"+getType());
        printPeopleList(out);
        printPointsOfContact(out);
    }

    protected void printPeopleList(PrintStream out) {
        boolean first;
        out.println("---People---");
        for (IndividualRelationship role : people) {
            Individual individual = role.getReference();
            out.print("   "+individual.getIdentityName()+": roles(");
            first = true;
            for (IndividualRole r : role.getRoles()) {
                if (!first) {
                    out.print(",");
                }
                else {
                    first = false;
                }
                out.print(r);
            }
            out.println(")");
        }
    }

    protected void printPointsOfContact(PrintStream out) {
        boolean first;
        out.println("---Points of Contact---");
        for (PartyPOC pocr : pointsOfContact) {
            PointOfContact poc = pocr.getReference();
            out.print("   "+poc.getIdentityName()+": roles(");
            first = true;
            for (POCRole r : poc.getContactRoles()) {
                if (!first) {
                    out.print(",");
                }
                else {
                    first = false;
                }
                out.print(r);
            }
            out.println(")");
        }
    }

    /** Get the set of addresses for this Party */
    @Transient
    public Set<ContactAddress> getAddresses() {
        final List<ContactAddress> addresses;

        addresses = FilterTools.getMatchingTransformed(pointsOfContact, new Filter<PartyPOC>() {
                public boolean accept(PartyPOC relationship) {
                    return POCType.Address.equals(relationship.getType());
                }
            }, new Transformer<PartyPOC, ContactAddress>() {
                    public ContactAddress transform(PartyPOC relationship) {
                        return (ContactAddress) relationship.getReference();
                    }
                });
        return new HashSet<ContactAddress>(addresses);
    }


    /**
     * Merges the PointsOfContact from one collection into another.
     * A new relationship is created for each PointOfContact but the reference is to the same PointOfContact instance
     **/
    private void mergePointsOfContact(Collection<PartyPOC> otherCollection) {
        if (otherCollection != null) {
            for (PartyPOC pocRelationship : otherCollection) {
                if (!hasPointOfContact(pocRelationship)) {
                    // merge
                    addPointOfContact(pocRelationship);
                }
            }
        }
    }

    /**
     * Merges the IndividualRelationships from one collection into another.
     *
     * If a relationship with the same name already exists with an Individual, the existing relationship is
     *  updated with any new Roles for that relationship
     *
     * If no relationship exists with the Individual, a new relationship is created
     *
     **/
    private void mergeRelationships(Collection<IndividualRelationship> otherPeople) {
        if (otherPeople != null) {
            for (IndividualRelationship individualRelationship : otherPeople) {
                Set<IndividualRelationship> existingRelationships = getIndividualRelationships(individualRelationship.getReference());
                if (existingRelationships.size() > 0) {
                    boolean added = false;

                    for (IndividualRelationship existingRelationship : existingRelationships) {
                        // a relationship with the same name exists - if there is a new role for the relationship, add it
                        if (existingRelationship.getName().equals(individualRelationship.getName())) {
                            // merge role with existing relationship
                            existingRelationship.addRoles(individualRelationship.getRoles());
                            added = true;
                        }
                    }

                    // no existing relationships with this name - add a new one
                    if (!added) {
                        addIndividualRelationship(individualRelationship);
                    }
                }
            }
        }
    }

    /**
     * Merge the properties of another Party into this Party.
     * <p/>
     * The basic properties and references to PointsOfContact are merged into this Party
     * 
     * @param other     the party to extract properties from into this object
     * @throws MergeUnsupportedException never
     *          if other isn't a Party
     */
    public <T extends DomainObject> void mergeWith(T other) throws MergeUnsupportedException {
        if (other != null) {
            if (Party.class.isAssignableFrom(other.getClass())) {
                super.mergeSimpleProperties(other);
                mergePointsOfContact(((Party) other).pointsOfContact);
                mergeRelationships(((Party) other).people);
                mergeTags(((Party) other).tagMaps);
            }
        }
    }

    /**
     * The tags that have been assigned to this Party
     **/
    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL)
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    public Set<PartyTagMap> getTagMaps() {
        return tagMaps;
    }

    public void setTagMaps(Set<PartyTagMap> tagMaps) {
        this.tagMaps = tagMaps;
    }

    @Transient
    public Set<Tag> getTags() {
        return TagTools.extractTags(tagMaps);
    }

    public void addTag(Tag tag) {
        if (!TagTools.contains(tagMaps, tag)) {
            tagMaps.add(new PartyTagMap(this, tag));
        }
    }

    public void removeTag(String tagName) {
        PartyTagMap toRemove = null;
        for (PartyTagMap map : tagMaps) {
            if (map.getTag().getName().equals(tagName)) {
                toRemove = map;
            }
        }
        if (toRemove != null) {
            tagMaps.remove(toRemove);
        }
    }

    public boolean hasTag(Tag tag) {
        for (PartyTagMap tagMap : tagMaps) {
            if (tagMap.getTag().equals(tag)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Merge the tags from the other party into this party
     *
     * @param otherTags
     */
    private void mergeTags(Set<PartyTagMap> otherTags) {
         if (otherTags != null) {
            for (PartyTagMap partyTagMap : otherTags) {
                if (!hasTag(partyTagMap.getTag())) {
                    // merge
                    addTag(partyTagMap.getTag());
                }
            }
        }
    }

    /**
     * Get the first point of contact that has the Business Role and is tagged as the Primary contact.
     * If no business point of contact is tagged as the primary role the first is returned
     *
     * @param pointsOfContact
     * @return
     */
    protected String firstBusinessPrimary(Set<PointOfContact> pointsOfContact) {
        return firstPrimary(POCRole.Business, pointsOfContact);
    }

    /**
     * Get the first point of contact that has the Personal Role and is tagged as the Primary contact.
     * If no personal point of contact is tagged as the primary role the first is returned
     *
     * @param pointsOfContact
     * @return
     */
    protected String firstPersonalPrimary(Set<PointOfContact> pointsOfContact) {
        return firstPrimary(POCRole.Personal, pointsOfContact);
    }

    /**
     * Get the first point of contact that has the specified Role and is tagged as the Primary contact.
     * If no point of contact is tagged as the primary role the first is returned
     *
     * @param pointsOfContact
     * @return
     */
    protected String firstPrimary(POCRole role, Set<PointOfContact> pointsOfContact) {
        String result = null;
        boolean first = true;
        for (PointOfContact poc : pointsOfContact) {
            if (poc.hasRole(role)) {
                if (poc.hasTag(TagConstants.PRIMARY)) {
                    result = poc.getValue();
                    break;
                } else {
                    if (first) {
                        // if it's a business contact we'll accept it without a primary tag if no other business
                        // contact has the primary tag
                        result = poc.getValue();
                        first = false;
                    }
                }
            }
        }
        return result;
    }

    /**
     * Get the first point of contact that has the Business Role and is tagged as the Primary contact.
     * If no business point of contact is tagged as the primary role the first is returned
     *
     * @param pointsOfContact
     * @return
     */
    protected Address firstBusinessPrimaryAddress(Set<PointOfContact> pointsOfContact) {
        return firstPrimaryAddress(POCRole.Business, pointsOfContact);
    }

    /**
     * Get the first point of contact that has the Personal Role and is tagged as the Primary contact.
     * If no personal point of contact is tagged as the primary role the first is returned
     *
     * @param pointsOfContact
     * @return
     */
    protected Address firstPersonalPrimaryAddress(Set<PointOfContact> pointsOfContact) {
        return firstPrimaryAddress(POCRole.Personal, pointsOfContact);
    }

    /**
     * Get the first point of contact that has the specified Role and is tagged as the Primary contact.
     * If no point of contact is tagged as the primary role the first is returned
     *
     * @param pointsOfContact
     * @return
     */
    protected Address firstPrimaryAddress(POCRole role, Set<PointOfContact> pointsOfContact) {
        Address result = null;
        boolean first = true;
        for (PointOfContact poc : pointsOfContact) {
            if (poc.hasRole(role)) {
                if (poc.hasTag(TagConstants.PRIMARY)) {
                    result = ((ContactAddress) poc).getAddress();
                    break;
                } else {
                    if (first) {
                        // if it's a business contact we'll accept it without a primary tag if no other business
                        // contact has the primary tag
                        result = ((ContactAddress) poc).getAddress();
                        first = false;
                    }
                }
            }
        }
        return result;
    }

    /**
     * Helper method to access the primary business phone for this Party
     * @return
     */
    @Transient
    public String getPrimaryBusinessPhone() {
        return firstBusinessPrimary(getPointsOfContactOfType(POCType.PhoneNumber));
    }

    /**
     * Helper method to access the primary business fax for this Party
     * @return
     */
    @Transient
    public String getPrimaryBusinessFax() {
        return firstBusinessPrimary(getPointsOfContactOfType(POCType.Fax));
    }

    /**
     * Helper method to access the primary business email for this Party
     * @return
     */
    @Transient
    public String getPrimaryBusinessEmail() {
        return firstBusinessPrimary(getPointsOfContactOfType(POCType.EmailAddress));
    }

    /**
     * Helper method to access the primary business website for this Party
     * @return
     */
    @Transient
    public String getPrimaryBusinessWebsite() {
        return firstBusinessPrimary(getPointsOfContactOfType(POCType.Website));
    }

     /**
     * Helper method to access the primary business website for this Party
     * @return
     */
    @Transient
    public Address getPrimaryBusinessAddress() {
        return firstBusinessPrimaryAddress(getPointsOfContactOfType(POCType.Address));
    }

    /**
     * Helper method to access the primary personal phone for this Party
     * @return
     */
    @Transient
    public String getPrimaryPersonalPhone() {
        return firstPersonalPrimary(getPointsOfContactOfType(POCType.PhoneNumber));
    }

    /**
     * Helper method to access the primary personal fax for this Party
     * @return
     */
    @Transient
    public String getPrimaryPersonalFax() {
        return firstPersonalPrimary(getPointsOfContactOfType(POCType.Fax));
    }

    /**
     * Helper method to access the primary personal email for this Party
     * @return
     */
    @Transient
    public String getPrimaryPersonalEmail() {
        return firstPersonalPrimary(getPointsOfContactOfType(POCType.EmailAddress));
    }

    /**
     * Helper method to access the primary personal website for this Party
     * @return
     */
    @Transient
    public String getPrimaryPersonalWebsite() {
        return firstPersonalPrimary(getPointsOfContactOfType(POCType.Website));
    }

     /**
     * Helper method to access the primary personal website for this Party
     * @return
     */
    @Transient
    public Address getPrimaryPersonalAddress() {
        return firstPersonalPrimaryAddress(getPointsOfContactOfType(POCType.Address));
    }

}


