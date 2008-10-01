package com.blueskyminds.enterprise.address;

import com.blueskyminds.homebyfive.framework.framework.AbstractDomainObject;
import com.blueskyminds.homebyfive.framework.framework.tools.Named;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

/**
 * A physical Street, road, etc.
 *
 * A street always has a name and a type.
 * A street may have sections.
 * A street will be in one or more suburbs 
 *
 * To associate this street with a suburb, use suburb.addStreet()
 *
 * User: Jeromy
 * Date: 16/04/2006
 * Time: 13:07:14
 *
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd<br/>
 */
@Entity
public class Street extends AbstractDomainObject implements Named {

    private String name;
    private StreetType type;
    private StreetSection section;
    //private Collection<SuburbStreetMap> suburbStreetMaps;

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a new instance of a street without the standard set of properties
     */
    public Street(String name, StreetType type, StreetSection section) {
        init();
        this.name = name;
        this.type = type;
        this.section = section;
    }

    /**
     * Create a new instance of a street without a street section (street sections are rare)
     */
    public Street(String name, StreetType type) {
        init();
        this.name = name;
        this.type = type;
    }

    /**
     * Default constructor for ORM
     */
    protected Street() {
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /** Initialise the street with default attributes */
    private void init() {
    //    suburbStreetMaps = new HashSet<SuburbStreetMap>();
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the name for this street.
     */
    @Basic
    @Column(name="Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the type of this street.
     */
    @Enumerated
    @Column(name="Type")
    public StreetType getType() {
        return type;
    }

    public void setType(StreetType type) {
        this.type = type;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the section of this street. [optional]
     */
    @Enumerated
    @Column(name="Section")
    public StreetSection getSection() {
        return section;
    }

    protected void setSection(StreetSection section) {
        this.section = section;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the mappings of this street to suburbs
     * @return SuburbStreetMaps */
//    @OneToMany(mappedBy = "street")
//    protected Collection<SuburbStreetMap> getSuburbStreetMaps() {
//        return suburbStreetMaps;
//    }
//
//    protected void setSuburbStreetMaps(Collection<SuburbStreetMap> suburbStreetMaps) {
//        this.suburbStreetMaps = suburbStreetMaps;
//    }

    /**
     * Get the suburbs this street has been mapped to
     * @return
     */
    @Transient
//    public Collection<Suburb> getSuburbs() {
//        Collection<Suburb> suburbs = new HashSet<Suburb>();
//        for (SuburbStreetMap map : suburbStreetMaps) {
//            suburbs.add(map.getSuburb());
//        }
//        return suburbs;
//    }

    // ------------------------------------------------------------------------------------------------------

    public void print(PrintStream out) {
        out.println(getIdentityName()+" ("+getName()+" "+getType()+" "+(getSection() != null ? getSection() : "")+")");
    }

    // ------------------------------------------------------------------------------------------------------

    public String toString() {
        return (isIdSet() ? "Street["+getId()+"]" : "")+getName()+" "+getType()+" "+(((getSection() != null) && (getSection() != StreetSection.NA)) ? getSection() : "");
    }

    /** Gets a full displayable name for this street (eg.  North Street) */
    @Transient
    public String getFullName() {
        List<String> parts = new LinkedList<String>();
        
        if (StreetType.The.equals(type)) {
            parts.add(type.name());
        }
        parts.add(getName());
        if (type != null) {
            if (!StreetType.The.equals(type)) {
                parts.add(type.name());
            }
        }
        if ((section != null) && (StreetSection.NA != section)) {
            parts.add(section.name());
        }
        return StringUtils.join(parts.iterator(), " ");
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
}
