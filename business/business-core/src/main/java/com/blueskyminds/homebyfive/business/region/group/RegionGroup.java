package com.blueskyminds.homebyfive.business.region.group;

import com.blueskyminds.homebyfive.business.region.composite.RegionComposite;
import com.blueskyminds.homebyfive.business.region.reference.RegionRef;

import java.util.List;
import java.util.LinkedList;

/**
 * An ordered group of region composites
 *
 * Eg. states within a country
 *
 * Date Started: 5/01/2008
 * <p/>
 * History:
 */
public class RegionGroup {

    private RegionRef parent;
    private List<RegionComposite> regions;

    public RegionGroup() {
        this(null);
    }

    public RegionGroup(RegionRef parent) {
        this.parent = parent;
        regions = new LinkedList<RegionComposite>();
    }

    public RegionGroup add(RegionComposite regionComposite) {
        regions.add(regionComposite);
        return this;
    }

    public RegionGroup add(RegionRef regionRef) {
        regions.add(new RegionComposite().add(regionRef));
        return this;
    }

    public RegionComposite[] getRegions() {
        RegionComposite[] result = new RegionComposite[regions.size()];
        return regions.toArray(result);
    }

    public RegionRef getParent() {
        return parent;
    }
}
