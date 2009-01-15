package com.blueskyminds.homebyfive.business.region.graph;

import com.blueskyminds.homebyfive.business.region.graph.Region;
import com.blueskyminds.homebyfive.business.region.graph.State;
import com.blueskyminds.homebyfive.business.region.RegionTypes;
import com.blueskyminds.homebyfive.business.region.PathHelper;
import com.blueskyminds.homebyfive.business.region.index.PostalCodeBean;
import com.blueskyminds.homebyfive.business.tools.KeyGenerator;

import javax.persistence.*;

import org.jboss.envers.Versioned;

import java.util.List;
import java.util.Arrays;

/**
 * RegionHandle for a postcode
 *
 * Date Started: 7/07/2007
 * <p/>
 * History:
 */
@Entity
@DiscriminatorValue("P")
@Versioned
public class PostalCode extends Region {

    private State state;

    public PostalCode(String name) {
        super(name, RegionTypes.PostCode);
        populateAttributes();
    }

    public PostalCode(State state, String name) {
        super(name, RegionTypes.PostCode);
        setState(state);
        populateAttributes();
        if (state != null) {
            state.addPostCode(this);
        }
    }

    /** Used to edit a new postalcode */
    public PostalCode(State state) {
        super("", RegionTypes.PostCode);
        setState(state);
        populateAttributes();
        if (state != null) {
            state.addPostCode(this);
        }
    }

     public PostalCode(String statePath, String name) {
        super(name, RegionTypes.PostCode);
        this.parentPath = statePath;
        populateAttributes();
    }

    public PostalCode() {
    }

    @Override
    @Transient
    public State getParent() {
        return state;
    }

    public void setParent(Region region) {
        this.state = (State) region;
    }

    /**
     * Gets the parent StateHandle
     *
     * @return
     */
    @ManyToOne
    @JoinColumn(name="PostalCodeStateId")
    public State getState() {
        return state;
    }

    /**
     * Set the state for this postalcode. If a state is already set, the current state is removed
     * @param state
     */
    public void setState(State state) {
        this.state = state;
        if (state != null) {
            this.parentPath = state.getPath();
        } else {
            this.parentPath = null;
        }
    }

    /**
     * Populates the generated/read-only properties
     */
    public void populateAttributes() {
        this.key = KeyGenerator.generateId(name);
        State state = getState();
        if (state != null) {
            this.parentPath = state.getPath();
        }
        this.path = PathHelper.joinPath(parentPath, key);
    }

     /**
     * Create or update the denormalized index entity
     */
    @PrePersist
    protected void prePersist() {
        super.prePersist();
        if (regionIndex == null) {
             regionIndex = new PostalCodeBean(this);
        } else {
            // update the attribute of the index
            regionIndex.populateDenormalizedAttributes();
        }
    }

    public Suburb addSuburb(Suburb suburb) {
        addChildRegion(suburb);
        return suburb;
    }
}
