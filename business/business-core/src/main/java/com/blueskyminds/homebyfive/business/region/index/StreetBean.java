package com.blueskyminds.homebyfive.business.region.index;

import com.blueskyminds.homebyfive.business.tools.KeyGenerator;
import com.blueskyminds.homebyfive.business.region.PathHelper;
import com.blueskyminds.homebyfive.business.region.RegionTypes;
import com.blueskyminds.homebyfive.business.region.graph.Street;
import com.blueskyminds.homebyfive.business.region.graph.Region;
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
public class StreetBean extends RegionIndex {

    public StreetBean() {
    }

    public StreetBean(Region regionHandle) {
        super(regionHandle);
        populateDenormalizedAttributes();
    }

    @Transient
    public Street getStreetHandle() {
        return (Street) region;
    }

    public void populateDenormalizedAttributes() {
        this.key = KeyGenerator.generateId(name);

        // the suburb is the parent
        parent = getStreetHandle().getSuburb().getRegionIndex();

        if (parent != null) {
            this.path = PathHelper.joinPath(parentPath, key);
            this.status = DomainObjectStatus.Valid;
            this.type = RegionTypes.Street;

            this.countryId = parent.getCountryId();
            this.countryPath = parent.getCountryPath();
            this.countryName = parent.getCountryName();

            this.stateId = parent.getStateId();
            this.statePath = parent.getStatePath();
            this.stateName = parent.getStateName();

            this.suburbId = parent.getKey();
            this.suburbPath = parent.getPath();
            this.suburbName = parent.getName();
        }
    }

    /**
     * Merge this region with another region of the same type
     * <p/>
     * This operation cannot be reversed
     *
     * @param regionBean
     */
    public void mergeWith(RegionIndex regionBean) {
        if (regionBean instanceof StreetBean) {
            getStreetHandle().mergeWith(((StreetBean) regionBean).getStreetHandle());
        }
    }
}
