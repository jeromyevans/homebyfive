package com.blueskyminds.homebyfive.business.address;

import com.blueskyminds.homebyfive.framework.core.AbstractDomainObject;
import com.blueskyminds.homebyfive.business.region.graph.PostalCode;
import com.blueskyminds.homebyfive.business.region.graph.*;

import javax.persistence.*;

import org.apache.commons.lang.StringUtils;
import org.jboss.envers.Versioned;

/**
 * An Address identifies a physical location
 *
 * User: Jeromy
 * Date: 16/04/2006
 * Time: 13:00:47
 *
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
@Entity
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="AddressClass", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("Abstract")
@Versioned
public abstract class Address extends AbstractDomainObject {

    /** The suburb the address is in */
    private Suburb suburb;

    /** The postcode for the address - usually implied by the suburb, but not always */
    private PostalCode postCode;

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a new address of the specified type in the suburb identified
     *
     * @param suburb        Suburb of this address (inferring the state and country)
     * @param postCode      PostCode for the address (usually the suburb's postcode)
     */
    public Address(Suburb suburb, PostalCode postCode) {
        this.suburb = suburb;
        this.postCode = postCode;
    }


    /**
     * Create a new address of the specified type in the suburb identified
     *
     * @param suburb        Suburb of this address (inferring the state and country)
     */
    public Address(Suburb suburb) {
        this.suburb = suburb;
    }

    /** Default constructor for ORM */
    protected Address() {
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /**
     * Get which suburb this address belongs to.
     */
    @ManyToOne(cascade = CascadeType.ALL)  // cascade is required if Street is added to the Suburb
    @JoinColumn(name="SuburbId")
    public Suburb getSuburb() {
        return suburb;
    }

    public void setSuburb(Suburb suburb) {
        this.suburb = suburb;
    }

    public boolean hasSuburb() {
        return suburb != null;
    }
    
    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the postcode used by this address.  This is usually the suburb's postcode
     */
    @ManyToOne
    @JoinColumn(name="PostCodeId")
    public PostalCode getPostCode() {
        return postCode;
    }

    public void setPostCode(PostalCode postCode) {
        this.postCode = postCode;
    }

    public boolean hasPostCode() {
        return postCode != null;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Gets a reference to the state from the suburb or postcode (suburb takes priority)
     * This is not a fast away to access this information
     *
     * @return
     */
    @Transient
    public Region getState() {
        if (hasSuburb()) {
            return suburb.getState();
        } else {
            if (hasPostCode()) {
                return postCode.getState();
            }
        }
        return null;
    }

    /**
     * Gets a reference to the country via the state
     * This is not a fast away to access this information
     *
     * @return
     */
    @Transient
    public Region getCountry() {
        Region state = getState();
        if (state != null) {
            return state.getCountry();
        } else {
            return null;
        }
    }

    /**
     * Get the number part of this address
     *
     * @return a string, eg. 22 or 1/22 or null if not applicable
     */
    @Transient
    public abstract String getNumber();

    public boolean hasNumber() {
        return StringUtils.isNotBlank(getNumber());
    }

    /**
     * Get the street part of this address
     *
     * @return street or null if not applicable
     */
    @Transient
    public abstract Street getStreet();

    /**
     * Format the address into a human readable string
     *
     * @param includeState
     * @param includePostCode
     * @param includeCountry
     * @return
     */
    public abstract String format(boolean includeState, boolean includePostCode, boolean includeCountry);

    /** Format the suburb/postcode/country part of the address that is common to all types */
    protected void formatSuburb(StringBuilder buffer, boolean includeState, boolean includePostCode, boolean includeCountry) {

        if (getSuburb() != null) {
            String suburbName = getSuburb().getName();
            buffer.append(StringUtils.isNotBlank(suburbName) ? suburbName+ " " : "");

            if (includeState) {
                State stateHandle = getSuburb().getState();
                if (stateHandle != null) {
                    buffer.append(stateHandle.getName()+" ");
                }
            }

            if (includePostCode) {
                PostalCode postCode = getSuburb().getPostCode();
                if (postCode != null) {
                    buffer.append(postCode.getName()+" ");
                }
            }

            if (includeCountry) {
                Country countryHandle = getCountry();
                if (countryHandle != null) {
                    buffer.append(countryHandle.getName()+" ");
                }
            }
        }
    }
}
