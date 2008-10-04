package com.blueskyminds.enterprise.region.index;

import com.blueskyminds.homebyfive.framework.core.DomainObjectStatus;
import com.blueskyminds.enterprise.region.index.RegionBean;
import com.blueskyminds.enterprise.region.index.CountryBean;
import com.blueskyminds.enterprise.region.index.PostCodeBean;
import com.blueskyminds.enterprise.region.index.StateBean;
import com.blueskyminds.enterprise.region.PathHelper;
import com.blueskyminds.enterprise.region.reference.PostCodeRef;
import com.blueskyminds.enterprise.region.reference.StateRef;
import com.blueskyminds.enterprise.region.reference.CountryRef;
import com.blueskyminds.enterprise.region.graph.SuburbHandle;
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
public class SuburbBean extends RegionBean implements CountryRef, StateRef, PostCodeRef {

    private CountryBean countryBean;
    private String countryName;
    private String countryPath;
    private StateBean stateBean;
    private String stateName;
    private String statePath;
    private PostCodeBean postCodeBean;
    private String postCodeName;
    private String postCodePath;

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

    @Transient
    public String getSuburbId() {
        return key;
    }

    protected void setSuburbId(String suburbId) {
        this.key = suburbId;
    }

    /**
     * The handle for the Suburb implementation
     *
     * @return
     */
    @Transient
    public SuburbHandle getSuburbHandle() {
        return (SuburbHandle) regionHandle;
    }

    public void setSuburbHandle(SuburbHandle suburbHandle) {
        this.regionHandle = suburbHandle;
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
        this.key = KeyGenerator.generateId(name);
        this.path = PathHelper.joinPath(parentPath, key);
        this.status = DomainObjectStatus.Valid;
        this.type = RegionTypes.Suburb;
    }

    public void mergeWith(RegionBean suburbBean) {
        if (suburbBean instanceof SuburbBean) {
            getSuburbHandle().mergeWith(((SuburbBean) suburbBean).getSuburbHandle());
        }
    }

}
