package com.blueskyminds.business.region.graph;

import com.blueskyminds.homebyfive.framework.core.AbstractEntity;

import javax.persistence.*;

/**
 * A directional map between two regions.
 *
 * An edge between two vertices in a region graph.
 *
 * Date Started: 2/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd<br/>
 *  */
@Entity
public class RegionHierarchy extends AbstractEntity {

    protected Region parent;
    protected Region child;

    public RegionHierarchy(Region parent, Region child) {
        this.parent = parent;
        this.child = child;
    }

    /** Default constructor for ORM */
    protected RegionHierarchy() {
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId")
    public Region getParent() {
        return parent;
    }

    public void setParent(Region parent) {
        this.parent = parent;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "childId")
    public Region getChild() {
        return child;
    }

    public void setChild(Region child) {
        this.child = child;
    }
}