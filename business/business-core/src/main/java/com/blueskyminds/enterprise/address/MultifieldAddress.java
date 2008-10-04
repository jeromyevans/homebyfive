package com.blueskyminds.enterprise.address;

import com.blueskyminds.enterprise.region.graph.Street;
import com.blueskyminds.enterprise.region.graph.PostalCode;
import com.blueskyminds.enterprise.region.graph.Suburb;
import org.apache.commons.lang.StringUtils;

/**
 * A non-persistent simple address separated into fields but unparsed
 *
 * Date Started: 4/06/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class MultifieldAddress {

    protected String unitNo;
    protected String lotNo;
    protected String streetNo;
    protected String streetName;
    protected StreetType streetType;
    protected StreetSection streetSection;
    protected String suburb;
    protected String postCode;
    protected String state;
    protected String country;

    public MultifieldAddress() {
    }

    public MultifieldAddress(Address address) {
        this.unitNo = AddressHelper.unitNo(address);
        this.lotNo = AddressHelper.lotNo(address);
        this.streetNo = AddressHelper.streetNo(address);
        this.streetName= AddressHelper.streetName(address);
        this.streetType = AddressHelper.streetType(address);
        this.streetSection = AddressHelper.streetSection(address);
        this.suburb = AddressHelper.suburb(address);
        this.state = AddressHelper.state(address);
        this.country = AddressHelper.country(address);
    }

    public String getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public String getStreetNo() {
        return streetNo;
    }

    public void setStreetNo(String streetNo) {
        this.streetNo = streetNo;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public StreetType getStreetType() {
        return streetType;
    }

    public void setStreetType(StreetType streetType) {
        this.streetType = streetType;
    }

    public StreetSection getStreetSection() {
        return streetSection;
    }

    public void setStreetSection(StreetSection streetSection) {
        this.streetSection = streetSection;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Augment this address with the information known frmo the RegionHandle.
     *
     * For example, if supplied a SuburbHandle, a StreetAddress, UnitAddress or LotAddress in that suburb will
     *  be returned
     *
     * todo: if we know the state or country, that's still useful isn't it?
     * todo: street support
     *
     * @param suburb
     * @return a new Address implementation if possible, otherwise null
     */
    public Address augmentWithKnown(Suburb suburb) {
        Address address = null;
        PostalCode postCode;

        if (suburb != null) {

            postCode = suburb.getPostCode();
            
            Street street = new Street(streetName, streetType, streetSection);

            if (StringUtils.isNotBlank(unitNo)) {
                address = new UnitAddress(unitNo, streetNo, street, suburb, postCode);
            } else {
                if (StringUtils.isNotBlank(lotNo)) {
                    address = new LotAddress(lotNo, streetNo, street, suburb, postCode);
                } else {
                    if (StringUtils.isNotBlank(streetNo)) {
                        address = new StreetAddress(streetNo, street, suburb, postCode);
                    }
                }
            }
        }

        return address;
    }

    /** Format this address for human readability */
    public String format(boolean includeState, boolean includePostCode, boolean includeCountry) {
        StringBuilder buffer = new StringBuilder();

        buffer.append(StringUtils.isNotBlank(unitNo) ? unitNo + "/" : "");
        buffer.append(StringUtils.isNotBlank(lotNo) ? "Lot "+lotNo + " " : "");
        buffer.append(StringUtils.isNotBlank(streetNo) ? streetNo+ " " : "");

        buffer.append(StringUtils.isNotBlank(streetName) ? streetName+ " " : "");
        buffer.append(streetType != null ? streetType + " " : "");
        buffer.append(streetSection != null ? streetSection + " " : "");

        buffer.append(StringUtils.isNotBlank(suburb) ? suburb + " " : "");

        if (includeState) {
            buffer.append(StringUtils.isNotBlank(state) ? state + " " : "");
        }
        if (includePostCode) {
            buffer.append(StringUtils.isNotBlank(postCode) ? postCode + " " : "");
        }
        if (includeCountry) {
            buffer.append(StringUtils.isNotBlank(country) ? country + " " : "");
        }

        return buffer.toString();
    }

    public boolean isUnitAddress() {
        return StringUtils.isNotBlank(unitNo);
    }

    public boolean isLotAddress() {
        return StringUtils.isNotBlank(lotNo);
    }

}
