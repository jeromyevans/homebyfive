package com.blueskyminds.housepad.core.region.model;

import com.blueskyminds.homebyfive.framework.core.AbstractEntity;
import com.blueskyminds.homebyfive.framework.core.DomainObjectStatus;
import com.blueskyminds.housepad.core.region.model.RegionBean;
import com.blueskyminds.housepad.core.region.PathHelper;
import com.blueskyminds.housepad.core.region.reference.PostCodeRef;
import com.blueskyminds.housepad.core.region.reference.StateRef;
import com.blueskyminds.housepad.core.region.reference.CountryRef;
import com.blueskyminds.enterprise.region.suburb.SuburbHandle;
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
@Table(name="hpSuburb")
public class SuburbBean extends AbstractEntity implements RegionBean, CountryRef, StateRef, PostCodeRef {

    private String parentPath;
    private String path;
    private CountryBean countryBean;
    private String countryName;
    private String countryPath;
    private StateBean stateBean;
    private String stateName;
    private String statePath;
    private PostCodeBean postCodeBean;
    private String postCodeName;
    private String postCodePath;
    private String suburbId;
    private String name;
    private SuburbHandle suburbHandle;
    private DomainObjectStatus status;

    public SuburbBean() {
    }

    public SuburbBean(StateBean stateBean) {
        this.stateBean = stateBean;
        this.countryBean = (CountryBean) stateBean.getParent();
        populateAttributes();
    }

    public SuburbBean(CountryBean countryBean, StateBean stateBean, PostCodeBean postCodeBean, String name) {
        this.countryBean = countryBean;
        this.stateBean = stateBean;
        this.postCodeBean = postCodeBean;
        this.name = name;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="PostCodeId")
    public PostCodeBean getPostCodeBean() {
        return postCodeBean;
    }

    public void setPostCodeBean(PostCodeBean postCodeBean) {
        this.postCodeBean = postCodeBean;
    }

    @Transient
    public Long getPostCodeId() {
        if (postCodeBean != null) {
            return postCodeBean.getId();
        } else {
            return null;
        }
    }

    @Basic
    @Column(name="PostCodeName")
    public String getPostCodeName() {
        return postCodeName;
    }

    public void setPostCodeName(String postCodeName) {
        this.postCodeName = postCodeName;
    }

    @Basic
    @Column(name="PostCodePath")
    public String getPostCodePath() {
        return postCodePath;
    }

    public void setPostCodePath(String postCodePath) {
        this.postCodePath = postCodePath;
    }

    /**
     * Lowercase unique state code in the country
     * @return
     */
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
    @Column(name="SuburbId")
    public String getSuburbId() {
        return suburbId;
    }

    protected void setSuburbId(String suburbId) {
        this.suburbId = suburbId;
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

    /**
     * The handle for the Suburb implementation
     *
     * @return
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="SuburbHandleId")
    public SuburbHandle getSuburbHandle() {
        return suburbHandle;
    }

    public void setSuburbHandle(SuburbHandle suburbHandle) {
        this.suburbHandle = suburbHandle;
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
        this.countryName = stateBean.getCountryName();
        this.countryPath = countryBean.getPath();
        this.stateName = stateBean.getName();
        this.statePath = stateBean.getPath();
        if (postCodeBean != null) {
            this.postCodeName = postCodeBean.getName();
            this.postCodePath = postCodeBean.getPath();
        }
        this.suburbId = KeyGenerator.generateId(name);
        this.path = PathHelper.joinPath(parentPath, suburbId);
        this.status = DomainObjectStatus.Valid;
    }

    public void mergeWith(RegionBean suburbBean) {
        if (suburbBean instanceof SuburbBean) {
            suburbHandle.mergeWith(((SuburbBean) suburbBean).getSuburbHandle());
        }
    }

    @Transient
    public RegionHandle getRegionHandle() {
        return suburbHandle;
    }

    @Transient
    public RegionTypes getType() {
        return RegionTypes.Suburb;
    }
}
