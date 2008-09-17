package com.blueskyminds.landmine.core.property.attribute;

import javax.persistence.*;

/**
 * An attribute describing a property
 *
 * Date Started: 9/04/2008
 *
 * History:
 *
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class Carspaces extends PropertyAttribute<Integer> {

    private Integer noOfCarspaces;

    // ------------------------------------------------------------------------------------------------------

    public Carspaces(int noOfCarspaces) {
//        super(attributeSet, PropertyAttributeTypes.Bedrooms);
        this.noOfCarspaces = noOfCarspaces;
    }

    /** Default constructor for ORM */
    protected Carspaces() {
    }

    // ------------------------------------------------------------------------------------------------------

    public Integer getNoOfCarspaces() {
        return noOfCarspaces;
    }

    protected void setNoOfCarspaces(Integer noOfCarspaces) {
        this.noOfCarspaces = noOfCarspaces;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Returns the number of carspaces value */
    public Integer value() {
        return getNoOfCarspaces();
    }

    // ------------------------------------------------------------------------------------------------------
}