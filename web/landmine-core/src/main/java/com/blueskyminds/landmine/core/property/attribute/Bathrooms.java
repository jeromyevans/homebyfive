package com.blueskyminds.landmine.core.property.attribute;

import javax.persistence.*;

/**
 * An attribute for a property specifying the number of bathrooms
 *
 * Date Started: 9/06/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class Bathrooms extends PropertyAttribute<Integer> {

    private Integer noOfBathrooms;

    // ------------------------------------------------------------------------------------------------------

    public Bathrooms(int noOfBathrooms) {
//        super(attributeSet, PropertyAttributeTypes.Bathrooms);
        this.noOfBathrooms = noOfBathrooms;
    }

    /** Default constructor for ORM */
    protected Bathrooms() {
    }

    // ------------------------------------------------------------------------------------------------------

    public Integer getNoOfBathrooms() {
        return noOfBathrooms;
    }

    protected void setNoOfBathrooms(Integer noOfBathrooms) {
        this.noOfBathrooms = noOfBathrooms;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Returns the number of bathrooms value */
    public Integer value() {
        return getNoOfBathrooms();
    }

    // ------------------------------------------------------------------------------------------------------
}
