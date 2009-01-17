package com.blueskyminds.homebyfive.business.address;

import com.blueskyminds.homebyfive.business.region.reference.RegionRefType;
import com.blueskyminds.homebyfive.framework.core.AbstractEntity;
import java.util.Date;

import javax.persistence.*;

import org.apache.commons.lang.StringUtils;

/**
 * Provides a mapping between an observed address string and the path used to access it
 *
 * Use as a cache so address strings only have to be parsed only once ever
 *
 * Date Started: 3/07/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
@Entity
@Table(name="AddressPath")
public class AddressPath extends AbstractEntity implements SimpleAddress {

    private Date dateCreated;
    private String addressString;
    private String path;
    private RegionRefType type;

    private String address1;
    private String address2;
    private String suburb;
    private String state;
    private String postCode;
    private String countryAbbr; 

    private AddressPathStatus status;

    public AddressPath(String addressString, String path, RegionRefType type) {
        this.addressString = StringUtils.lowerCase(addressString);
        this.path = path;
        this.type = type;
        this.dateCreated = new Date();
        this.status = AddressPathStatus.Valid;
    }

    public AddressPath(String address1, String address2, String suburb, String state, String postCode, String countryAbbr, String addressString) {
        this.address1 = address1;
        this.address2 = address2;
        this.suburb = suburb;
        this.state = state;
        this.postCode = postCode;
        this.countryAbbr = countryAbbr;
        this.addressString = addressString;
        this.status = AddressPathStatus.Valid;
    }

    /** Use this constructor to identify an invalid address string */
    public AddressPath(String addressString, RegionRefType type, AddressPathStatus status) {
        this.addressString = StringUtils.lowerCase(addressString);
        this.type = type;
        this.status = status;
    }

    public AddressPath() {
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="DateCreated")
    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    /** Return the address as a concatenation of all the string components
     * @return one line address */
    @Basic
    @Column(name="AddressString")
    public String getAddressString() {
        if (StringUtils.isBlank(addressString)) {
            StringBuilder result = new StringBuilder();

            appendIfNotBlank(result, getAddress1());
            appendIfNotBlank(result, getAddress2());
            appendIfNotBlank(result, getSuburb());
            appendIfNotBlank(result, getState());
            appendIfNotBlank(result, getPostCode());

            return result.toString();
        } else {
            return addressString;
        }
    }

    public void setAddressString(String addressString) {
        this.addressString = addressString;
    }

    @Basic
    @Column(name="Path")
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Enumerated
    @Column(name="Type")
    public RegionRefType getType() {
        return type;
    }

    public void setType(RegionRefType type) {
        this.type = type;
    }

    @Basic
    @Column(name="Address1", length = 1024)
    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    @Basic
    @Column(name="Address2", length = 1024)
    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    @Basic
    @Column(name="Suburb", length = 1024)
    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    @Basic
    @Column(name="State", length = 1024)
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Basic
    @Column(name="PostCode", length = 1024)
    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    @Basic
    @Column(name="ISO3DigitCountryCode")
    public String getCountryAbbr() {
        return countryAbbr;
    }

    public void setCountryAbbr(String countryAbbr) {
        this.countryAbbr = countryAbbr;
    }

     /** Return the address1 and address2 as a concatenation
     * @return one line address */
    @Transient
    //@IgnoreProperty
    public String getStreetString() {
        StringBuilder result = new StringBuilder();

        appendIfNotBlank(result, getAddress1());
        appendIfNotBlank(result, getAddress2());

        return result.toString();
    }

    private void appendIfNotBlank(StringBuilder result, String value) {
        if (StringUtils.isNotBlank(value)) {
            if (result.length() > 0) {
                result.append(" ");
            }
            result.append(value);
        }
    }

    @Enumerated
    @Column(name="Status")
    public AddressPathStatus getStatus() {
        return status;
    }

    public void setStatus(AddressPathStatus status) {
        this.status = status;
    }

    @Transient
    public boolean isValid() {
        return !AddressPathStatus.Invalid.equals(status);
    }
}
