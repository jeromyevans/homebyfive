package com.blueskyminds.enterprise.region.street;

import com.blueskyminds.enterprise.region.RegionHandle;
import com.blueskyminds.enterprise.region.RegionTypes;
import com.blueskyminds.enterprise.address.StreetType;
import com.blueskyminds.enterprise.address.StreetSection;

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
public class StreetHandle extends RegionHandle {

    private StreetType streetType;
    private StreetSection section;

    public StreetHandle(String name) {
        super(name, RegionTypes.Street);
    }

    /**
     * Create a new instance of a street without the standard set of properties
     */
    public StreetHandle(String name, StreetType streetType, StreetSection section) {
        super(name, RegionTypes.Street);
        this.streetType = streetType;
        this.section = section;
    }

    /**
     * Create a new instance of a street without a street section (street sections are rare)
     */
    public StreetHandle(String name, StreetType streetType) {
        super(name, RegionTypes.Street);
        this.streetType = streetType;
    }

    protected StreetHandle() {
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

}
