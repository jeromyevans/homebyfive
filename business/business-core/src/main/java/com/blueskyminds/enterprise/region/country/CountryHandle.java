package com.blueskyminds.enterprise.region.country;

import com.blueskyminds.enterprise.region.RegionHandle;
import com.blueskyminds.enterprise.region.state.StateHandle;
import com.blueskyminds.enterprise.region.RegionTypes;

import javax.persistence.*;

/**
 * A RegionHandle for a Country
 *
 * Date Started: 7/07/2007
 * <p/>
 * History:
 */
@Entity
@DiscriminatorValue("C")
public class CountryHandle extends RegionHandle implements CountryI {

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
    public CountryHandle(String name) {
        super(name, RegionTypes.Country);
    }

    /**
     * Create a new CountryHandle pointing to the Country implementation
     * Use the factory to create new instances
     */
    public CountryHandle(String name, String... aliases) {
        super(name, RegionTypes.Country, aliases);
    }

    protected CountryHandle() {
    }

    /**
     * Add the specified state to this country
     *
     * @param state
     */
    public StateHandle addState(StateHandle state) {
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
        return getAbbreviation();
    }

    public void setIso2CountryCode(String iso2CountryCode) {
        setAbbreviation(iso2CountryCode);
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
}
