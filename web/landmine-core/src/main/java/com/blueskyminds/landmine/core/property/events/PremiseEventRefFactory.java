package com.blueskyminds.landmine.core.property.events;

import com.blueskyminds.housepad.core.property.model.PropertyBean;

/**
 * Date Started: 22/06/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class PremiseEventRefFactory {

    public static PremiseEventRef create(PropertyBean propertyBean) {
        return new PremiseEventRef(propertyBean.getEventPath(), propertyBean.getEventType(), propertyBean.getEventDate(), propertyBean.getEventDescription());
    }
}
