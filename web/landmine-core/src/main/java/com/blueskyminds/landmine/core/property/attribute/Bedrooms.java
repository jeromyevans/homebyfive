package com.blueskyminds.landmine.core.property.attribute;

import javax.persistence.*;

/**
 * An attribute describing a property
 *
 * Date Started: 9/06/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class Bedrooms extends PropertyAttribute<Integer> {

    private Integer noOfBedrooms;

    // ------------------------------------------------------------------------------------------------------

    public Bedrooms(int noOfBedrooms) {
//        super(attributeSet, PropertyAttributeTypes.Bedrooms);
        this.noOfBedrooms = noOfBedrooms;
    }

    /** Default constructor for ORM */
    protected Bedrooms() {
    }

    // ------------------------------------------------------------------------------------------------------

    public Integer getNoOfBedrooms() {
        return noOfBedrooms;
    }

    protected void setNoOfBedrooms(Integer noOfBedrooms) {
        this.noOfBedrooms = noOfBedrooms;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Returns the number of bedrooms value */
    public Integer value() {
        return getNoOfBedrooms();
    }

    // ------------------------------------------------------------------------------------------------------
}
