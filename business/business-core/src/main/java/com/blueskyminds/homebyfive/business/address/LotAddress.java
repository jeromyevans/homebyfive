package com.blueskyminds.homebyfive.business.address;

import org.apache.commons.lang.StringUtils;
import org.jboss.envers.Versioned;

import javax.persistence.*;

import com.blueskyminds.homebyfive.business.region.graph.Suburb;
import com.blueskyminds.homebyfive.business.region.graph.PostalCode;
import com.blueskyminds.homebyfive.business.region.graph.Street;

/**
 * A Lot address is a type of physical address typically used to:
 *   - identifying real property prior to it being assigned a street address
 *   - identifying real property that is not on a recognised street (eg. in the country)
 *
 * User: Jeromy
 * Date: 16/04/2006
 * Time: 13:01:55
 *
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
@Entity
@DiscriminatorValue("Lot")
@Versioned
public class LotAddress extends StreetAddress {

    private String lotNumber;

    // ------------------------------------------------------------------------------------------------------

    public LotAddress(String lotNumber, String streetNumber, Street street, Suburb suburb, PostalCode postCode) {
        super(streetNumber, street, suburb,  postCode);
        this.lotNumber = lotNumber;
    }

    /** Default constructor for ORM */
    protected LotAddress() {

    }

    @Transient
    public String getNumber() {
        if (lotNumber != null) {
            if (getStreetNumber() != null) {
                return "Lot "+lotNumber + " "+super.getNumber();
            } else {
                return "Lot "+lotNumber;
            }
        } else {
            return super.getNumber();
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the lot number for this address.
     */
    @Basic
    @Column(name="LotNumber")
    public String getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(StringUtils.isNotBlank(lotNumber) ? "[Lot "+lotNumber+"] " : "");

        appendStreetNumber(buffer);
        appendStreet(buffer);
        appendSuburb(buffer);

        return buffer.toString();
    }

    public String format(boolean includeState, boolean includePostCode, boolean includeCountry) {
        StringBuilder buffer = new StringBuilder();

        buffer.append(StringUtils.isNotBlank(lotNumber) ? "Lot "+lotNumber+" " : "");
        buffer.append(StringUtils.isNotBlank(getStreetNumber()) ? getStreetNumber()+ " " : "");

        if (getStreet() != null) {
            String streetName = getStreet().getFullName();
            buffer.append(StringUtils.isNotBlank(streetName) ? streetName+ " " : "");
        }

        formatSuburb(buffer, includeState, includePostCode, includeCountry);

        return buffer.toString();
    }
}
