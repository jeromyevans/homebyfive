package com.blueskyminds.housepad.core.property.model;

import com.blueskyminds.housepad.core.address.model.AddressBean;
import com.blueskyminds.housepad.core.region.model.RegionBean;
import com.blueskyminds.landmine.core.property.PropertyTypes;
import com.blueskyminds.enterprise.region.RegionHandle;
import org.apache.commons.lang.StringUtils;

/**
 * Simple representation of a property by its address
 *
 * Date Started: 4/06/2008
 *
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class PropertyFormBean extends AddressBean {

    private String name;

    public PropertyFormBean(PropertyBean propertyBean) {
        super(propertyBean.getSuburbBean());
        this.name = propertyBean.getName();        
    }

    /**
     * Create an AddressBean pre-populated with the attributes of a RegionBean
     *
     * @param region
     */
    public PropertyFormBean(RegionBean region) {
        super(region);
    }

    public PropertyFormBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
