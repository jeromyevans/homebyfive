package com.blueskyminds.landmine.core.property.advertisement;

import com.blueskyminds.housepad.core.region.composite.RegionComposite;
import com.blueskyminds.housepad.core.region.composite.RegionCompositeFactory;
import com.blueskyminds.enterprise.address.Address;

import java.util.List;
import java.util.LinkedList;

/**
 * Date Started: 23/04/2008
 */
public class PropertyAgencySummary {

    private RegionComposite address;
    private String name;
    private String website;
    private String email;
    private String phone;
    private String fax;
    private List<PropertyAgencyContactSummary> contacts;

    public PropertyAgencySummary(String name) {
        this.name = name;
        contacts = new LinkedList<PropertyAgencyContactSummary>();
    }

    public PropertyAgencySummary() {
        contacts = new LinkedList<PropertyAgencyContactSummary>();
    }
   
    public static PropertyAgencySummary create(String name) {
        return new PropertyAgencySummary(name);
    }

    public PropertyAgencySummary withAddress(Address address) {
        if (address != null) {
            this.address = RegionCompositeFactory.create(address);
        }
        return this;
    }

    public PropertyAgencySummary withWebsite(String website) {
        this.website = website;
        return this;
    }

    public PropertyAgencySummary withEmail(String email) {
        this.email = email;
        return this;
    }

    public PropertyAgencySummary withPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public PropertyAgencySummary withFax(String fax) {
        this.fax = fax;
        return this;
    }

    public RegionComposite getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getWebsite() {
        return website;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getFax() {
        return fax;
    }

    public void addContact(PropertyAgencyContactSummary contact) {
        contacts.add(contact);
    }

    public List<PropertyAgencyContactSummary> getContacts() {
        return contacts;
    }
}
