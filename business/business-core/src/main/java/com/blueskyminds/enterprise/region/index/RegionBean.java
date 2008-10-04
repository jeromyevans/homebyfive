package com.blueskyminds.enterprise.region.index;

import com.blueskyminds.enterprise.region.graph.RegionHandle;
import com.blueskyminds.enterprise.region.RegionTypes;
import com.blueskyminds.homebyfive.framework.core.DomainObjectStatus;
import com.blueskyminds.homebyfive.framework.core.AbstractEntity;

import javax.persistence.*;

/**
 * Simplified denormalied view of an entry in the Region Graph
 *
 * Date Started: 3/03/2008
 * <p/>
 * History:
 */
@Entity
@Table(name="RegionBean")
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="Impl", discriminatorType = DiscriminatorType.CHAR)
@DiscriminatorValue("R")
public abstract class RegionBean extends AbstractEntity {

    protected String parentPath;
    protected String path;
    protected String key;
    protected String name;
    protected RegionTypes type;
    protected DomainObjectStatus status;

    protected RegionBean parent;
    protected RegionHandle regionHandle;

    /** The unique path of this region's primary parent */
    @Basic
    @Column(name="ParentPath")
    public String getParentPath() {
        return parentPath;
    }

    protected void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    /** The unique path of this region */
    @Basic
    @Column(name="Path")
    public String getPath() {
        return path;
    }

    protected void setPath(String path) {
        this.path = path;
    }

    /** unique key within the parent path */
    @Basic
    @Column(name="KeyValue")
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    /** The human readable name of this region */
    @Basic
    @Column(name="Name")
    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    @Enumerated
    @Column(name="Type")
    public RegionTypes getType() {
        return type;
    }

    public void setType(RegionTypes type) {
        this.type = type;
    }

    @Enumerated
    @Column(name="Status")
    public DomainObjectStatus getStatus() {
        return status;
    }

    public void setStatus(DomainObjectStatus status) {
        this.status = status;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ParentId")
    public RegionBean getParent() {
        return parent;
    }

    public void setParent(RegionBean parent) {
        this.parent = parent;
    }

    /**
     * The handle for the region implementation
     *
     * @return
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="RegionHandleId")
    public RegionHandle getRegionHandle() {
        return regionHandle;
    }

    public void setRegionHandle(RegionHandle regionHandle) {
        this.regionHandle = regionHandle;
    }

    /**
     * Merge this region with another region of the same type
     *
     * This operation cannot be reversed
     *
     * @param regionBean
     */
    public abstract void mergeWith(RegionBean regionBean);

    public abstract void populateAttributes();
    
}
