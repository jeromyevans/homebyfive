package com.blueskyminds.homebyfive.business.region.composite;

import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

import com.blueskyminds.homebyfive.business.region.reference.RegionRef;
import com.blueskyminds.homebyfive.business.region.reference.RegionRefType;

/**
 * An sequence of related regions
 *
 * eg. "Neutral Bay NSW Australia" contains three region references
 *
 * Date Started: 16/09/2007
 */
public class RegionComposite implements Serializable {

    private List<RegionRef> sequence;

    public RegionComposite() {
        init();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the RegionComposite with default attributes
     */
    private void init() {
        sequence = new ArrayList<RegionRef>(10);
    }

    // ------------------------------------------------------------------------------------------------------

    public RegionComposite add(Long id, String path, String name, RegionRefType type) {
        sequence.add(new RegionRef(id, path, name, null, type));
        return this;
    }

    public RegionComposite add(String path, String name, RegionRefType type) {
        sequence.add(new RegionRef(path, name, null, type));
        return this;
    }

    public RegionComposite add(RegionRef regionRef) {
        if (regionRef != null) {
            sequence.add(regionRef);
        }
        return this;
    }

    public String toString() {
        return StringUtils.join(sequence.iterator(), " ");
    }

    public RegionRef[] getSequence() {
        RegionRef[] result = new RegionRef[sequence.size()];
        return sequence.toArray(result);
    }
}
