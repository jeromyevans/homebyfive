package com.blueskyminds.enterprise.region.country;

import com.blueskyminds.enterprise.region.DefaultRegionImpl;
import com.blueskyminds.enterprise.region.Region;
import com.blueskyminds.enterprise.region.*;
import com.blueskyminds.enterprise.region.RegionMixin;
import com.blueskyminds.enterprise.region.state.StateHandle;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.PrintStream;

/**
 * Represents a Country
 * A country is a Region.  It's children are Suburbs and PostCodes
 *
 * Date Started: 7/07/2007
 * <p/>
 * History:
 */
@Entity
public class Country extends DefaultRegionImpl implements Region, CountryI {

    public static final String AUS = "AUS";

    /** The ISO-3166-1 Alpha 2-digit code */
    private String iso2CountryCode;

    /** The ISO-3166-1 Alpha 3-digit code */
    private String iso3CountryCode;

    /** The default currency code */
    private String currencyCode;

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a new instance of a Country
     *
     * Use the factory to create new instances as a CountryHandle instance is also required
     *
     * @param name
     * @param iso2CountryCode
     * @param iso3CountryCode
     * @param currencyCode
     */
    protected Country(String name, String iso2CountryCode, String iso3CountryCode, String currencyCode) {
        this.iso2CountryCode = iso2CountryCode;
        this.iso3CountryCode = iso3CountryCode;
        this.currencyCode = currencyCode;

        //this.regionXMixin = new RegionMixin(new CountryHandle(name, this, iso2CountryCode, iso3CountryCode), RegionTypes.Country);
        this.regionXMixin = new RegionMixin(RegionTypes.Country);
        this.regionXMixin.setName(name);
    }

    /**
     * Create a new instance of a Country
     *
     * Use the factory to create new instances as a CountryHandle instance is also required
     *
     * @param name 
     */
    protected Country(String name) {
        //this.regionXMixin = new RegionMixin(new CountryHandle(name, this), RegionTypes.Country);
        this.regionXMixin = new RegionMixin(RegionTypes.Country);
        this.regionXMixin.setName(name);
    }

    /** Default constructor for ORM */
    protected Country() {
        this.regionXMixin = new RegionMixin();
    }

    // ------------------------------------------------------------------------------------------------------

    /** Initialise the country with default attributes */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    @Basic
    @Column(name="ISO2DigitCountryCode")
    public String getIso2CountryCode() {
        return iso2CountryCode;
    }

    public void setIso2CountryCode(String iso2CountryCode) {
        this.iso2CountryCode = iso2CountryCode;
    }

    // ------------------------------------------------------------------------------------------------------

    @Basic
    @Column(name="ISO3DigitCountryCode")
    public String getIso3CountryCode() {
        return iso3CountryCode;
    }

    public void setIso3CountryCode(String iso3CountryCode) {
        this.iso3CountryCode = iso3CountryCode;
    }

    // ------------------------------------------------------------------------------------------------------

    @Basic
    @Column(name="CurrencyCode")
    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    // ------------------------------------------------------------------------------------------------------

    @Transient
    public String getIdentityName() {
        return super.getIdentityName()+" ("+getName()+")";
    }

    // ------------------------------------------------------------------------------------------------------

    public void print(PrintStream out) {
        out.println(getIdentityName()+" ("+getName()+")");
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


}
