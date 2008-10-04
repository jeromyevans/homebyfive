package com.blueskyminds.enterprise.region.graph;

import com.blueskyminds.homebyfive.framework.core.tools.Named;
import com.blueskyminds.homebyfive.framework.core.AbstractEntity;

import javax.persistence.*;

/**
 * An alias for a region (an alternative name)
 *
 * Date Started: 7/07/2007
 * <p/>
 * History:
 */
@Entity
public class RegionAlias extends AbstractEntity implements Named {

    private Region regionHandle;
    private String name;

    public RegionAlias(Region regionHandle, String name) {
        this.regionHandle = regionHandle;
        this.name = name;
    }

    /** Default constructor for ORM */
    public RegionAlias() {
    }

    @ManyToOne
    @JoinColumn(name="RegionHandleId")
    public Region getRegionHandle() {
        return regionHandle;
    }

    public void setRegionHandle(Region regionHandle) {
        this.regionHandle = regionHandle;
    }

    @Basic
    @Column(name="Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
