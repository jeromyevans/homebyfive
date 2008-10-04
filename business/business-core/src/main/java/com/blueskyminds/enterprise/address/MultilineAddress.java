package com.blueskyminds.enterprise.address;

import com.blueskyminds.enterprise.region.graph.Suburb;
import com.blueskyminds.enterprise.region.graph.PostalCode;
import com.blueskyminds.enterprise.region.graph.Street;

import javax.persistence.*;

import org.apache.commons.lang.StringUtils;

/**
 * Represents a simple multi-line address
 *
 * Date Started: 8/06/2006
 *
 * History:
 *
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd 
 */
@Entity
@DiscriminatorValue("Multiline")
public class MultilineAddress extends Address {

    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String addressLine4;

    // ------------------------------------------------------------------------------------------------------

    public MultilineAddress(String addressLine1, String addressLine2, String addressLine3, String addressLine4, Suburb suburb, PostalCode postCode) {
        super(suburb, postCode);
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.addressLine3 = addressLine3;
        this.addressLine4 = addressLine4;
    }

    /** Default constructor for ORM */
    protected MultilineAddress() {

    }

    @Transient
    public String getNumber() {
        return null;
    }

    @Transient
    public Street getStreet() {
        return null;
    }

    // ------------------------------------------------------------------------------------------------------

    @Basic
    @Column(name="Line1")
    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    // ------------------------------------------------------------------------------------------------------

    @Basic
    @Column(name="Line2")
    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    // ------------------------------------------------------------------------------------------------------

    @Basic
    @Column(name="Line3")
    public String getAddressLine3() {
        return addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

    // ------------------------------------------------------------------------------------------------------

    @Basic
    @Column(name="Line4")
    public String getAddressLine4() {
        return addressLine4;
    }

    public void setAddressLine4(String addressLine4) {
        this.addressLine4 = addressLine4;
    }

    // ------------------------------------------------------------------------------------------------------

    public String format(boolean includeState, boolean includePostCode, boolean includeCountry) {
        StringBuilder buffer = new StringBuilder();

        buffer.append(StringUtils.isNotBlank(addressLine1) ? addressLine1 + " " : "");
        buffer.append(StringUtils.isNotBlank(addressLine2) ? addressLine2 + " " : "");
        buffer.append(StringUtils.isNotBlank(addressLine3) ? addressLine3 + " " : "");
        buffer.append(StringUtils.isNotBlank(addressLine4) ? addressLine4 + " " : "");

        formatSuburb(buffer, includeState, includePostCode, includeCountry);

        return buffer.toString();
    }
}
