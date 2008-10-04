package com.blueskyminds.enterprise.region.index;

import com.blueskyminds.homebyfive.framework.core.DomainObjectStatus;
import com.blueskyminds.enterprise.region.index.RegionBean;
import com.blueskyminds.enterprise.region.index.CountryBean;
import com.blueskyminds.enterprise.region.PathHelper;
import com.blueskyminds.enterprise.region.reference.CountryRef;
import com.blueskyminds.enterprise.region.graph.StateHandle;
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
public class StateBean extends RegionBean implements CountryRef {

    private CountryBean countryBean;
    private String countryName;
    private String countryPath;
    private String abbr;

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
    @Transient
    public String getStateId() {
        return key;
    }

    protected void setStateId(String stateId) {
        this.key = stateId;
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
    public StateHandle getStateHandle() {
        return (StateHandle) regionHandle;
    }

    public void setStateHandle(StateHandle stateHandle) {
        this.regionHandle = stateHandle;
    }

    /**
     * Populates the generated/read-only properties
     */
    public void populateAttributes() {
        this.parentPath = countryBean.getPath();
        this.countryName = countryBean.getName();
        this.countryPath = countryBean.getPath();
        this.key = KeyGenerator.generateId(abbr);
        this.path = PathHelper.joinPath(parentPath, key);
        this.status = DomainObjectStatus.Valid;
        this.type = RegionTypes.State;
    }

    public void mergeWith(RegionBean otherState) {
        if (otherState instanceof StateBean) {
            getStateHandle().mergeWith(((StateBean) otherState).getStateHandle());
        }
    }

}