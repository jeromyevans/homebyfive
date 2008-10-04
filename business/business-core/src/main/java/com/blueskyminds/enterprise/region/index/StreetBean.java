package com.blueskyminds.enterprise.region.index;

import com.blueskyminds.enterprise.tools.KeyGenerator;
import com.blueskyminds.enterprise.region.PathHelper;
import com.blueskyminds.enterprise.region.RegionTypes;
import com.blueskyminds.enterprise.region.graph.Street;
import com.blueskyminds.homebyfive.framework.core.DomainObjectStatus;

import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.persistence.DiscriminatorValue;

/**
 * Date Started: 4/10/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
@Entity
@DiscriminatorValue("T")
public class StreetBean extends RegionBean {

    public StreetBean() {
    }

    @Transient
    public Street getStreetHandle() {
        return (Street) regionHandle;
    }

    public void populateAttributes() {
        this.key = KeyGenerator.generateId(name);
        this.path = PathHelper.joinPath(parentPath, key);
        this.status = DomainObjectStatus.Valid;
        this.type = RegionTypes.Suburb;
    }

    /**
     * Merge this region with another region of the same type
     * <p/>
     * This operation cannot be reversed
     *
     * @param regionBean
     */
    public void mergeWith(RegionBean regionBean) {
        if (regionBean instanceof StreetBean) {
            getStreetHandle().mergeWith(((StreetBean) regionBean).getStreetHandle());
        }
    }
}
