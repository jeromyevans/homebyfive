package com.blueskyminds.landmine.core.property;

import com.blueskyminds.framework.DomainObject;
import com.blueskyminds.framework.DomainObjectList;

import java.util.Date;

/**
 *
 * Manages a specified feature for a property
 *
 * Date Started: 9/06/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class FeatureList<T extends DomainObject> extends DomainObjectList<FeatureListEntry> {

    public FeatureList() {
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the FeatureList with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    public T addFeature(T feature, Date dateApplied) {
        FeatureListEntry<T> entry = new FeatureListEntry<T>(this, feature, dateApplied);
        entry = super.create(entry);
        if (entry != null) {
            return entry.getUnderlying();
        } else {
            return null;
        }
    }

    // ------------------------------------------------------------------------------------------------------

}
