package com.blueskyminds.homebyfive.business.region.tag;

import com.blueskyminds.homebyfive.framework.core.AbstractEntity;
import com.blueskyminds.homebyfive.business.tag.TagMap;
import com.blueskyminds.homebyfive.business.tag.Tag;
import com.blueskyminds.homebyfive.business.tag.Taggable;
import com.blueskyminds.homebyfive.business.region.graph.Region;

import javax.persistence.*;

import org.jboss.envers.Versioned;

/**
 * Date Started: 28/10/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
@Entity
@Table(name="RegionTagMap")
@Versioned
public class RegionTagMap extends AbstractEntity implements TagMap {

    private Region region;
    private Tag tag;

    public RegionTagMap(Region region, Tag tag) {
        this.region = region;
        this.tag = tag;
    }

    /** Default constructor for ORM*/
    public RegionTagMap() {
    }


    @ManyToOne
    @JoinColumn(name="RegionId")
    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    @ManyToOne
    @JoinColumn(name="TagId")
    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    @Transient
    public Taggable getTarget() {
        return region;
    }

}
