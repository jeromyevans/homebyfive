package com.blueskyminds.enterprise.address;

import com.blueskyminds.framework.AbstractDomainObject;
import com.blueskyminds.enterprise.region.suburb.SuburbHandle;
import com.blueskyminds.enterprise.region.postcode.PostCodeHandle;
import com.blueskyminds.enterprise.region.state.StateHandle;
import com.blueskyminds.enterprise.region.country.CountryHandle;

import javax.persistence.*;

import org.apache.commons.lang.StringUtils;

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
public abstract class Address extends AbstractDomainObject {

    /** The suburb the address is in */
    private SuburbHandle suburb;

    /** The postcode for the address - usually implied by the suburb, but not always */
    private PostCodeHandle postCode;

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a new address of the specified type in the suburb identified
     *
     * @param suburb        Suburb of this address (inferring the state and country)
     * @param postCode      PostCode for the address (usually the suburb's postcode)
     */
    public Address(SuburbHandle suburb, PostCodeHandle postCode) {
        this.suburb = suburb;
        this.postCode = postCode;
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
    public SuburbHandle getSuburb() {
        return suburb;
    }

    public void setSuburb(SuburbHandle suburb) {
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
    public PostCodeHandle getPostCode() {
        return postCode;
    }

    public void setPostCode(PostCodeHandle postCode) {
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
    public StateHandle getState() {
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
    public CountryHandle getCountry() {
        StateHandle state = getState();
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
                StateHandle stateHandle = getSuburb().getState();
                if (stateHandle != null) {
                    buffer.append(stateHandle.getName()+" ");
                }
            }

            if (includePostCode) {
                PostCodeHandle postCode = getSuburb().getPostCode();
                if (postCode != null) {
                    buffer.append(postCode.getName()+" ");
                }
            }

            if (includeCountry) {
                CountryHandle countryHandle = getCountry();
                if (countryHandle != null) {
                    buffer.append(countryHandle.getName()+" ");
                }
            }
        }
    }
}
