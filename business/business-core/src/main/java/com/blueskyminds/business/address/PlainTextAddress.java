package com.blueskyminds.business.address;

import com.blueskyminds.business.region.graph.PostalCode;
import com.blueskyminds.business.region.graph.Suburb;
import com.blueskyminds.business.region.graph.Street;

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
    public PlainTextAddress(String address, Suburb suburb, PostalCode postCode) {
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

