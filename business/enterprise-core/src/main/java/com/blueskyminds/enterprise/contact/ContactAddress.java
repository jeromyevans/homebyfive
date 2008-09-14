package com.blueskyminds.enterprise.contact;


import javax.persistence.*;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import com.blueskyminds.enterprise.address.MultilineAddress;
import com.blueskyminds.enterprise.address.Address;
import com.blueskyminds.enterprise.region.suburb.SuburbHandle;
import com.blueskyminds.enterprise.region.postcode.PostCodeHandle;

/**
 * A simple plain text address for a contact
 *
 * Date Started: 8/06/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
@DiscriminatorValue("Address")
public class ContactAddress extends PointOfContact {

    private Address address;

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise a simple address with all the parameters specified
     * Use blank or null for a non-applicable parameter
     *
     * @param address1
     * @param address2
     * @param address3
     * @param address4
     * @param suburb
     * @param postcode
     * @param role
     */
    public ContactAddress(String address1, String address2, String address3, String address4, SuburbHandle suburb, PostCodeHandle postcode, POCRole role) {
        super(role);
        this.address = new MultilineAddress(address1, address2, address3, address4, suburb, postcode);
    }

    /**
     * Initialise a contact address referencing the specified address
     */
    public ContactAddress(Address address, POCRole initialRole) {
        super(initialRole);
        this.address = address;
    }

    /** Default constructor for ORM */
    protected ContactAddress() {

    }

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,  CascadeType.REFRESH})  // no remove!
    @JoinColumn(name = "AddressId")
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    // ------------------------------------------------------------------------------------------------------

    /** If the specified value is non-blank, adds it the list */
    private void addPartIfNonBlank(String value, List<String> parts) {
        if (StringUtils.isNotBlank(value)) {
            parts.add(value);
        }
    }



    // ------------------------------------------------------------------------------------------------------

    /**
     * Concatenates the multi-part address into a single string
     * @return string representation of the address
     */
    @Override
    public String toString() {
//        List<String> parts = new LinkedList<String>();
//
//        addPartIfNonBlank(address.address1, parts);
//        addPartIfNonBlank(address2, parts);
//        addPartIfNonBlank(address3, parts);
//        addPartIfNonBlank(address4, parts);
//        addPartIfNonBlank(suburb, parts);
//        addPartIfNonBlank(state, parts);
//        addPartIfNonBlank(postcode, parts);
//        addPartIfNonBlank(country, parts);

        // joint all the parts separated by whitespace
//        return StringUtils.join(parts.iterator(), " ");
        return address.toString();
    }

    // ------------------------------------------------------------------------------------------------------
    
    /** The contextless value for this PointOfContact */
    @Transient
    public String getValue() {
        return address.toString();
    }
}
