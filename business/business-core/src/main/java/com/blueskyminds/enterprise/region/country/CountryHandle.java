package com.blueskyminds.enterprise.region.country;

import com.blueskyminds.enterprise.region.country.Country;
import com.blueskyminds.enterprise.region.RegionHandle;
import com.blueskyminds.enterprise.region.Region;
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

    private Country country;

    /**
     * Create a new CountryHandle pointing to the Country implementation
     *
     * Use the factory to create new instances
     *
     * @param name
     */
    protected CountryHandle(String name, Country country) {
        super(name, RegionTypes.Country);
        this.country = country;
    }

    /**
     * Create a new CountryHandle pointing to the Country implementation
     * Use the factory to create new instances
     */
    protected CountryHandle(String name, Country country, String... aliases) {
        super(name, RegionTypes.Country, aliases);
        this.country = country;
    }

    protected CountryHandle() {
    }

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="CountryId")
    public Country getCountry() {
        country.setRegionHandle(this);
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Transient
    protected Region getRegionTarget() {
        country.setRegionHandle(this);        
        return country;
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
    public String getISO2DigitCode() {
        return country.getIso2CountryCode();
    }
}
