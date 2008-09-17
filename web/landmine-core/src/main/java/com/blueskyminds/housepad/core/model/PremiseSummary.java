package com.blueskyminds.housepad.core.model;

import com.blueskyminds.landmine.core.property.PropertyTypes;
import com.blueskyminds.housepad.core.region.composite.RegionComposite;

/**
 * Date Started: 30/10/2007
 * <p/>
 * History:
 */
public class PremiseSummary {

    private Long id;
    private RegionComposite address;
    private PropertyTypes type;
    private Integer bedrooms;
    private Integer bathrooms;

    public PremiseSummary() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RegionComposite getAddress() {
        return address;
    }

    public void setAddress(RegionComposite address) {
        this.address = address;
    }

    public PropertyTypes getType() {
        return type;
    }

    public void setType(PropertyTypes type) {
        this.type = type;
    }

    public Integer getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(Integer bedrooms) {
        this.bedrooms = bedrooms;
    }

    public Integer getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(Integer bathrooms) {
        this.bathrooms = bathrooms;
    }
}
