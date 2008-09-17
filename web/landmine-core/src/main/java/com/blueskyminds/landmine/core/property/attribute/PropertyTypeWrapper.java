package com.blueskyminds.landmine.core.property.attribute;

import com.blueskyminds.landmine.core.property.PropertyTypes;

/**
 *
 * A property attribute  identifying the property type
 *
 * Date Started: 9/06/2006
 *
 * History:
 *
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class PropertyTypeWrapper extends PropertyAttribute<PropertyTypes> {

    private PropertyTypes type;

    // ------------------------------------------------------------------------------------------------------

    public PropertyTypeWrapper(PropertyTypes propertyType) {
//        super(attributeSet, PropertyAttributeTypes.PropertyTypeWrapper);
        this.type = propertyType;
    }

    /** Default constructor for ORM */
    protected PropertyTypeWrapper() {
    }

    // ------------------------------------------------------------------------------------------------------

    public PropertyTypes getType() {
        return type;
    }

    protected void setType(PropertyTypes type) {
        this.type = type;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Returns the type value */
    public PropertyTypes value() {
        return getType();
    }

    /**
     * Makes a gross determination about whether the specified property type enumeration would usually
     *  have a unit component in the address
     *
     * This method is not intended for a finite determination
     *
     * @param type
     * @return true if it probably would have a unit component in the address
     */
    public static boolean isTypeOfUnit(PropertyTypes type) {
        boolean isUnit = true;

        switch (type) {
            case Complex:
            case House:
            case Land:
            case Unknown:
                isUnit = false;
                break;
        }
        return isUnit;
    }

}
