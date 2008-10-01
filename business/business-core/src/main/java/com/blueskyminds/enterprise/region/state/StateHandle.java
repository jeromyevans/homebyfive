package com.blueskyminds.enterprise.region.state;

import com.blueskyminds.enterprise.region.RegionTypes;
import com.blueskyminds.enterprise.region.RegionHandle;
import com.blueskyminds.enterprise.region.Region;
import com.blueskyminds.enterprise.region.country.CountryHandle;
import com.blueskyminds.enterprise.region.postcode.PostCodeHandle;
import com.blueskyminds.enterprise.region.suburb.SuburbHandle;
import com.blueskyminds.homebyfive.framework.framework.DomainObjectStatus;

import javax.persistence.*;

/**
 * Date Started: 7/07/2007
 * <p/>
 * History:
 */
@Entity
@DiscriminatorValue("S")
public class StateHandle extends RegionHandle implements StateI {

    /** A special case StateHandle instance used to indentify an invalid State rather than a null value */
    public static final StateHandle INVALID = invalid();

    private State state;

    protected StateHandle(String name, State state, String abbreviation) {
        super(name, RegionTypes.State, abbreviation);
        this.state = state;
    }

    protected StateHandle() {
    }

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="StateId")
    public State getState() {
        state.setRegionHandle(this);
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Transient
    protected Region getRegionTarget() {
        state.setRegionHandle(this);
        return state;
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

    /**
     * Gets the parent CountryHandle
     * Deproxies the instance if necessary
     *
     * @return
     */
    @Transient
    public CountryHandle getCountry() {
        RegionHandle parent = getParent(RegionTypes.Country);
        if (parent != null) {
            return (CountryHandle) parent.unproxy().getModel();
        } else {
            return null;
        }
    }

    /** Get the abbreviation from the implementation */
    @Transient
    public String getAbbr() {
        return state.getAbbreviation();
    }

     private static StateHandle invalid() {
        StateHandle invalid = new StateHandle();
        invalid.setName("INVALID");
        invalid.setStatus(DomainObjectStatus.Deleted);
        return invalid;
    }

    @Transient
    public boolean isInvalid() {
        return this.equals(INVALID);
    }
}
