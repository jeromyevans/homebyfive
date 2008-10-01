package com.blueskyminds.homebyfive.framework.core.hierarchy;

import java.util.Set;

/**
 *
 * An interface to a multiple-parent, multiple-child hierarchy
 *
 * Date Started: 2/09/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public interface ManyParentsHierarchy<T> extends Hierarchy<T> {

    /**
     * The getParent operation is not supported for a hierarchy with many parents - there is no
     *  concept of a first or primary parent
     * @return null
     */
    T getParent();

    /**
     * Gets the parents of this node
     * @return parents
     */
    Set<T> getParents();

      /**
     * Adds a parent to this node
     * @param parent to add
     * @return true if the parent was added as a new node.  False is the parent already exists, parent is null,
     *  or policy states that the parent is not permitted.
     */
    boolean addParent(T parent);

    /**
     * Get the children of this node
     * @return the collection of children of this node.
     */
    Set<T> getChildren();

    /**
     * Defines the policy that determines if a parent can be added to this hierarchy
     * @param policy
     */
    void setAddParentStrategy(AddChildPolicy policy);

}
