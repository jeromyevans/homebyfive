package com.blueskyminds.landmine.core.property;

import com.blueskyminds.enterprise.address.Address;
import com.blueskyminds.enterprise.region.suburb.SuburbHandle;
import com.blueskyminds.enterprise.tag.Taggable;
import com.blueskyminds.enterprise.tag.Tag;
import com.blueskyminds.enterprise.tag.TagTools;
import com.blueskyminds.framework.AbstractHierarchicalDomainObject;
import com.blueskyminds.framework.measurement.Area;
import com.blueskyminds.framework.tools.filters.FilterTools;
import com.blueskyminds.framework.tools.filters.Filter;
import com.blueskyminds.framework.transformer.Transformer;
import com.blueskyminds.landmine.core.property.events.*;
import com.blueskyminds.landmine.core.property.advertisement.PropertyAdvertisementCampaign;
import com.blueskyminds.landmine.core.property.advertisement.PropertyAdvertisement;
import com.blueskyminds.landmine.core.property.tag.PremiseTagMap;
import com.blueskyminds.landmine.core.property.assets.PremiseAssetMap;
import com.blueskyminds.landmine.core.property.assets.PremiseAsset;
import com.blueskyminds.landmine.core.property.assets.PremiseAssetTools;
import com.blueskyminds.landmine.core.property.assets.PremiseAssetMapType;

import javax.persistence.*;
import java.io.*;
import java.util.*;

import org.apache.commons.lang.time.DateUtils;


/**
 * A physical premise, as in real-estate property.
 *
 * Premise is designed to permit its attributes to change over time to model the real-world for property
 *
 *   For example, the number of bedrooms may change, the property type may change, and even the address may
 *  change (or it may have multiple addresses).
 *
 * The Premise is also the anchor point for artifacts related to the property - for example, advertisements
 *  when it's for sale
 *
 * Date Started: 16/04/2006
 *
 * History:
 *
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd<br/>
 **/
@Entity
public class Premise extends AbstractHierarchicalDomainObject<Premise> implements PropertyAttributes, Taggable {

    /**
     * The addresses mapped to this property.  The mapping is used to associate a timestamp
     * with each address as applied to this property
     **/
    private Set<PremiseAddressMap> addresses;

    /**
     * The attributes specified for this property.  Each attribute set also has a timestamp applied
     **/
    private Set<PremiseAttributeSetMap> attributes;

    /**
     * The advertisement campaigns created for this premise
     */
    private Set<PropertyAdvertisementCampaign> advertisementCampaigns;

    /**
     * Events recorded for this premise
     */
    private Set<PremiseEvent> events;

    /**
     * Tags assigned to this Premise
     */
    private Set<PremiseTagMap> tagMaps;

    /**
     * Current assets for this Premise
     */
    private Set<PremiseAssetMap> assetMaps;

    // ------------------------------------------------------------------------------------------------------

    public Premise() {
        init();
    }

    // ------------------------------------------------------------------------------------------------------

    /** Initialise the real property with default attributes */
    private void init() {
        addresses = new HashSet<PremiseAddressMap>();
        attributes = new HashSet<PremiseAttributeSetMap>();
        advertisementCampaigns = new HashSet<PropertyAdvertisementCampaign>();
        events = new HashSet<PremiseEvent>();
        tagMaps = new HashSet<PremiseTagMap>();
        assetMaps = new HashSet<PremiseAssetMap>();
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ParentId")
    public Premise getParent() {
        return super.getParent();
    }

    public void setParent(Premise parent) {
        super.setParent(parent);
    }

    @OneToMany(mappedBy="parent", cascade = CascadeType.ALL)
    public Set<Premise> getChildren() {
        return super.getChildren();
    }

    protected void setChildren(Set<Premise> children) {
        super.setChildren(children);
    }

    // ------------------------------------------------------------------------------------------------------

    @OneToMany(mappedBy = "premise", cascade = CascadeType.ALL)
    protected Set<PremiseAddressMap> getAddresses() {
        return addresses;
    }

    protected void setAddresses(Set<PremiseAddressMap> addresses) {
        this.addresses = addresses;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Associate the given attributes with this property */
    public boolean associateAddress(Address address, Date dateApplied) {
        return (addresses.add(new PremiseAddressMap(this, address,  dateApplied)));
    }

    // ------------------------------------------------------------------------------------------------------

    /** Associate the given attributes with this property */
    public boolean associateWithAttributes(PremiseAttributeSet spec) {
        spec.setPremise(this);
        return (attributes.add(new PremiseAttributeSetMap(this, spec)));
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /** Get the current address for this property.  The current address is the address most recently
     * applied to the property. */
    @Transient
    public Address getAddress() {
        Address mostRecent = null;
        Date mostRecentDate = null;

        for (PremiseAddressMap pam : addresses) {
            if ((mostRecentDate == null) || (pam.getDateApplied().after(mostRecentDate))) {
                if (pam.getAddress().isValid()) {
                    // if the most recent hasn't been set yet, or the address is newer than the last set, use this address
                    mostRecentDate = pam.getDateApplied();
                    mostRecent = pam.getAddress();
                }
            }            
        }

        return mostRecent;
    }

    /**
     * List of known addresses for the premise ordered by date applied desc
     *
     * @return
     */
    @Transient
    public List<Address> getAddressList() {
        List<Address> addressList = new LinkedList<Address>();
        List<PremiseAddressMap> addressMap = new ArrayList<PremiseAddressMap>(addresses.size());
        for (PremiseAddressMap pam : addresses) {
            if (!addressList.contains(pam.getAddress())) {
                addressList.add(pam.getAddress());
                addressMap.add(pam);
            }
        }

        // sort by date applied
        Collections.sort(addressMap, new Comparator<PremiseAddressMap>() {
            public int compare(PremiseAddressMap o1, PremiseAddressMap o2) {
                return o1.getDateApplied().compareTo(o2.getDateApplied())*-1;
            }
        });

        addressList.clear();
        for (PremiseAddressMap pam : addressMap) {
            addressList.add(pam.getAddress());
        }

        return addressList;
    }

    // ------------------------------------------------------------------------------------------------------

   // ------------------------------------------------------------------------------------------------------

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "premise")
    protected Set<PremiseAttributeSetMap> getPremiseAttributeSetMaps() {
        return attributes;
    }

    protected void setPremiseAttributeSetMaps(Set<PremiseAttributeSetMap> attributes) {
        this.attributes = attributes;
    }

    @Transient
    protected Set<PremiseAttributeSet> getAttributes() {
        return new HashSet<PremiseAttributeSet>(FilterTools.getTransformed(attributes, new Transformer<PremiseAttributeSetMap, PremiseAttributeSet>() {
            public PremiseAttributeSet transform(PremiseAttributeSetMap fromObject) {
                return fromObject.getAttributeSet();
            }
        }));
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /** Get the value of the PropertyAttribute of the specified class in this consolidated feature spec */
    private Object lookupMostRecentAttributeOfType(PropertyAttributeTypes type) {
        Object mostRecentAttribute = null;
        Date mostRecentDate = null;
        Object attribute;

        for (PremiseAttributeSetMap map : attributes) {
            PremiseAttributeSet spec = map.getAttributeSet();
            if (spec.isValid()) {
                if ((mostRecentDate == null) || (spec.getDateApplied().after(mostRecentDate))) {
                    // check if the attribute exists in this spec...
                    attribute = spec.getAttributeOfType(type);
                    if (attribute != null) {
                        // if the most recent hasn't been set yet, or the attribute is newer than the last one set, use it
                        mostRecentDate = spec.getDateApplied();
                        mostRecentAttribute = attribute;
                    }
                }
            }
        }

        return mostRecentAttribute;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Gets the attribute of the specified type from the consolidated list that is the most
     * recently applied, and returns its value assuming the return type is the specified class
     *
     * @param type
     * @param valueClass
     * @return the value, or null if not defined or wrong class
     */
    @SuppressWarnings({"unchecked"})
    private  <T> T lookupMostRecentAttributeOfType(PropertyAttributeTypes type, Class<T> valueClass) {
        Object attribute = lookupMostRecentAttributeOfType(type);

        if (attribute != null) {
            try {
                return (T) attribute;
            } catch (ClassCastException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the current property type.  The current type is the type most recently
     * applied to the property. */
    @Transient
    public PropertyTypes getType() {
        return lookupMostRecentAttributeOfType(PropertyAttributeTypes.PropertyType, PropertyTypes.class);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the current number of bedrooms for the property.  */
    @Transient
    public Integer getBedrooms() {
        return lookupMostRecentAttributeOfType(PropertyAttributeTypes.Bedrooms, Integer.class);
    }

    /** Get the current number of bathrooms for the property.  */
    @Transient
    public Integer getBathrooms() {
        return lookupMostRecentAttributeOfType(PropertyAttributeTypes.Bathrooms, Integer.class);
    }

    /** Get the current number of carspaces for the property.  */
    @Transient
    public Integer getCarspaces() {
        return lookupMostRecentAttributeOfType(PropertyAttributeTypes.Carspaces, Integer.class);
    }

    /** Get the current number of storeys for the property.  */
    @Transient
    public Integer getStoreys() {
        return lookupMostRecentAttributeOfType(PropertyAttributeTypes.Storeys, Integer.class);
    }

    @Transient
    public Integer getNoOfUnits() {
        return lookupMostRecentAttributeOfType(PropertyAttributeTypes.NoOfUnits, Integer.class);
    }

    /** Get the building area for the property.  */
    @Transient
    public Area getBuildingArea() {
        return lookupMostRecentAttributeOfType(PropertyAttributeTypes.BuildingArea, Area.class);
    }

    @Transient
    public Area getGaragesArea() {
        return lookupMostRecentAttributeOfType(PropertyAttributeTypes.GaragesArea, Area.class);
    }

    @Transient
    public Area getVerandaArea() {
        return lookupMostRecentAttributeOfType(PropertyAttributeTypes.VerandaArea, Area.class);
    }

    @Transient
    public Area getCommonBuildingArea() {
        return lookupMostRecentAttributeOfType(PropertyAttributeTypes.CommonBuildingArea, Area.class);
    }

    /** Get the building area for the property.  */
    @Transient
    public Area getLandArea() {
        return lookupMostRecentAttributeOfType(PropertyAttributeTypes.LandArea, Area.class);
    }

    /** Get the construction date for the property.  */
    @Transient
    public Date getConstructionDate() {
        return lookupMostRecentAttributeOfType(PropertyAttributeTypes.ConstructionDate, Date.class);
    }

    /**
      * Returns the property attribute in this bean for the specified attribute type.
      *
      * @return The attribute value, or null of it's not defined
      */
    @Transient
    public Object getAttributeOfType(PropertyAttributeTypes type) {
        return PropertyAttributesHelper.getAttributeOfType(this, type);
    }

    /**
     * Detect if any of the new attributes alter the current attributes of this premise
     *
     * Null values are ignored (no change)
     *
     * @param newAttributes
     * @return
     */
    public boolean attributesChangedBy(PropertyAttributes newAttributes) {
        for (PropertyAttributeTypes type : PropertyAttributeTypes.values()) {
            Object newValue = newAttributes.getAttributeOfType(type);
            if (newValue != null) {
                Object currentValue = getAttributeOfType(type);
                if (!newValue.equals(currentValue)) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Set the type of this premise by creating a new PremiseAttributeset applied from this moment */
    public void setType(PropertyTypes type, String sourceType, Long sourceId) {
        if (type != null) {
            associateWithAttributes(new PremiseAttributeSet(new Date(), type, sourceType, sourceId));
        }
    }

    /**
     * Get the events applied to this property
     * @return
     */
    @OneToMany(mappedBy = "premise", cascade = CascadeType.ALL)
    public Set<PremiseEvent> getEvents() {
        return events;
    }

    public void setEvents(Set<PremiseEvent> events) {
        this.events = events;
    }

    public PremiseEvent addEvent(PremiseEvent event) {
        if (events.add(event)) {
            return event;
        } else {
            return null;
        }
    }

    /**
     * List the events on or before the specified date. The list is NOT ordered.
     * @param when
     * @param eventType     the type of event or null for all
     * @return
     */
    public List<PremiseEvent> listEventsOnOrBefore(final Date when, final Class<?> eventType) {
        return FilterTools.getMatching(events, new Filter<PremiseEvent>() {
            public boolean accept(PremiseEvent event) {
                Date dateApplied = event.getDateApplied();
                if (dateApplied != null) {
                    if ((eventType == null) || (eventType.isAssignableFrom(event.getClass()))) {
                        return event.getDateApplied().before(when) || (DateUtils.isSameDay(event.getDateApplied(), when));
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        });
    }

    /**
     * List the events AFTER the specified date. The list is NOT ordered.  It does not include events that fall ON the date
     * @param when
     * @param eventType     the type of event or null for all
     * @return
     */
    public List<PremiseEvent> listEventsAfter(final Date when, final Class<?> eventType) {
        return FilterTools.getMatching(events, new Filter<PremiseEvent>() {
            public boolean accept(PremiseEvent event) {
                Date dateApplied = event.getDateApplied();
                if (dateApplied != null) {
                    if ((eventType == null) || (eventType.isAssignableFrom(event.getClass()))) {
                        return event.getDateApplied().after(when) || (!DateUtils.isSameDay(event.getDateApplied(), when));
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        });
    }

    /** Get the most recent event on or before the date supplied */
    @Transient
    public PremiseEvent getLastEvent(final Date when) {
        List<PremiseEvent> events = listEventsOnOrBefore(when, null);
        if (events != null && events.size() >0) {
            Collections.sort(events, new Comparator<PremiseEvent>() {
                public int compare(PremiseEvent o1, PremiseEvent o2) {
                    return o1.getDateApplied().compareTo(o2.getDateApplied()) * -1;
                }
            });
            return events.get(0);
        } else {
            return null;
        }
    }

    /**
     * The advertisement campaigns recorded for this premise
     * @return
     */
    @OneToMany(mappedBy = "premise", cascade = CascadeType.ALL)
    public Set<PropertyAdvertisementCampaign> getAdvertisementCampaigns() {
        return advertisementCampaigns;
    }

    public void setAdvertisementCampaigns(Set<PropertyAdvertisementCampaign> advertisementCampaigns) {
        this.advertisementCampaigns = advertisementCampaigns;
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    public void print(PrintStream out) {
        out.print(getIdentityName()+": ");
        Address address = getAddress();
        if (address != null) {
            address.print(out);
        } else {
            out.println();
        }
        out.println("AttributeSets: "+getAttributes().size());
        for (PremiseAttributeSet attributeSet : getAttributes()) {
            attributeSet.print(out);
        }
    }

    public String printString() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        print(new PrintStream(out));
        return new String(out.toByteArray());
    }

    /**
     * Get the suburb from the address
     * @return
     */
    @Transient
    public SuburbHandle getSuburb() {
        return getAddress().getSuburb();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Provides the AdvertisedEvent (any applicable) for the premise that occurred on or before the specified date
     *
     * @param date
     * @return the Advertised event or null if there is no record
     */
    public Advertised lastAdvertised(Date date) {
        List<PremiseEvent> eventList = listEventsOnOrBefore(date, Advertised.class);
        Collections.sort(eventList, new PremiseEventDateComparator());
        if (eventList.size() > 0) {
            return (Advertised) eventList.get(eventList.size()-1);
        } else {
            return null;
        }
    }


    /**
     * Provides the AdvertisedEvent (any applicable) for the premise that occurred on or before the specified date
     *
     * @param date
     * @return the Advertised event or null if there is no record
     */
    public Advertised nextAdvertised(Date date) {
        List<PremiseEvent> eventList = listEventsAfter(date, Advertised.class);
        Collections.sort(eventList, new PremiseEventDateComparator());
        if (eventList.size() > 0) {
            return (Advertised) eventList.get(0);
        } else {
            return null;
        }
    }

    /**
     * Associates a property advertisement with this premise
     *
     * Activities:
     *   1. decides whether to create a new PropertyAdvertisementCampaign or join an existing one, based on the
     *     date and attributes of the advertisement
     *   2.creates a new event for the premise if appropriate and updates dates of existing events if necessary
     *
     * Case 1: Asking price change
     * eg. current status:
     *       Mar08 Advertised 400k
     *  new advertisement is Apr 380k
     *
     * new events:
     *       Mar08 Advertised 400k
     *      Apr09 AskingPriceReduced 380k
     *
     * Case 2 Existing event modified
     *
     * * eg. current status:
     *       Mar08 Advertised 400k
     * new advertisement is Feb 300k
     * new events:
     *       Feb08 Advertised 400k (existing event modified)
     *
     * The outcome will be:
     *    - no effect (advertisement added to an existing campaign with no consequences)
     *    - Advertised (new campaign)
     *    - AskingPriceChanged (existing campaign)
     *
     * @param propertyAdvertisement
     * @return a PremiseEvent if a new event was created
     */
    public PremiseEvent associateAdvertisement(PropertyAdvertisement propertyAdvertisement) {

        PremiseEvent premiseEvent = null;

        // associate attributes from the advertisement with the premise.
        associateWithAttributes(propertyAdvertisement.getAttributes());
        propertyAdvertisement.associateWithPremise(this);

        PropertyAdvertisementCampaign campaign;

        // create or join an advertising campaign - lookup the preceding and next events
        // and check if they match the characteristics of this advertisement
        Date when = propertyAdvertisement.getDateListed();

        Advertised lastAdvertised = lastAdvertised(when);
        if (lastAdvertised != null) {
            if (lastAdvertised.within2MonthsBefore(when)) {
                if (lastAdvertised.isOfType(propertyAdvertisement.getType())) {
                    lastAdvertised.getCampaign();
                }
            }
        }
        Advertised nextAdvertised = nextAdvertised(when);
        if (nextAdvertised != null) {
            if (nextAdvertised.within2MonthsAfter(when)) {
                if (nextAdvertised.isOfType(propertyAdvertisement.getType())) {
                    nextAdvertised.getCampaign();
                }
            }
        }

        // extract the campaign from the preceding or next event, if defined
        PropertyAdvertisementCampaign existingCampaign = (lastAdvertised != null ?  lastAdvertised.getCampaign() : (nextAdvertised != null ? nextAdvertised.getCampaign() : null));

        if (existingCampaign != null) {
            // a campaign exists so join it
            campaign = existingCampaign;

            // check if this is a change in the asking price for the campaign
            if (lastAdvertised != null) {
                PropertyAdvertisement lastAdvertisement = campaign.getLastPropertyAdvertisementWithPrice(propertyAdvertisement.getDateListed());
                if (lastAdvertisement != null) {
                    Integer compare = propertyAdvertisement.compareAskingPrice(lastAdvertisement.getPrice());
                    if (compare != null) {
                        if (compare == 0) {
                            // same price
                        } else {
                            if (compare < 0) {
                                // new advertisement is a reduction in price
                                premiseEvent = addEvent(new AskingPriceChanged(this, propertyAdvertisement.getDateListed(), lastAdvertisement, propertyAdvertisement, AskingPriceChangeType.Reduced));
                            } else {
                                // new advertisement is an increase in price
                                premiseEvent = addEvent(new AskingPriceChanged(this, propertyAdvertisement.getDateListed(), lastAdvertisement, propertyAdvertisement, AskingPriceChangeType.Increased));
                            }
                        }
                    }
                }
            } else {
                // The existing campaign was started after this advertisement, so the
                // campaign start event needs to be revised back
                nextAdvertised.setDateApplied(propertyAdvertisement.getDateListed());

                PropertyAdvertisement nextAdvertisement = campaign.getNextPropertyAdvertisementWithPrice(propertyAdvertisement.getDateListed());
                if (nextAdvertisement != null) {
                    Integer compare = propertyAdvertisement.compareAskingPrice(nextAdvertisement.getPrice());
                    if (compare != null) {
                        if (compare == 0) {
                            // same price
                        } else {
                             if (compare < 0) {
                                // new advertisement is higher price than the existing one after it
                                premiseEvent = addEvent(new AskingPriceChanged(this, nextAdvertisement.getDateListed(), propertyAdvertisement, nextAdvertisement, AskingPriceChangeType.Reduced));
                            } else {
                                // new advertisement is lower price than the existing one after it
                                premiseEvent = addEvent(new AskingPriceChanged(this, nextAdvertisement.getDateListed(), propertyAdvertisement, nextAdvertisement, AskingPriceChangeType.Increased));
                            }
                        }
                    }
                }
            }

            // finally add the new advertisement to the campaign
            campaign.addPropertyAdvertisement(propertyAdvertisement);
        } else {
            // start a new campaign and an initial event for the advertisement
            campaign = new PropertyAdvertisementCampaign(this, propertyAdvertisement);

            addCampaign(campaign);
            premiseEvent = addEvent(new Advertised(this, propertyAdvertisement.getDateListed(), campaign));
        }

        return premiseEvent;
    }

    private void addCampaign(PropertyAdvertisementCampaign campaign) {
        advertisementCampaigns.add(campaign);
    }

    /**
     * The tags that have been assigned to this Premise
     **/
    @OneToMany(mappedBy = "premise", cascade = CascadeType.ALL)
    public Set<PremiseTagMap> getTagMaps() {
        return tagMaps;
    }

    public void setTagMaps(Set<PremiseTagMap> tagMaps) {
        this.tagMaps = tagMaps;
    }

    @Transient
    public Set<Tag> getTags() {
        return TagTools.extractTags(tagMaps);
    }

    public void addTag(Tag tag) {
        if (!TagTools.contains(tagMaps, tag)) {
            tagMaps.add(new PremiseTagMap(this, tag));
        }
    }

    public boolean hasTag(Tag tag) {
        for (PremiseTagMap tagMap : tagMaps) {
            if (tagMap.getTag().equals(tag)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Merge the tags from the other premise into this premise
     *
     * @param otherTags
     */
    private void mergeTags(Set<PremiseTagMap> otherTags) {
         if (otherTags != null) {
            for (PremiseTagMap PremiseTagMap : otherTags) {
                if (!hasTag(PremiseTagMap.getTag())) {
                    // merge
                    addTag(PremiseTagMap.getTag());
                }
            }
        }
    }

    /**
     * If true, indicates that this premise has sub-premises (children).  Eg. it is an apartment complex
     * @return
     */
    @Transient
    public boolean isComplex() {
        return getChildren().size() > 0;
    }

    /**
     * If true, indicates that this premise is a child of a parent premise
     * This has nothing to do with the PropertyType
     * @return
     */
    @Transient
    public boolean isUnit() {
        return getParent() != null;
    }

     /**
     * The tags that have been assigned to this Premise
     **/
    @OneToMany(mappedBy = "premise", cascade = CascadeType.ALL)
    public Set<PremiseAssetMap> getPremiseAssetMaps() {
        return assetMaps;
    }

    public void setPremiseAssetMaps(Set<PremiseAssetMap> assetMaps) {
        this.assetMaps = assetMaps;
    }

    @Transient
    public Set<PremiseAsset> getAssets() {
        return PremiseAssetTools.extractPremiseAssets(assetMaps);
    }

    /**
     * Include the specified asset as Installed for the premise in the quantity given and date if available 
     * @param asset
     * @param dateInstalled
     * @param quantity
     */
    public void addAsset(PremiseAsset asset, Date dateInstalled, Integer quantity) {
        if (!PremiseAssetTools.contains(assetMaps, asset)) {
            assetMaps.add(new PremiseAssetMap(this, asset, PremiseAssetMapType.Installed, dateInstalled, quantity));
        }
    }

    public boolean hasAsset(PremiseAsset asset) {
        for (PremiseAssetMap assetMap : assetMaps) {
            if (assetMap.getAsset().equals(asset)) {
                return true;
            }
        }
        return false;
    }

}
