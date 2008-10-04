package com.blueskyminds.enterprise.region.index;

import javax.persistence.*;

import com.blueskyminds.homebyfive.framework.core.DomainObjectStatus;
import com.blueskyminds.enterprise.region.index.RegionBean;
import com.blueskyminds.enterprise.region.PathHelper;
import com.blueskyminds.enterprise.region.index.CountryBean;
import com.blueskyminds.enterprise.region.reference.CountryRef;
import com.blueskyminds.enterprise.region.reference.StateRef;
import com.blueskyminds.enterprise.region.graph.PostalCode;
import com.blueskyminds.enterprise.region.RegionTypes;
import com.blueskyminds.enterprise.region.index.StateBean;
import com.blueskyminds.enterprise.tools.KeyGenerator;

/**
 * Denormalized postcode bean for fast lookup by path
 *
 * Date Started: 5/03/2008
 * <p/>
 * History:
 */
@Entity
@Table(name="hpPostCode")
public class PostCodeBean extends RegionBean implements CountryRef, StateRef {

    private CountryBean countryBean;
    private String countryName;
    private String countryPath;
    private StateBean stateBean;
    private String stateName;
    private String statePath;


    public PostCodeBean() {
    }

    public PostCodeBean(CountryBean countryBean, StateBean stateBean, String name) {
        this.countryBean = countryBean;
        this.stateBean = stateBean;
        this.name = name;
        populateAttributes();
    }

    public PostCodeBean(StateBean stateBean) {
        this.countryBean = (CountryBean) stateBean.getParent();
        this.stateBean = stateBean;
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

    @Transient
    public Long getCountryId() {
        return countryBean.getId();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="StateId")
    public StateBean getStateBean() {
        return stateBean;
    }

    public void setStateBean(StateBean stateBean) {
        this.stateBean = stateBean;
    }

    @Transient
    public Long getStateId() {
        return stateBean.getId();
    }

    @Basic
    @Column(name="StateName")
    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    @Basic
    @Column(name="StatePath")
    public String getStatePath() {
        return statePath;
    }

    public void setStatePath(String statePath) {
        this.statePath = statePath;
    }

    @Transient
    public String getPostCodeId() {
        return key;
    }

    protected void setPostCodeId(String postCodeId) {
        this.key = postCodeId;
    }

    @Transient
    public PostalCode getPostCodeHandle() {
        return (PostalCode) regionHandle;
    }

    public void setPostCodeHandle(PostalCode postCodeHandle) {
        this.regionHandle = postCodeHandle;
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
        this.parentPath = stateBean.getPath();
        this.countryPath = countryBean.getPath();
        this.countryName = countryBean.getPath();
        this.statePath = stateBean.getPath();
        this.stateName = stateBean.getName();
        this.key = KeyGenerator.generateId(name);
        this.path = PathHelper.joinPath(parentPath, key);
        this.status = DomainObjectStatus.Valid;
        this.type = RegionTypes.PostCode;
    }

    public void mergeWith(RegionBean otherPostCode) {
        if (otherPostCode instanceof PostCodeBean) {
            getPostCodeHandle().mergeWith(((PostCodeBean) otherPostCode).getPostCodeHandle());
        }
    }

}
