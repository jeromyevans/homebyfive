package com.blueskyminds.homebyfive.business.address;

import org.apache.commons.lang.StringUtils;
import org.jboss.envers.Versioned;

import javax.persistence.*;

import com.blueskyminds.homebyfive.business.region.graph.PostalCode;
import com.blueskyminds.homebyfive.business.region.graph.Suburb;
import com.blueskyminds.homebyfive.business.region.graph.State;
import com.blueskyminds.homebyfive.business.region.graph.Street;

import java.io.PrintStream;

/**
 * The physical address of real property situated on a street
 *
 * User: Jeromy
 * Date: 16/04/2006
 * Time: 12:53:52
 * 
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
@Entity
@DiscriminatorValue("Street")
@Versioned
public class StreetAddress extends Address {

    private String streetNumber;
    private Street street;

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a new instance of a street address for a property with a street number
     *
     * @param streetNumber
     * @param street
     * @param suburb
     */
    public StreetAddress(String streetNumber, Street street, Suburb suburb, PostalCode postCode) {
        super(suburb, postCode);

        this.streetNumber = streetNumber;
        this.street = street;
    }

    /**
     * Create a new instance of a street address for a property with a street number
     *
     * @param streetNumber
     * @param street
     * @param suburb
     */
    public StreetAddress(String streetNumber, Street street, Suburb suburb) {
        super(suburb);

        this.streetNumber = streetNumber;
        this.street = street;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a new instance of a street address for a property with no street number
     *
     * @param street
     * @param suburb
     */
    public StreetAddress(Street street, Suburb suburb, PostalCode postCode) {
        super(suburb, postCode);

        this.street = street;
    }


    /** Default constructor for ORM */
    protected StreetAddress() {

    }

    /** Get the number component of the address.  This may be overridden by specialisations of StreetAddress */
    @Transient
    public String getNumber() {
        return streetNumber;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Defines a street number for this street address.  Note that this does not REPLACE any existing values.
     */
    @Basic
    @Column(name="StreetNumber")
    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Defines a the street for this street address.  Note that this does not REPLACE any existing values.
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="StreetId")
    public Street getStreet() {
        return street;
    }

    public void setStreet(Street street) {
        this.street = street;
    }

    // ------------------------------------------------------------------------------------------------------

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        if (isIdSet()) {
            buffer.append(getIdentityName()+" ");
        }

        appendStreetNumber(buffer);
        appendStreet(buffer);
        appendSuburb(buffer);

        return buffer.toString();
    }

    protected void appendStreetNumber(StringBuilder buffer) {
        buffer.append(StringUtils.isNotBlank(streetNumber) ? "["+streetNumber+"] " : "");
    }

    protected void appendStreet(StringBuilder buffer) {
        if (street != null) {
            buffer.append(StringUtils.isNotBlank(street.toString()) ? "["+street+"] " : "");
        }
    }

    protected void appendSuburb(StringBuilder buffer) {
        if (getSuburb() != null) {
            buffer.append(StringUtils.isNotBlank(getSuburb().toString()) ? getSuburb()+ " " : "");
            State stateHandle = getSuburb().getState();
            if (stateHandle != null) {
                buffer.append(stateHandle.toString()+" ");
            }
            PostalCode postCode = getSuburb().getPostCode();
            if (postCode != null) {
                buffer.append(postCode.toString()+" ");
            }
        }
    }

    public String format(boolean includeState, boolean includePostCode, boolean includeCountry) {
        StringBuilder buffer = new StringBuilder();

        buffer.append(StringUtils.isNotBlank(streetNumber) ? streetNumber + " " : "");

        if (street != null) {
            String streetName = street.getFullName();
            buffer.append(StringUtils.isNotBlank(streetName) ? streetName+ " " : "");
        }

        formatSuburb(buffer, includeState, includePostCode, includeCountry);

        return buffer.toString();
    }

    public void print(PrintStream out) {
        out.println(getIdentityName()+" ("+toString()+")");
    }
    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a copy of this street address.  The copy does not inherit the id or temporal attributes
     * @return
     */
    public StreetAddress duplicate() {
        return new StreetAddress(streetNumber, street, getSuburb(), getPostCode());
    }
}
