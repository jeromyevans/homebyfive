package com.blueskyminds.enterprise.address;

import com.blueskyminds.homebyfive.framework.framework.AbstractDomainObject;
import com.blueskyminds.enterprise.region.suburb.Suburb;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.CascadeType;

/**
 * Mapping class that maps Streets to One or more suburbs
 *
 * Suburb<->Street is a many-to-many relationship
 *
 * Cascading is from Suburb->Street for all types except REMOVE
 *
 * Date Started: 14/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd<br/>
 */
@Entity
public class SuburbStreetMap extends AbstractDomainObject {

    private Suburb suburb;
    private Street street;

    // ------------------------------------------------------------------------------------------------------

    public SuburbStreetMap(Suburb suburb, Street street) {
        this.suburb = suburb;
        this.street = street;
    }

    /** Default constructor for ORM */
    protected SuburbStreetMap() {
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the SuburbStreetMap with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    @ManyToOne
    @JoinColumn(name="SuburbId")
    public Suburb getSuburb() {
        return suburb;
    }

    public void setSuburb(Suburb suburb) {
        this.suburb = suburb;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="StreetId")
    public Street getStreet() {
        return street;
    }

    public void setStreet(Street street) {
        this.street = street;
    }
}
