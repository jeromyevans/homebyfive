package com.blueskyminds.homebyfive.framework.framework.hierarchy;

import com.blueskyminds.homebyfive.framework.framework.hierarchy.AddChildPolicy;

/**
 * Provides a default implementation of an AddChildPolicy
 *
 * The policy allows any non-null child to be added to the hierarchy
 *
 * Date Started: 29/04/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class DefaultAddChildPolicy<T> implements AddChildPolicy<T> {

    // ------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns true of the specified child is allowed to be added to the parent
     * The default policy is that a non-null child can be added to a non-null parent
     *
     * @param child
     * @param child
     * @return true if the child is allowed
     */
    public boolean childPermitted(T parent, T child) {
        return (child != null) && (parent != null);
    }

    // ------------------------------------------------------------------------------------------------------
}
