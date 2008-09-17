package com.blueskyminds.landmine.core.property.attribute;

import com.blueskyminds.framework.measurement.Area;

import javax.persistence.*;

/**
 * An attribute for a property identifying it's land area
 *
 * Date Started: 10/06/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class LandArea extends PropertyAttribute<Area> {

    private Area landArea;

    // ------------------------------------------------------------------------------------------------------

    public LandArea(Area landArea) {
        //super(attributeSet, PropertyAttributeTypes.LandArea);
        this.landArea = landArea;
    }

    /** Default constructor for ORM */
    protected LandArea() {
    }

    // ------------------------------------------------------------------------------------------------------
  
    public Area getLandArea() {
        return landArea;
    }

    protected void setLandArea(Area landArea) {
        this.landArea = landArea;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Returns the land area value */
    public Area value() {
        return getLandArea();
    }

    // ------------------------------------------------------------------------------------------------------
}
