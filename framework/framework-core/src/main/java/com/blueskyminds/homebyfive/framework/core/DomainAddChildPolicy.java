package com.blueskyminds.homebyfive.framework.core;

import com.blueskyminds.homebyfive.framework.core.hierarchy.AddChildPolicy;

/**
 * Provides a default implementation of an AddChildPolicy for the HierarchicalDomainObject's
 *
 * The policy allows any non-null child to be added to the hierarchy provided it is a DomainObject
 *
 * Date Started: 19/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class DomainAddChildPolicy<T> implements AddChildPolicy<T> {

    // ------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns true of the specified child is allowed to be added to the parent
     * The default policy is that a non-null child can be added to a non-null parent, provided it is
     * a DomainObject too
     *
     * @param child
     * @param child
     * @return true if the child is allowed
     */
    public boolean childPermitted(T parent, T child) {
        if ((child != null) && (parent != null)) {
            return child instanceof DomainObject;
        }
        else {
            return false;
        }
    }

    // ------------------------------------------------------------------------------------------------------
}

