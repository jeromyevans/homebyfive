package com.blueskyminds.enterprise.region.graph;

import com.blueskyminds.enterprise.region.graph.Region;
import com.blueskyminds.enterprise.region.graph.State;
import com.blueskyminds.enterprise.region.RegionTypes;
import com.blueskyminds.enterprise.region.PathHelper;
import com.blueskyminds.enterprise.tools.KeyGenerator;

import javax.persistence.*;

/**
 * RegionHandle for a postcode
 *
 * Date Started: 7/07/2007
 * <p/>
 * History:
 */
@Entity
@DiscriminatorValue("P")
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
    
    protected PostalCode() {
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
     * Populates the generated/read-only properties
     */
    public void populateAttributes() {
        this.key = KeyGenerator.generateId(name);
        State state = getState();
        if (state != null) {
            this.parentPath = state.getPath();
            this.path = PathHelper.joinPath(parentPath, key);
        }
    }

}
