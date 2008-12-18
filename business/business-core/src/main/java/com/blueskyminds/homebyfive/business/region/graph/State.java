package com.blueskyminds.homebyfive.business.region.graph;

import com.blueskyminds.homebyfive.business.region.RegionTypes;
import com.blueskyminds.homebyfive.business.region.PathHelper;
import com.blueskyminds.homebyfive.business.region.index.StateBean;
import com.blueskyminds.homebyfive.business.region.graph.Region;
import com.blueskyminds.homebyfive.business.region.graph.Country;
import com.blueskyminds.homebyfive.business.region.graph.PostalCode;
import com.blueskyminds.homebyfive.business.region.graph.Suburb;
import com.blueskyminds.homebyfive.business.tools.KeyGenerator;
import com.blueskyminds.homebyfive.framework.core.DomainObjectStatus;

import javax.persistence.*;

import org.jboss.envers.Versioned;

import java.util.List;
import java.util.Arrays;

/**
 * Date Started: 7/07/2007
 * <p/>
 * History:
 */
@Entity
@DiscriminatorValue("S")
@Versioned
public class State extends Region {

    /** A special case StateHandle instance used to identify an invalid State rather than a null value */
    public static final State INVALID = invalid();

    private Country country;
    private StateType stateType;
    
    public State(String name, String abbreviation) {
        super(name, RegionTypes.State);
        this.abbr = abbreviation;
        this.stateType = StateType.State;
        addAlias(abbreviation);
        populateAttributes();
    }

    public State(Country country, String name, String abbreviation) {
        super(name, RegionTypes.State);
        this.abbr = abbreviation;
        this.stateType = StateType.State;
        addAlias(abbreviation);
        setCountry(country);
        populateAttributes();
    }

     public State(Country country, String name, String abbreviation, StateType stateType) {
        super(name, RegionTypes.State);
        this.abbr = abbreviation;
        this.stateType = stateType;
        addAlias(abbreviation);
        this.parentPath = country.getPath();
        populateAttributes();
    }

    public State(String countryPath, String name, String abbreviation, StateType stateType) {
       super(name, RegionTypes.State);
       this.abbr = abbreviation;
       this.stateType = stateType;
       addAlias(abbreviation);
       this.parentPath = countryPath;
       populateAttributes();
   }

    /** Used for editing a new state */
    public State(Country country) {
        super("", RegionTypes.State);
        this.parentPath = country.getPath();
        this.stateType = StateType.State;
        populateAttributes();
    }

    public State() {
        this.type = RegionTypes.State;
    }

    @Override
    @Transient
    public Region getParent() {
        return country;
    }

    public void setParent(Region region) {
        this.country = (Country) region;
    }

    /**
     * Add a postcode to this state
     *
     * @param postCode
     * @return
     */
    public PostalCode addPostCode(PostalCode postCode) {
        addChildRegion(postCode);
        return postCode;
    }

    /**
     * Add a suburb to this state
     *
     * @param suburb
     * @return
     */
    public Suburb addSuburb(Suburb suburb) {
        addChildRegion(suburb);
        return suburb;
    }

    /**
     * Gets the parent Country     
     *
     * @return
     */
    @ManyToOne
    @JoinColumn(name="CountryId")
    public Country getCountry() {
        return country;
    }

    /**
     * Set the country for this state. If a country is already set, the current country is removed
     * @param country
     */
    public void setCountry(Country country) {
        this.country = country;
        if (country != null) {
            this.parentPath = country.getPath();
        } else {
            parentPath = null;
        }
    }

     private static State invalid() {
        State invalid = new State();
        invalid.setName("INVALID");
        invalid.setStatus(DomainObjectStatus.Deleted);
        return invalid;
    }

    /**
     * Populates the generated/read-only properties
     */
    public void populateAttributes() {
        this.key = KeyGenerator.generateId(abbr);
        Country country = getCountry();
        if (country != null) {
            this.parentPath = country.getPath();
        }
        this.path = PathHelper.joinPath(parentPath, key);
    }

    @Transient
    public boolean isInvalid() {
        return this.equals(INVALID);
    }

    @Enumerated
    @Column(name="StateType")
    public StateType getStateType() {
        return stateType;
    }

    public void setStateType(StateType stateType) {
        this.stateType = stateType;
    }

    /**
     * Create or update the denormalized index entity
     */
    @PrePersist
    protected void prePersist() {
        super.prePersist();
        if (regionIndex == null) {
             regionIndex = new StateBean(this);
        } else {
            // update the attribute of the index
            regionIndex.populateDenormalizedAttributes();
        }
    }
   
}
