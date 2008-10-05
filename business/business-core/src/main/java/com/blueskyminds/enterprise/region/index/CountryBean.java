package com.blueskyminds.enterprise.region.index;

import com.blueskyminds.homebyfive.framework.core.DomainObjectStatus;
import com.blueskyminds.enterprise.region.index.RegionIndex;
import com.blueskyminds.enterprise.region.PathHelper;
import com.blueskyminds.enterprise.region.graph.Country;
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
@DiscriminatorValue("C")
public class CountryBean extends RegionIndex {

    public CountryBean() {
    }

    public CountryBean(String name, String abbr) {
        this.name = name;
        this.abbr = abbr;
        populateDenormalizedAttributes();
    }

    public CountryBean(Country countryHandle) {
        super(countryHandle);
        this.abbr = countryHandle.getAbbr();
        populateDenormalizedAttributes();
    }

    /**
     * eg au
     *
     * @return
     */
    @Transient
    public String getCountryid() {
        return key;
    }

    protected void setCountryid(String countryid) {
        this.key = countryid;
    }

    /**
     * The handle for the Country implementation
     *
     * @return
     */
    @Transient
    public Country getCountryHandle() {
        return (Country) region;
    }

    public void setCountryHandle(Country countryHandle) {
        this.region = countryHandle;
    }

    /**
     * Populates the generated/read-only properties
     */
    public void populateDenormalizedAttributes() {
        this.key = KeyGenerator.generateId(abbr);
        this.parentPath = PathHelper.ROOT;
        this.path = PathHelper.joinPath(getParentPath(), key);
        this.status = DomainObjectStatus.Valid;
        this.type = RegionTypes.Country;

        this.countryId = key;
        this.countryPath = path;
        this.countryName = name;
    }

    @Transient
    public boolean isValid() {
        return path != null && path.length() > 1 && StringUtils.isNotBlank(abbr) && (StringUtils.isNotBlank(name));
    }

    public void mergeWith(RegionIndex otherCountryBean) {
        if (otherCountryBean instanceof CountryBean) {
            getCountryHandle().mergeWith(((CountryBean) otherCountryBean).getCountryHandle());
        }
    }

}
