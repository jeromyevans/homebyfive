package com.blueskyminds.enterprise.address;

import com.blueskyminds.enterprise.region.postcode.PostCodeHandle;
import com.blueskyminds.enterprise.region.suburb.SuburbHandle;
import com.blueskyminds.enterprise.region.state.StateHandle;
import com.blueskyminds.enterprise.region.country.CountryHandle;

import javax.persistence.*;
import java.io.PrintStream;

import org.apache.commons.lang.StringUtils;

/**
 * An address that is in the full-text form - unvalidated and not separated into fields
 *
 * Date Started: 8/06/2006
 *
 * History:
 *
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
@Entity
@DiscriminatorValue("PlainText")
public class PlainTextAddress extends Address {

    private String value;

    /* Create an address that is in the full-text form - unvalidated and not separated into fields */
    public PlainTextAddress(String address) {
        super(null, null);
        this.value = address;
    }

    /* Create an address that is in the full-text form - unvalidated and not separated into fields, but with a
     known suburb*/
    public PlainTextAddress(String address, SuburbHandle suburb, PostCodeHandle postCode) {
        super(suburb, postCode);
        this.value = address;
    }

    /** Default constructor for ORM */
    protected PlainTextAddress() {

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

    /** Get the plain text address value */
    @Basic
    @Column(name="PlainAddress")
    public String getAddress() {
        return value;
    }

    public void setAddress(String value) {
        this.value = value;
    }

    // ------------------------------------------------------------------------------------------------------

    public void print(PrintStream out) {
        out.println(getIdentityName()+" ("+value+")");
    }

    // ------------------------------------------------------------------------------------------------------

    public String format(boolean includeState, boolean includePostCode, boolean includeCountry) {
        StringBuilder buffer = new StringBuilder();

        buffer.append(StringUtils.isNotBlank(value) ? value + " " : "");
        
        formatSuburb(buffer, includeState, includePostCode, includeCountry);

        return buffer.toString();
    }
}

