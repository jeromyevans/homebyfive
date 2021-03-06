package com.blueskyminds.homebyfive.business.region.graph;

import com.blueskyminds.homebyfive.business.region.graph.Region;
import com.blueskyminds.homebyfive.business.region.RegionTypes;
import com.blueskyminds.homebyfive.business.region.PathHelper;
import com.blueskyminds.homebyfive.business.region.index.StreetBean;
import com.blueskyminds.homebyfive.business.address.StreetType;
import com.blueskyminds.homebyfive.business.address.StreetSection;
import com.blueskyminds.homebyfive.business.tools.KeyGenerator;
import com.blueskyminds.homebyfive.business.tag.Tag;

import javax.persistence.*;
import java.util.List;
import java.util.LinkedList;
import java.io.PrintStream;

import org.apache.commons.lang.StringUtils;
import org.jboss.envers.Versioned;

/**
 * Date Started: 4/10/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
@Entity
@DiscriminatorValue("T")
@Versioned
public class Street extends Region {

    private Suburb suburb;

    private StreetType streetType;
    private StreetSection section;

    public Street(String name) {
        super(name, RegionTypes.Street);
        populateAttributes();
    }

    /**
     * Create a new instance of a street without the standard set of properties
     */
    public Street(String name, StreetType streetType, StreetSection section) {
        super(name, RegionTypes.Street);
        this.streetType = streetType;
        this.section = section;
        populateAttributes();
    }

    /**
     * Create a new instance of a street without the standard set of properties
     */
    public Street(Suburb suburb, String name, StreetType streetType, StreetSection section) {
        super(name, RegionTypes.Street);
        this.streetType = streetType;
        this.section = section;
        setSuburb(suburb);
        populateAttributes();
        if (suburb != null) {
            suburb.addStreet(this);
        }
    }

    /**
     * Create a new instance of a street without a street section (street sections are rare)
     */
    public Street(String name, StreetType streetType) {
        super(name, RegionTypes.Street);
        this.streetType = streetType;
        populateAttributes();
    }

     /**
     * Create a new instance of a street without a street section (street sections are rare)
     */
    public Street(Suburb suburb, String name, StreetType streetType) {
        super(name, RegionTypes.Street);
        this.streetType = streetType;
        setSuburb(suburb);
        populateAttributes();
        if (suburb != null) {
            suburb.addStreet(this);
        }
    }
   
    public Street(Suburb suburb) {
        super("", RegionTypes.Street);
        setSuburb(suburb);
        populateAttributes();
        if (suburb != null) {
            suburb.addStreet(this);
        }
    }
    
    public Street() {
        this.type = RegionTypes.Street;
    }

    @Override
    @Transient
    public Region getParent() {
        return suburb;
    }

    public void setParent(Region region) {
        this.suburb = (Suburb) region;
    }

    /**
     * Get the type of this street.
     */
    @Enumerated
    @Column(name = "StreetType")
    public StreetType getStreetType() {
        return streetType;
    }

    public void setStreetType(StreetType streetType) {
        this.streetType = streetType;
    }

    /**
     * Get the section of this street. [optional]
     */
    @Enumerated
    @Column(name = "Section")
    public StreetSection getSection() {
        return section;
    }

    protected void setSection(StreetSection section) {
        this.section = section;
    }

    public String toString() {
        return (isIdSet() ? "Street[" + getId() + "]" : "") + getName() + " " + getStreetType() + " " + (((getSection() != null) && (getSection() != StreetSection.NA)) ? getSection() : "");
    }


    public void print(PrintStream out) {
        out.println(getIdentityName() + " (" + getName() + " " + getStreetType() + " " + (getSection() != null ? getSection() : "") + ")");
    }


    /**
     * Gets a full displayable name for this street (eg.  North Street)
     */
    @Transient
    public String getFullName() {
        List<String> parts = new LinkedList<String>();

        if (StreetType.The.equals(streetType)) {
            parts.add(streetType.name());
        }
        parts.add(getName());
        if (streetType != null) {
            if (!StreetType.The.equals(streetType)) {
                parts.add(streetType.name());
            }
        }
        if ((section != null) && (StreetSection.NA != section)) {
            parts.add(section.name());
        }
        return StringUtils.join(parts.iterator(), " ");
    }

    /**
     * Gets the parent Suburb
     *
     * @return
     */
    @ManyToOne
    @JoinColumn(name="StreetSuburbId")
    public Suburb getSuburb() {
         return suburb;
    }

    /**
     * Set the parent suburb for this street.
     * @param suburb
     */
    public void setSuburb(Suburb suburb) {
        this.suburb = suburb;
        if (suburb != null) {
            this.parentPath = suburb.getPath();
        } else {
            this.parentPath = null;
        }
    }

    public void populateAttributes() {
        this.key = KeyGenerator.generateId(getFullName());
        Region suburb = getSuburb();
        if (suburb != null) {
            this.parentPath = suburb.getPath();
            this.path = PathHelper.joinPath(parentPath, key);
        }
    }

    /** Add a tag (same as addTag, but returns this */
    public Street withTag(Tag a) {
        super.withTag(a);
        return this;
    }

     /** Add tags and return this */
    public Street withTags(Tag... tags) {
        super.withTags(tags);
        return this;
    }

    /**
     * Create or update the denormalized index entity
     */
    @PrePersist
    protected void prePersist() {
        super.prePersist();
        if (regionIndex == null) {
             regionIndex = new StreetBean(this);
        } else {
            // update the attribute of the index
            regionIndex.populateDenormalizedAttributes();
        }
    }

}
