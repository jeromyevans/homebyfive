package com.blueskyminds.housepad.core.advertisement.model;

import com.blueskyminds.housepad.core.property.model.PropertyBean;
import com.blueskyminds.landmine.core.property.PropertyAdvertisementTypes;

import javax.persistence.*;

/**
 * Date Started: 14/03/2008
 * <p/>
 * History:
 */
//@Entity
//@Table(name="hpPropertyAdvertisement")
public class PropertyAdvertisementBean {

    private PropertyBean property;
    private PropertyAdvertisementTypes type;

    public PropertyAdvertisementBean() {
    }

    /**
     * The property the advertisement is being created for
     * @return
     */
    @ManyToOne
    @JoinColumn(name="PropertyId")
    public PropertyBean getProperty() {
        return property;
    }

    public void setProperty(PropertyBean property) {
        this.property = property;
    }
    
    @Enumerated
    public PropertyAdvertisementTypes getType() {
        return type;
    }

    public void setType(PropertyAdvertisementTypes type) {
        this.type = type;
    }
}
