package com.blueskyminds.enterprise.contact;

import javax.persistence.*;

/**
 * A PhoneNumber is a type of Point of Contact
 *
 * Date Started: 29/04/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
@DiscriminatorValue("Phone")
public class PhoneNumber extends PointOfContact {

    /** The phone number value */
    private String phoneNumber;

    /** The type of phone number */
    private PhoneNumberTypes type;

    // ------------------------------------------------------------------------------------------------------

    /** Create a phone number with the specified initial role */
    public PhoneNumber(String phoneNumber, POCRole initialRole, PhoneNumberTypes type) {
        super(initialRole);
        this.phoneNumber = phoneNumber;
        this.type = type;
    }

    /** Default constructor for ORM */
    protected PhoneNumber() {

    }

    // ------------------------------------------------------------------------------------------------------

    @Enumerated
    @Column(name="Type")
    public PhoneNumberTypes getType() {
        return type;
    }

    public void setType(PhoneNumberTypes type) {
        this.type = type;
    }

    // ------------------------------------------------------------------------------------------------------

    @Basic
    @Column(name="PhoneNumber")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    // ------------------------------------------------------------------------------------------------------

    @Transient
    @Override
    public String getIdentityName() {
        return super.getIdentityName()+" ("+getType()+":"+getPhoneNumber()+")";
    }

    // ------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return phoneNumber;
    }
    // ------------------------------------------------------------------------------------------------------

    /** The contextless value for this PointOfContact */
    @Transient
    public String getValue() {
        return phoneNumber;
    }

}
