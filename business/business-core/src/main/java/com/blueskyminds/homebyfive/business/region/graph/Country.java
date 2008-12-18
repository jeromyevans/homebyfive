package com.blueskyminds.homebyfive.business.region.graph;

import com.blueskyminds.homebyfive.business.region.graph.Region;
import com.blueskyminds.homebyfive.business.region.graph.State;
import com.blueskyminds.homebyfive.business.region.RegionTypes;
import com.blueskyminds.homebyfive.business.region.PathHelper;
import com.blueskyminds.homebyfive.business.region.index.CountryBean;
import com.blueskyminds.homebyfive.business.tools.KeyGenerator;

import javax.persistence.*;

import org.apache.commons.lang.StringUtils;
import org.jboss.envers.Versioned;

import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.util.List;

/**
 * A RegionHandle for a Country
 *
 * Date Started: 7/07/2007
 * <p/>
 * History:
 */
@Entity
@DiscriminatorValue("C")
@Versioned
public class Country extends Region {    

    /**
     * Create a new CountryHandle pointing to the Country implementation
     *
     * Use the factory to create new instances
     *
     * @param name
     */
    public Country(String name, String abbr) {
        super(name, RegionTypes.Country);
        this.abbr = abbr;
        addAlias(abbr);
        populateAttributes();
    }

    /**
     * Create a new CountryHandle pointing to the Country implementation
     * Use the factory to create new instances
     */
    public Country(String name, String abbr, String... aliases) {
        super(name, RegionTypes.Country, aliases);
        this.abbr = abbr;
        addAlias(abbr);
        populateAttributes();
    }

    public Country() {
        this.type = RegionTypes.Country;
    }

    @Override
    @Transient
    public Region getParent() {
        return null;
    }

    public void setParent(Region region) {
    }

    /**
     * Add the specified state to this country
     *
     * @param state
     */
    public State addState(State state) {
        addChildRegion(state);
        return state;
    }

    /**
     * Get the 2 digit ISO code from the implementation
     * 
     * @return
     */
    @Transient
    public String getIso2CountryCode() {
        return getAbbr();
    }

    public void setIso2CountryCode(String iso2CountryCode) {
        setAbbr(iso2CountryCode);
    }

    /**
     * Populates the generated/read-only properties
     */
    public void populateAttributes() {
        this.key = KeyGenerator.generateId(abbr);
        this.parentPath = PathHelper.ROOT;
        this.path = PathHelper.joinPath(parentPath, key);
    }

    public void mergeWith(Region anotherRegion) {
        super.mergeWith(anotherRegion);
    }

    @Transient
    public boolean isValid() {
        return path != null && path.length() > 1 && StringUtils.isNotBlank(abbr) && (StringUtils.isNotBlank(name));
    }

    /**
     * Create or update the denormalized index entity
     */
     @PrePersist
    protected void prePersist() {
        super.prePersist();
        if (regionIndex == null) {
             regionIndex = new CountryBean(this);            
        } else {
            // update the attribute of the index
            regionIndex.populateDenormalizedAttributes();
        }
    }


}
