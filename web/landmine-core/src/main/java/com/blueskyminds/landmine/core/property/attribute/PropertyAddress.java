package com.blueskyminds.landmine.core.property.attribute;

import com.blueskyminds.enterprise.address.Address;
import com.blueskyminds.landmine.core.property.PremiseAttributeSet;

import javax.persistence.*;

/**
 * An attribute containing an address for a property
 *
 * Date Started: 9/06/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Deprecated // address is associated with the top-level class
//@Entity
//@PrimaryKeyJoinColumn(name="PropertyAttributeId")
public class PropertyAddress extends PropertyAttribute<Address> {

    private Address address;

    // ------------------------------------------------------------------------------------------------------

    public PropertyAddress(PremiseAttributeSet attributeSet, Address address) {
//        super(attributeSet, PropertyAttributeTypes.PropertyTypeWrapper);
        this.address = address;
    }

    /** Default constructor for ORM */
    protected PropertyAddress() {
    }

    // ------------------------------------------------------------------------------------------------------

    @ManyToOne
    @JoinColumn(name="AddressId")
    public Address getAddress() {
        return address;
    }

    protected void setAddress(Address address) {
        this.address = address;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Returns the type value */
    public Address value() {
        return getAddress();
    }

    // ------------------------------------------------------------------------------------------------------
}
