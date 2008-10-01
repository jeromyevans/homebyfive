package com.blueskyminds.housepad.core.region.model;

import javax.persistence.*;

import com.blueskyminds.homebyfive.framework.core.AbstractEntity;
import com.blueskyminds.homebyfive.framework.core.DomainObjectStatus;
import com.blueskyminds.housepad.core.region.model.RegionBean;
import com.blueskyminds.housepad.core.region.PathHelper;
import com.blueskyminds.housepad.core.region.reference.CountryRef;
import com.blueskyminds.housepad.core.region.reference.StateRef;
import com.blueskyminds.enterprise.region.postcode.PostCodeHandle;
import com.blueskyminds.enterprise.region.RegionHandle;
import com.blueskyminds.enterprise.region.RegionTypes;
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
public class PostCodeBean extends AbstractEntity implements RegionBean, CountryRef, StateRef {

    private String parentPath;
    private String path;
    private CountryBean countryBean;
    private String countryName;
    private String countryPath;
    private StateBean stateBean;
    private String stateName;
    private String statePath;
    private String postCodeId;
    private String name;
    private PostCodeHandle postCodeHandle;
    private DomainObjectStatus status;

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

    @Basic
    @Column(name="PostCodeId")
    public String getPostCodeId() {
        return postCodeId;
    }

    protected void setPostCodeId(String postCodeId) {
        this.postCodeId = postCodeId;
    }

    @Basic
    @Column(name="Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Transient
    public RegionBean getParent() {
        return stateBean;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="PostCodeHandleId")
    public PostCodeHandle getPostCodeHandle() {
        return postCodeHandle;
    }

    public void setPostCodeHandle(PostCodeHandle postCodeHandle) {
        this.postCodeHandle = postCodeHandle;
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
        this.postCodeId = KeyGenerator.generateId(name);
        this.path = PathHelper.joinPath(parentPath, postCodeId);
        this.status = DomainObjectStatus.Valid;
    }

    public void mergeWith(RegionBean otherPostCode) {
        if (otherPostCode instanceof PostCodeBean) {
            postCodeHandle.mergeWith(((PostCodeBean) otherPostCode).getPostCodeHandle());
        }
    }

    @Transient
    public RegionHandle getRegionHandle() {
        return postCodeHandle;
    }

    @Transient
    public RegionTypes getType() {
        return RegionTypes.PostCode;
    }
}
