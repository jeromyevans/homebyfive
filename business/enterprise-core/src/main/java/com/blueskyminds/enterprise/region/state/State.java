package com.blueskyminds.enterprise.region.state;

import com.blueskyminds.enterprise.region.Region;
import com.blueskyminds.enterprise.region.DefaultRegionImpl;
import com.blueskyminds.enterprise.region.RegionMixin;
import com.blueskyminds.enterprise.region.suburb.SuburbHandle;
import com.blueskyminds.enterprise.region.postcode.PostCodeHandle;
import com.blueskyminds.enterprise.region.RegionTypes;

import javax.persistence.*;

/**
 * A state or territory within a country
 *
 * All States have a name, an abbreviation and a type
 * A state always belongs to a single Country.
 *
 * The State class provides behaviour to encapsulate the underlying Subject and Associations.
 *
 * User: Jeromy
 * Date: 16/04/2006
 * Time: 16:54:56
 *
 *
 * Date Started: 7/07/2007
 * <p/>
 * History:
 */
@Entity
public class State extends DefaultRegionImpl implements Region, StateI {

    private String abbreviation;

    protected State(String fullName, String abbreviation) {
        this.abbreviation = abbreviation;

        //this.regionXMixin = new RegionMixin(new StateHandle(fullName, this, abbreviation), RegionTypes.State);
        this.regionXMixin = new RegionMixin(RegionTypes.State);
        this.regionXMixin.setName(fullName);
    }

    protected State() {
        this.regionXMixin = new RegionMixin();
    }

    @Basic
    @Column(name="Abbr")
    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    /**
     * Add a postcode to this state
     *
     * @param postCode
     * @return
     */
    public PostCodeHandle addPostCode(PostCodeHandle postCode) {
        addChildRegion(postCode);
        return postCode;
    }

    /**
     * Add a suburb to this state
     *
     * @param suburb
     * @return
     */
    public SuburbHandle addSuburb(SuburbHandle suburb) {
        addChildRegion(suburb);
        return suburb;
    }
}
