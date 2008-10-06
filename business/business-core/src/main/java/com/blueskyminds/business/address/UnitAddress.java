package com.blueskyminds.business.address;

import org.apache.commons.lang.StringUtils;

import javax.persistence.*;

import com.blueskyminds.business.region.graph.Suburb;
import com.blueskyminds.business.region.graph.PostalCode;
import com.blueskyminds.business.region.graph.Street;

/**
 * Extension of a StreetAddress to include a unit number
 *
 * Date Started: 25/06/2006
 *
 * History:
 *
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
@Entity
@DiscriminatorValue("Unit")
public class UnitAddress extends StreetAddress {

    private String unitNumber;

    // ------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a new instance of a unit address for a property with a unit number and street number
     *
     * @param unitNumber
     * @param streetNumber
     * @param street
     */
    public UnitAddress(String unitNumber, String streetNumber, Street street, Suburb suburb, PostalCode postCode) {
        super(streetNumber, street, suburb, postCode);

        this.unitNumber = unitNumber;
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /** Default constructor for ORM */
    protected UnitAddress() {

    }

    @Transient
    public String getNumber() {
        if (unitNumber != null) {
            if (getStreetNumber() != null) {
                return unitNumber + "/"+super.getNumber();
            } else {
                return "Unit "+unitNumber;
            }
        } else {
            return super.getNumber();
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Defines a unit number for this street address.  Note that this does not REPLACE any existing values.
     */
    @Basic
    @Column(name="UnitNumber")
    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    // ------------------------------------------------------------------------------------------------------

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        if (isIdSet()) {
            buffer.append(getIdentityName()+" ");
        }
        buffer.append(StringUtils.isNotBlank(unitNumber) ? "["+unitNumber + "]/" : "");

        appendStreetNumber(buffer);
        appendStreet(buffer);
        appendSuburb(buffer);

        return buffer.toString();
    }

    public String format(boolean includeState, boolean includePostCode, boolean includeCountry) {
        StringBuilder buffer = new StringBuilder();

        buffer.append(StringUtils.isNotBlank(unitNumber) ? unitNumber + "/" : "");
        buffer.append(StringUtils.isNotBlank(getStreetNumber()) ? getStreetNumber()+ " " : "");

        if (getStreet() != null) {
            String streetName = getStreet().getFullName();
            buffer.append(StringUtils.isNotBlank(streetName) ? streetName+ " " : "");
        }

        formatSuburb(buffer, includeState, includePostCode, includeCountry);

        return buffer.toString();
    }
    
    /**
     * Create a copy of this street address.  The copy does not inherit the id or temporal attributes
     * @return
     */
    public UnitAddress duplicate() {
        return new UnitAddress(unitNumber, getStreetNumber(), getStreet(), getSuburb(), getPostCode());
    }
    
    // ------------------------------------------------------------------------------------------------------

    /**
     * Extract the street address component  of this unit address as a new instance
     * @return
     */
    public StreetAddress extractStreetAddress() {
        return super.duplicate();
    }

}
