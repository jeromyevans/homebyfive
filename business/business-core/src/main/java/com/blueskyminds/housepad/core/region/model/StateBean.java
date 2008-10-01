package com.blueskyminds.housepad.core.region.model;

import com.blueskyminds.homebyfive.framework.core.AbstractEntity;
import com.blueskyminds.homebyfive.framework.core.DomainObjectStatus;
import com.blueskyminds.housepad.core.region.model.RegionBean;
import com.blueskyminds.housepad.core.region.PathHelper;
import com.blueskyminds.housepad.core.region.reference.CountryRef;
import com.blueskyminds.enterprise.region.state.StateHandle;
import com.blueskyminds.enterprise.region.RegionHandle;
import com.blueskyminds.enterprise.region.RegionTypes;
import com.blueskyminds.enterprise.tools.KeyGenerator;

import javax.persistence.*;

/**
 * Date Started: 3/03/2008
 * <p/>
 * History:
 */
@Entity
@Table(name="hpState")
public class StateBean extends AbstractEntity implements RegionBean, CountryRef {

    private String parentPath;
    private String path;    
    private CountryBean countryBean;
    private String countryName;
    private String countryPath;
    private String stateId;
    private String name;
    private String abbr;
    private StateHandle stateHandle;
    private DomainObjectStatus status;

    public StateBean() {
    }

    public StateBean(CountryBean countryBean) {
        this.countryBean = countryBean;
        populateAttributes();
    }

    public StateBean(CountryBean countryBean, String name, String abbr) {
        this.countryBean = countryBean;
        this.name = name;
        this.abbr = abbr;
        populateAttributes();
    }

    @Basic
    @Column(name="ParentPath")
    public String getParentPath() {
        return parentPath;
    }

    protected void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    @Basic
    @Column(name="Path")
    public String getPath() {
        return path;
    }

    protected void setPath(String path) {
        this.path = path;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CountryId")
    public CountryBean getCountryBean() {
        return countryBean;
    }

    public void setCountryBean(CountryBean countryBean) {
        this.countryBean = countryBean;
    }

    @Transient
    public Long getCountryId() {
        return countryBean.getId();
    }

    @Basic
    @Column(name="CountryName")
    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    @Basic
    @Column(name="CountryPath")
    public String getCountryPath() {
        return countryPath;
    }

    public void setCountryPath(String countryPath) {
        this.countryPath = countryPath;
    }

    /**
     * Lowercase unique state code in the country
     * @return
     */
    @Basic
    @Column(name="StateId")
    public String getStateId() {
        return stateId;
    }

    protected void setStateId(String stateId) {
        this.stateId = stateId;
    }

    /** Full human-readable name */
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

    @Transient
    public RegionBean getParent() {
        return countryBean;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="StateHandleId")
    public StateHandle getStateHandle() {
        return stateHandle;
    }

    public void setStateHandle(StateHandle stateHandle) {
        this.stateHandle = stateHandle;
    }

    @Enumerated
    @Column(name="Status")
    public DomainObjectStatus getStatus() {
        return status;
    }

    public void setStatus(DomainObjectStatus status) {
        this.status = status;
    }

    /**
     * Populates the generated/read-only properties
     */
    public void populateAttributes() {
        this.parentPath = countryBean.getPath();
        this.countryName = countryBean.getName();
        this.countryPath = countryBean.getPath();
        this.stateId = KeyGenerator.generateId(abbr);
        this.path = PathHelper.joinPath(parentPath, stateId);
        this.status = DomainObjectStatus.Valid;
    }

    public void mergeWith(RegionBean otherState) {
        if (otherState instanceof StateBean) {
            stateHandle.mergeWith(((StateBean) otherState).getStateHandle());
        }
    }

    @Transient
    public RegionHandle getRegionHandle() {
        return stateHandle;
    }

    @Transient
    public RegionTypes getType() {
        return RegionTypes.State;
    }
}