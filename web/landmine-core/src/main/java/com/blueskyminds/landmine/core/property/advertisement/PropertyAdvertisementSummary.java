package com.blueskyminds.landmine.core.property.advertisement;

import com.blueskyminds.landmine.core.property.PropertyAdvertisementTypes;

import java.util.Date;

/**
 * Date Started: 23/04/2008
 */
public class PropertyAdvertisementSummary {

    private Long id;

    private PropertyAdvertisementTypes type;
    private PropertyAgencySummary agency;
    private String description;
    private AskingPrice askingPrice;
    private Date dateListed;

    public PropertyAdvertisementSummary(Long propertyAdvertisementId, PropertyAdvertisementTypes type) {
        this.id = propertyAdvertisementId;
        this.type = type;
    }

    public PropertyAdvertisementTypes getType() {
        return type;
    }

    public Long getId() {
        return id;
    }

    public PropertyAgencySummary getAgency() {
        return agency;
    }

    public void setAgency(PropertyAgencySummary agency) {
        this.agency = agency;
    }

    public AskingPrice getAskingPrice() {
        return askingPrice;
    }

    public void setAskingPrice(AskingPrice askingPrice) {
        this.askingPrice = askingPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateListed() {
        return dateListed;
    }

    public void setDateListed(Date dateListed) {
        this.dateListed = dateListed;
    }
}
