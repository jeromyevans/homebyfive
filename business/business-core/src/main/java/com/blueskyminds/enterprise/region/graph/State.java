package com.blueskyminds.enterprise.region.graph;

import com.blueskyminds.enterprise.region.RegionTypes;
import com.blueskyminds.enterprise.region.PathHelper;
import com.blueskyminds.enterprise.region.graph.Region;
import com.blueskyminds.enterprise.region.graph.Country;
import com.blueskyminds.enterprise.region.graph.PostalCode;
import com.blueskyminds.enterprise.region.graph.Suburb;
import com.blueskyminds.enterprise.tools.KeyGenerator;
import com.blueskyminds.homebyfive.framework.core.DomainObjectStatus;

import javax.persistence.*;

/**
 * Date Started: 7/07/2007
 * <p/>
 * History:
 */
@Entity
@DiscriminatorValue("S")
public class State extends Region {

    /** A special case StateHandle instance used to identify an invalid State rather than a null value */
    public static final State INVALID = invalid();

    public State(String name, String abbreviation) {
        super(name, RegionTypes.State);
        this.abbr = abbreviation;
        addAlias(abbreviation);
        populateAttributes();
    }

    public State(Country country, String name, String abbreviation) {
        super(name, RegionTypes.State);
        this.abbr = abbreviation;
        addAlias(abbreviation);
        this.addParentRegion(country); 
        populateAttributes();
    }

    protected State() {
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
     * Gets the parent CountryHandle
     * Deproxies the instance if necessary
     *
     * @return
     */
    @Transient
    public Country getCountry() {
        Region parent = getParent(RegionTypes.Country);
        if (parent != null) {
            return (Country) parent.unproxy().getModel();
        } else {
            return null;
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
            this.path = PathHelper.joinPath(parentPath, key);
        }

    }

    @Transient
    public boolean isInvalid() {
        return this.equals(INVALID);
    }

}
