package com.blueskyminds.homebyfive.business.region.index;

import com.blueskyminds.homebyfive.business.region.graph.Region;
import com.blueskyminds.homebyfive.business.region.RegionTypes;
import com.blueskyminds.homebyfive.framework.core.DomainObjectStatus;
import com.blueskyminds.homebyfive.framework.core.AbstractEntity;

import javax.persistence.*;

/**
 * Denormalized view of an entry in the Region Graph
 *
 * Date Started: 3/03/2008
 * <p/>
 * History:
 */
@Entity
@Table(name="RegionIndex")
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="Impl", discriminatorType = DiscriminatorType.CHAR)
@DiscriminatorValue("R")
public abstract class RegionIndex extends AbstractEntity {

    protected String parentPath;
    protected String path;
    protected String key;
    protected String name;
    protected String abbr;
    protected RegionTypes type;
    protected DomainObjectStatus status;

    protected RegionIndex parent;
    protected Region region;

    // --- denormalised values
    protected String countryName;
    protected String countryPath;
    protected String countryId;

    protected String stateName;
    protected String statePath;
    protected String stateId;

    protected String postalCodeName;
    protected String postalCodePath;
    protected String postalCodeId;

    protected String suburbName;
    protected String suburbPath;
    protected String suburbId;

    protected RegionIndex(Region region) {
        this.region = region;
        this.name = region.getName();
    }


    protected RegionIndex() {
    }

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

    @Basic
    @Column(name="Abbr")
    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
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
    public RegionIndex getParent() {
        return parent;
    }

    public void setParent(RegionIndex parent) {
        this.parent = parent;
    }

    /**
     * The handle for the region implementation
     *
     * @return
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="RegionId")
    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryPath() {
        return countryPath;
    }

    public void setCountryPath(String countryPath) {
        this.countryPath = countryPath;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStatePath() {
        return statePath;
    }

    public void setStatePath(String statePath) {
        this.statePath = statePath;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getPostalCodeName() {
        return postalCodeName;
    }

    public void setPostalCodeName(String postalCodeName) {
        this.postalCodeName = postalCodeName;
    }

    public String getPostalCodePath() {
        return postalCodePath;
    }

    public void setPostalCodePath(String postalCodePath) {
        this.postalCodePath = postalCodePath;
    }

    public String getPostalCodeId() {
        return postalCodeId;
    }

    public void setPostalCodeId(String postalCodeId) {
        this.postalCodeId = postalCodeId;
    }

    public String getSuburbName() {
        return suburbName;
    }

    public void setSuburbName(String suburbName) {
        this.suburbName = suburbName;
    }

    public String getSuburbPath() {
        return suburbPath;
    }

    public void setSuburbPath(String suburbPath) {
        this.suburbPath = suburbPath;
    }

    public String getSuburbId() {
        return suburbId;
    }

    public void setSuburbId(String suburbId) {
        this.suburbId = suburbId;
    }

    /**
     * Merge this region with another region of the same type
     *
     * This operation cannot be reversed
     *
     * @param regionBean
     */
    public abstract void mergeWith(RegionIndex regionBean);

    public abstract void populateDenormalizedAttributes();

    @PrePersist
    void prePersist() {
        populateDenormalizedAttributes();
    }
}
