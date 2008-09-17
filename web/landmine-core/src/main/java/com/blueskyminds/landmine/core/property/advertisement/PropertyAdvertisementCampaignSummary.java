package com.blueskyminds.landmine.core.property.advertisement;

import com.blueskyminds.landmine.core.property.PropertyAdvertisementTypes;

import java.util.List;
import java.util.LinkedList;

/**
 * Date Started: 23/04/2008
 */
public class PropertyAdvertisementCampaignSummary {

    private Long id;
    private PropertyAdvertisementTypes type;
    private List<PropertyAdvertisementSummary> advertisements;

    public PropertyAdvertisementCampaignSummary(Long id, PropertyAdvertisementTypes type) {
        this.id = id;
        this.type = type;
        advertisements = new LinkedList<PropertyAdvertisementSummary>();
    }

    public Long getId() {
        return id;
    }

    public PropertyAdvertisementTypes getType() {
        return type;
    }

    public void addAdvertisement(PropertyAdvertisementSummary advertisementSummary) {
        this.advertisements.add(advertisementSummary);
    }

    public List<PropertyAdvertisementSummary> getAdvertisements() {
        return advertisements;
    }
}
