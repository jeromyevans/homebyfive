package com.blueskyminds.business.contact;

import javax.persistence.*;


/**
 * An email address is a type of PointOfContact
 *
 * Date Started: 29/04/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
@DiscriminatorValue("Email")
public class EmailAddress extends PointOfContact {

    /** The email address value */
    private String emailAddress;

    // ------------------------------------------------------------------------------------------------------

    /** Create a new email adddress with the specified initial role */
    public EmailAddress(String emailAddress, POCRole initialRole) {
        super(initialRole);
        this.emailAddress = emailAddress;
    }

    /** Default constructor for ORM */
    protected EmailAddress() {
    }

    // ------------------------------------------------------------------------------------------------------

    @Basic
    @Column(name="EmailAddress")
    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    // ------------------------------------------------------------------------------------------------------

    @Transient
    @Override
    public String getIdentityName() {
        return super.getIdentityName()+" ("+getEmailAddress()+")";
    }

    // ------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return emailAddress;
    }

    // ------------------------------------------------------------------------------------------------------

    /** The contextless value for this PointofContact */
    @Transient
    public String getValue() {
        return emailAddress;
    }
}
