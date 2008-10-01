package com.blueskyminds.enterprise.region;

import com.blueskyminds.homebyfive.framework.framework.tools.Named;
import com.blueskyminds.homebyfive.framework.framework.AbstractEntity;

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

    private RegionHandle regionHandle;
    private String name;

    public RegionAlias(RegionHandle regionHandle, String name) {
        this.regionHandle = regionHandle;
        this.name = name;
    }

    /** Default constructor for ORM */
    public RegionAlias() {
    }

    @ManyToOne
    @JoinColumn(name="RegionHandleId")
    public RegionHandle getRegionHandle() {
        return regionHandle;
    }

    public void setRegionHandle(RegionHandle regionHandle) {
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
