package com.blueskyminds.housepad.core.analysis.suburb;

import com.blueskyminds.landmine.core.property.PropertyTypes;
import com.blueskyminds.framework.datetime.*;

/**
 * Date Started: 6/12/2007
 * <p/>
 * History:
 */
public class AnalysisSet {

    private PropertyTypes propertyType;
    private Integer beds;
    private Integer baths;
    private Double minAskingPrice;
    private Double maxAskingPrice;
    private Double medianAskingPrice;
    private Double medianRent;
    private Double yield;

    public PropertyTypes getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyTypes propertyType) {
        this.propertyType = propertyType;
    }

    public Integer getBeds() {
        return beds;
    }

    public void setBeds(Integer beds) {
        this.beds = beds;
    }

    public Integer getBaths() {
        return baths;
    }

    public void setBaths(Integer baths) {
        this.baths = baths;
    }

    public Double getMedianAskingPrice() {
        return medianAskingPrice;
    }

    public void setMedianAskingPrice(Double medianAskingPrice) {
        this.medianAskingPrice = medianAskingPrice;
    }

    public Double getMedianRent() {
        return medianRent;
    }

    public void setMedianRent(Double medianRent) {
        this.medianRent = medianRent;
    }

    public Double getYield() {
        return yield;
    }

    public void setYield(Double yield) {
        this.yield = yield;
    } 
}
