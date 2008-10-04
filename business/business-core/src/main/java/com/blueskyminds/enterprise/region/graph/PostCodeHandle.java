package com.blueskyminds.enterprise.region.graph;

import com.blueskyminds.enterprise.region.graph.RegionHandle;
import com.blueskyminds.enterprise.region.graph.StateHandle;
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
public class PostCodeHandle extends RegionHandle {

    public PostCodeHandle(String name) {
        super(name, RegionTypes.PostCode);
    }

    protected PostCodeHandle() {
    }
    
    /**
     * Gets the parent StateHandle
     * Deproxies the instance if necessary
     *
     * @return
     */
    @Transient
    public StateHandle getState() {
        RegionHandle parent = getParent(RegionTypes.State);
        if (parent != null) {
            return (StateHandle) parent.unproxy().getModel();
        } else {
            return null;
        }
    }
}
