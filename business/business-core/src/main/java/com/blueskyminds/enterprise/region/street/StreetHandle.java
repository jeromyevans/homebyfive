package com.blueskyminds.enterprise.region.street;

import com.blueskyminds.enterprise.region.RegionHandle;
import com.blueskyminds.enterprise.region.RegionTypes;

import javax.persistence.Entity;
import javax.persistence.DiscriminatorValue;

/**
 * Date Started: 4/10/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
@Entity
@DiscriminatorValue("T")
public class StreetHandle extends RegionHandle {

    public StreetHandle(String name) {
        super(name, RegionTypes.Street);
    }

    /**
     * Create a RegionHandle with the specified name and aliases
     */
    public StreetHandle(String name,  String... aliases) {
        super(name, RegionTypes.Street, aliases);
    }

    protected StreetHandle() {
    }
}
