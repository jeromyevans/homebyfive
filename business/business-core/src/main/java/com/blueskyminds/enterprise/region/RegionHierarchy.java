package com.blueskyminds.enterprise.region;

import com.blueskyminds.homebyfive.framework.framework.AbstractEntity;

import javax.persistence.*;

/**
 * A directional map between two regions.  Each region is abstracted by a RegionHandle
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

    protected RegionHandle parent;
    protected RegionHandle child;

    public RegionHierarchy(RegionHandle parent, RegionHandle child) {
        this.parent = parent;
        this.child = child;
    }

    /** Default constructor for ORM */
    protected RegionHierarchy() {
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId")
    public RegionHandle getParent() {
        return parent;
    }

    public void setParent(RegionHandle parent) {
        this.parent = parent;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "childId")
    public RegionHandle getChild() {
        return child;
    }

    public void setChild(RegionHandle child) {
        this.child = child;
    }
}