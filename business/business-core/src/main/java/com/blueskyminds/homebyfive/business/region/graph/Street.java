package com.blueskyminds.homebyfive.business.region.graph;

import com.blueskyminds.homebyfive.business.region.graph.Region;
import com.blueskyminds.homebyfive.business.region.RegionTypes;
import com.blueskyminds.homebyfive.business.region.PathHelper;
import com.blueskyminds.homebyfive.business.address.StreetType;
import com.blueskyminds.homebyfive.business.address.StreetSection;
import com.blueskyminds.homebyfive.business.tools.KeyGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.LinkedList;
import java.io.PrintStream;

import org.apache.commons.lang.StringUtils;

/**
 * Date Started: 4/10/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
@Entity
@DiscriminatorValue("T")
public class Street extends Region {

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
     * Create a new instance of a street without a street section (street sections are rare)
     */
    public Street(String name, StreetType streetType) {
        super(name, RegionTypes.Street);
        this.streetType = streetType;
        populateAttributes();
    }

   
    public Street(Suburb suburb) {
        super("", RegionTypes.Street);
        addParentRegion(suburb);
        populateAttributes();
    }
    
    protected Street() {
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
     * Deproxies the instance if necessary
     *
     * @return
     */
    @Transient
    public Suburb getSuburb() {
         Region parent = getParent(RegionTypes.Suburb);
         if (parent != null) {
            return (Suburb) parent.unproxy().getModel();
         } else {
             return null;
         }
    }

    public void populateAttributes() {
        this.key = KeyGenerator.generateId(name);
        Suburb suburb = getSuburb();
        if (suburb != null) {
            this.parentPath = suburb.getPath();
            this.path = PathHelper.joinPath(parentPath, key);
        }
    }
}
