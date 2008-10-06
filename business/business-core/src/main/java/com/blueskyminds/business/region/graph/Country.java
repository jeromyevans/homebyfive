package com.blueskyminds.business.region.graph;

import com.blueskyminds.business.region.graph.Region;
import com.blueskyminds.business.region.graph.State;
import com.blueskyminds.business.region.RegionTypes;
import com.blueskyminds.business.region.PathHelper;
import com.blueskyminds.business.tools.KeyGenerator;

import javax.persistence.*;

import org.apache.commons.lang.StringUtils;

/**
 * A RegionHandle for a Country
 *
 * Date Started: 7/07/2007
 * <p/>
 * History:
 */
@Entity
@DiscriminatorValue("C")
public class Country extends Region {

    public static final String AUS = "AUS";

    /** The ISO-3166-1 Alpha 3-digit code */
    private String iso3CountryCode;

    /** The default currency code */
    private String currencyCode;

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

    @Basic
    @Column(name="ISO3DigitCountryCode")
    public String getIso3CountryCode() {
        return iso3CountryCode;
    }

    public void setIso3CountryCode(String iso3CountryCode) {
        this.iso3CountryCode = iso3CountryCode;
    }

    @Basic
    @Column(name="CurrencyCode")
    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    /**
     * Populates the generated/read-only properties
     */
    public void populateAttributes() {
        this.key = KeyGenerator.generateId(abbr);
        this.parentPath = PathHelper.ROOT;
        this.path = PathHelper.joinPath(parentPath, key);
    }

    @Transient
    public boolean isValid() {
        return path != null && path.length() > 1 && StringUtils.isNotBlank(abbr) && (StringUtils.isNotBlank(name));
    }

}
