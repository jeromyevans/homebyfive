package com.blueskyminds.homebyfive.business.region.graph;

import com.blueskyminds.homebyfive.business.region.graph.Region;
import com.blueskyminds.homebyfive.business.region.graph.State;
import com.blueskyminds.homebyfive.business.region.RegionTypes;
import com.blueskyminds.homebyfive.business.region.PathHelper;
import com.blueskyminds.homebyfive.business.region.index.PostalCodeBean;
import com.blueskyminds.homebyfive.business.tools.KeyGenerator;

import javax.persistence.*;

import org.jboss.envers.Versioned;

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

    public PostalCode(String name) {
        super(name, RegionTypes.PostCode);
        populateAttributes();
    }

    public PostalCode(State state, String name) {
        super(name, RegionTypes.PostCode);
        addParentRegion(state);
        populateAttributes();
    }

    /** Used to edit a new postalcode */
    public PostalCode(State state) {
        super("", RegionTypes.PostCode);
        addParentRegion(state);
        populateAttributes();
    }

     public PostalCode(String statePath, String name) {
        super(name, RegionTypes.PostCode);
        this.parentPath = statePath;
        populateAttributes();
    }

    public PostalCode() {
    }
    
    /**
     * Gets the parent StateHandle
     * Deproxies the instance if necessary
     *
     * @return
     */
    @Transient
    public State getState() {
        Region parent = getParent(RegionTypes.State);
        if (parent != null) {
            return (State) parent.unproxy().getModel();
        } else {
            return null;
        }
    }

    /**
     * Set the state for this postalcode. If a state is already set, the current state is removed
     * @param state
     */
    public void setState(State state) {
        State existing = getState();

        if (existing == null) {
            addParentRegion(state);
        } else {
            if (existing != state) {
                // remove old, add new
                removeParentRegion(existing);
                addParentRegion(state);
            }
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
}
