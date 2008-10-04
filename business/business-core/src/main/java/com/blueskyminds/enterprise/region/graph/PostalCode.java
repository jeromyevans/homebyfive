package com.blueskyminds.enterprise.region.graph;

import com.blueskyminds.enterprise.region.graph.Region;
import com.blueskyminds.enterprise.region.graph.State;
import com.blueskyminds.enterprise.region.RegionTypes;

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
}
