package com.blueskyminds.landmine.core.property.advertisement;

import com.blueskyminds.enterprise.address.Address;
import com.blueskyminds.enterprise.party.Individual;
import com.blueskyminds.enterprise.party.Organisation;
import com.blueskyminds.framework.AbstractEntity;
import com.blueskyminds.framework.IgnoreMerge;
import com.blueskyminds.framework.tools.filters.FilterTools;
import com.blueskyminds.framework.tools.ReflectionTools;
import com.blueskyminds.framework.transformer.Transformer;
import com.blueskyminds.landmine.core.property.Premise;
import com.blueskyminds.landmine.core.property.PropertyAdvertisementTypes;
import com.blueskyminds.landmine.core.property.advertisement.AskingPrice;
import com.blueskyminds.landmine.core.property.PremiseAttributeSet;

import javax.persistence.*;
import java.io.PrintStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A property advertisement captures information advertising a property for sale, auction, rent etc.
 *
 * The property advertisement always references a Premise as this reference, not the address, can be
 *   used during queries
 *
 * The property advertisement always belongs to one campaign
 *
 * Date Started: 9/06/2006
 *
 * History:
 *
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd<br/>
 */
@Entity
/*@NamedQueries( {
    @PersistenceNamedQuery(
        name="propertyAdvertisement.mostRecent",
        // most recent PropertyAdvertisment of Type for each Property in Region and AggregateSet,
        // listed between StartDate and EndDate
        query="select pa from PropertyAdvertisement pa, PropertyRegionMap prm, PropertyAggregateSetMap pasm where " +
                     "pa.premise = prm.premise and prm.region = :region and " +
                     "pa.premise = pasm.premise and pasm.aggregateSet = :aggregateSet and " +
                     "pa.dateListed in (select max(pa2.dateListed) from PropertyAdvertisement pa2 where " +
                                              "pa2.premise = pa.premise and " +
                                              "type = :type and pa2.dateListed between :startDate and :endDate group by pa2.premise)"),
    @PersistenceNamedQuery(
        name="propertyAdvertisement.mostRecentPrice",
        // PropertyAdvertisement.Price for the most recent PropertyAdvertisment of Type for each Property in Region
        // and AggregateSet, listed between StartDate and EndDate
        query="select pa.price from PropertyAdvertisement pa, PropertyRegionMap prm, PropertyAggregateSetMap pasm where " +
                     "pa.premise = prm.premise and prm.region = :region and " +
                     "pa.premise = pasm.premise and pasm.aggregateSet = :aggregateSet and " +
                     "pa.dateListed in (select max(pa2.dateListed) from PropertyAdvertisement pa2 where " +
                                              "pa2.premise = pa.premise and " +
                                              "type = :type and pa2.dateListed between :startDate and :endDate group by pa2.premise)"),
    @PersistenceNamedQuery(
        name="propertyAdvertisement.mostRecentInRegion",
        // most recent PropertyAdvertisment of Type for each Property in a specific Region,
        // listed between StartDate and EndDate
        query="select pa from PropertyAdvertisement pa, PropertyRegionMap prm where " +
                     "pa.premise = prm.premise and prm.region = :region and " +
                     "pa.dateListed in (select max(pa2.dateListed) from PropertyAdvertisement pa2 where " +
                                              "pa2.premise = pa.premise and " +
                                              "type = :type and pa2.dateListed between :startDate and :endDate group by pa2.premise)")})*/
public class PropertyAdvertisement extends AbstractEntity {

    private PropertyAdvertisementCampaign campaign;
    private Premise premise;
    private PropertyAdvertisementTypes type;
    private Date dateListed;
    private Date dateUnlisted;
    private Address address;
    private String description;
    private AskingPrice price;
    private PremiseAttributeSet attributes;
        // type
        // beds
        // baths
        // landarea
        // buildarea
        // yearbuilt
    //private List<PropertyFeature> features;

    private Organisation agency;
    private Set<PropertyAdvertisementContact> contacts;

    //private List<Contact> agentContacts;
    //private List<Photo> photos;

    // ------------------------------------------------------------------------------------------------------

    /** Create a new property advertisement, specifying the type but with no values set yet */
    public PropertyAdvertisement(Premise premise, PropertyAdvertisementTypes type) {
        this.premise = premise;
        this.type = type;
        init();
    }

    /** Create a new property advertisement, specifying the type.  THere is no premise reference yet*/
    public PropertyAdvertisement(PropertyAdvertisementTypes type) {
        this.premise = null;
        this.type = type;
        init();
    }

    /** Default constructor for ORM */
    protected PropertyAdvertisement() {
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the PropertyAdvertisement with default attributes
     */
    private void init() {
        contacts = new HashSet<PropertyAdvertisementContact>();
    }

    @ManyToOne
    @JoinColumn(name = "CampaignId")
    @IgnoreMerge /* Don't automatically merge this property */
    public PropertyAdvertisementCampaign getCampaign() {
        return campaign;
    }

    public void setCampaign(PropertyAdvertisementCampaign campaign) {
        this.campaign = campaign;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Associate with advertisement with the specified property */
    public void associateWithPremise(Premise premise) {
        setPremise(premise);
    }

    /** Get the premise that this advertisement is for */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="PremiseId")
    @IgnoreMerge /* Don't automatically merge this property */
    public Premise getPremise() {
        return premise;
    }

    protected void setPremise(Premise premise) {
        this.premise = premise;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the type of property advertisement */
    @Enumerated
    @Column(name="Type")
    public PropertyAdvertisementTypes getType() {
        return type;
    }

    protected void setType(PropertyAdvertisementTypes type) {
        this.type = type;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the date the advertisement was listed */
    @Temporal(TemporalType.DATE)
    @Column(name="DateListed")
    public Date getDateListed() {
        return dateListed;
    }

    public void setDateListed(Date dateListed) {
        this.dateListed = dateListed;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the date the advertisement was removed from listing */
    @Temporal(TemporalType.DATE)
    @Column(name="DateUnlisted")
    public Date getDateUnlisted() {
        return dateUnlisted;
    }

    public void setDateUnlisted(Date dateUnlisted) {
        this.dateUnlisted = dateUnlisted;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the address associated with this advertisement */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="AddressId")
    @IgnoreMerge /* Don't automatically merge this property */
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the description attached to this advertisement */
    @Basic
    @Column(name="Description", length = 8192)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the price included in this advertisement */
    @Embedded
    public AskingPrice getPrice() {
        return price;
    }

    public void setPrice(AskingPrice price) {
        this.price = price;
    }

    // ------------------------------------------------------------------------------------------------------

    @OneToOne(cascade = CascadeType.ALL)  // note: this is unidirectional (no mappedBy parameter)
    @JoinColumn(name="AttributeSetId")
    @IgnoreMerge /* Don't automatically merge this property */
    public PremiseAttributeSet getAttributes() {
        return attributes;
    }

    public void setAttributes(PremiseAttributeSet attributes) {
        this.attributes = attributes;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="AgencyId")
    @IgnoreMerge /* Don't automatically merge this property */
    public Organisation getAgency() {
        return agency;
    }

    public void setAgency(Organisation agency) {
        this.agency = agency;
    }

    /**
     * Get the individuals that are contacts for the property
     * @return
     */
    @OneToMany(mappedBy = "propertyAdvertisement", cascade = CascadeType.ALL)
    protected Set<PropertyAdvertisementContact> getAgentMaps() {
        return contacts;
    }

    protected void setAgentMaps(Set<PropertyAdvertisementContact> agents) {
        this.contacts = agents;
    }

    public boolean addAgent(Individual agent) {
        return contacts.add(new PropertyAdvertisementContact(this, agent));
    }

    @Transient
    public List<Individual> getContacts() {
        return FilterTools.getTransformed(contacts, new Transformer<PropertyAdvertisementContact, Individual>() {
            public Individual transform(PropertyAdvertisementContact propertyAdvertisementAgent) {
                return propertyAdvertisementAgent.getIndividual();
            }
        });
    }

    /**
     * Compares the asking price of this advertisement to the specified asking price.
     * Unlike Comparable, this method returns null if the ValueObjects can't be compared
     *
     * @param askingPrice
     * @return null if either value is null, otherwise -1, 0, or 1
     */
    public Integer compareAskingPrice(AskingPrice askingPrice) {
        if (this.price != null) {
            if (askingPrice != null) {
                 return price.compareTo(askingPrice);
            }
        }
        return null;

    }

    // ------------------------------------------------------------------------------------------------------

    /** Create a duplicate of this property advertisement.  Useful for testing.
     * The duplicate have a new AttributeSet, same address, no premise, no Id
     *
     * @return a new PropertyAdvertisement, very similar to this one
     */
    public PropertyAdvertisement duplicate() {
        PropertyAdvertisement newAdvertisement = new PropertyAdvertisement(this.type);
        newAdvertisement.setAddress(this.address);
        newAdvertisement.setAttributes(attributes.duplicate());
        newAdvertisement.setDateListed(this.dateListed);
        newAdvertisement.setDateUnlisted(this.dateUnlisted);
        newAdvertisement.setDescription(this.description);
        newAdvertisement.setPrice(this.price);

        return newAdvertisement;
    }

    // ------------------------------------------------------------------------------------------------------

    public void print(PrintStream out) {
        out.println(getIdentityName()+": "+getType()+" for "+ (premise != null ? premise.getIdentityName() : "null"));
        DateFormat dateFormat = DateFormat.getDateInstance();
        out.println("DateListed:"+(dateListed != null ? dateFormat.format(dateListed) : "null")+") ");
        out.println("AskingPrice:"+price);
        Address address = getAddress();
        if (address != null) {
            address.print(out);
        } else {
            out.println();
        }
        attributes.print(out);
    }

    /** After persisting the advertisement, update the attributes to reference this source */
    @PostPersist
    void postPersist() {
        if (attributes.getSourceId() == null) {
            attributes.setSourceType(getClass().getSimpleName());
            attributes.setSourceId(getId());
        }
    }

    /**
     * Update the simple properties of this advertisement from the properties of the other advertisement
     * where
     * @param other
     */
    public void updateFrom(PropertyAdvertisement other) {
        ReflectionTools.updateSimpleProperties(this, other);
        attributes.updateFrom(other.getAttributes());
    }
}

