package com.blueskyminds.landmine.core.property.attribute;

/**
 * The superclass for an attribute that describes a property
 * Contains the common properties of the attribute
 *
 * Property attributes may be contained on a AttributeSet for a property
 *
 * Date Started: 9/06/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public abstract class PropertyAttribute<T> {

    /** Default constructor for ORM */
    protected PropertyAttribute() {

    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /** Get the underlying instance value for this attribute */
    public abstract T value();

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    public String toString() {
        return value().toString();
    }
}
