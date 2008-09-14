package com.blueskyminds.framework.hierarchy;

/**
 * Interface to a policy that determines if a child can be added to a hierarchy
 *
 * Date Started: 29/04/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public interface AddChildPolicy<T> {

    /**
     * Returns true of the specified child is allowed
     * @param parent that the child is being added to
     * @param child to add
     * @return true if the child is allowed to be added to the parent
     */
    boolean childPermitted(T parent, T child);
}
