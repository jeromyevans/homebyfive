package com.blueskyminds.homebyfive.business.region.reference;

/**
 * A brief version of a RegionRef
 *
 * Date Started: 01/06/2009
 */
public class BriefRegionRef {

    private Long id;
    private String name;

    public BriefRegionRef(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
