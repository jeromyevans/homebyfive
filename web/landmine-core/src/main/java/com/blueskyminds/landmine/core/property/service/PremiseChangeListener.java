package com.blueskyminds.landmine.core.property.service;

import com.blueskyminds.landmine.core.property.Premise;

/**
 * Listeners are notified when a change is committed to a premise
 *
 * Date Started: 30/04/2008
 */
public interface PremiseChangeListener {

    /**
     * Notified after a change is committed to a premise
     *
     * @param premise
     */
    void onChange(Premise premise);
}
