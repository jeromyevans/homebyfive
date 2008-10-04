package com.blueskyminds.enterprise.region.country;

import com.blueskyminds.homebyfive.framework.core.AbstractEntity;
import com.blueskyminds.homebyfive.framework.core.DomainObjectStatus;
import com.blueskyminds.enterprise.region.RegionBean;
import com.blueskyminds.housepad.core.region.PathHelper;
import com.blueskyminds.enterprise.region.country.CountryHandle;
import com.blueskyminds.enterprise.region.RegionHandle;
import com.blueskyminds.enterprise.region.RegionTypes;
import com.blueskyminds.enterprise.tools.KeyGenerator;

import javax.persistence.*;

import org.apache.commons.lang.StringUtils;

/**
 * Date Started: 3/03/2008
 * <p/>
 * History:
 */
@Entity
@Table(name="hpCountry")
public class CountryBean extends AbstractEntity implements RegionBean {
    
    private String path;
    private String countryid;
    private String name;
    private String abbr;
    private CountryHandle countryHandle;
    private DomainObjectStatus status;
    
    public CountryBean() {
    }

    public CountryBean(String name, String abbr) {
        this.name = name;
        this.abbr = abbr;
        populateAttributes();
    }
    /**
     * The full path (eg. /au
     *
     * @return
     */
    @Column(name="Path")
    public String getPath() {
        return path;
    }

    protected void setPath(String path) {
        this.path = path;
    }

    /**
     * eg au
     *
     * @return
     */
    @Basic
    @Column(name="CountryId")
    public String getCountryid() {
        return countryid;
    }

    protected void setCountryid(String countryid) {
        this.countryid = countryid;
    }

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
        return null;  
    }

    @Transient
    public String getParentPath() {
        return PathHelper.ROOT;
    }

    /**
     * The handle for the Country implementation
     *
     * @return
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CountryHandleId")
    public CountryHandle getCountryHandle() {
        return countryHandle;
    }

    public void setCountryHandle(CountryHandle countryHandle) {
        this.countryHandle = countryHandle;
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
        this.countryid = KeyGenerator.generateId(abbr);
        this.path = PathHelper.joinPath(getParentPath(), countryid);
        this.status = DomainObjectStatus.Valid;
    }


    @Transient
    public boolean isValid() {
        return path != null && path.length() > 1 && StringUtils.isNotBlank(abbr) && (StringUtils.isNotBlank(name));
    }

    public void mergeWith(RegionBean otherCountryBean) {
        if (otherCountryBean instanceof CountryBean) {
            countryHandle.mergeWith(((CountryBean) otherCountryBean).getCountryHandle());
        }
    }

    @Transient
    public RegionHandle getRegionHandle() {
        return countryHandle;
    }

    @Transient
    public RegionTypes getType() {
        return RegionTypes.Country;
    }
}
