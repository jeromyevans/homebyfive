package com.blueskyminds.homebyfive.framework.framework.hierarchy;

import com.blueskyminds.homebyfive.framework.framework.hierarchy.AddChildPolicy;

import java.util.Set;

/**
 * A generalised hierarchy schema
 *
 * The implementation has a parent and collection of children
 *
 * Date Started: 29/04/2006
 * <p/>
 * History:
 * <p/>
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public interface Hierarchy<T> {

    /**
     * Sets the parent of this node
     * @param parent of this node
     */
    void setParent(T parent);

    /**
     * Gets the parent of this node
     * @return parent
     */
    T getParent();

    /**
     * Adds a child to this node
     * @param child to add
     * @return true if the child was added as a new node.  False is the child already exists, child is null,
     *  or policy states that the child is not permitted.
     */
    boolean addChild(T child);

    /**
     * Get the children of this node
     * @return the collection of children of this node.
     */
    Set<T> getChildren();

    /**
     * Defines the policy that determines if a child can be added to this hierarchy
     * @param policy
     */
    void setAddChildStrategy(AddChildPolicy policy);

    /**
     * Returns true if this node has the specified decendant
     * @return true if the specified node is a decendant
     */
    boolean hasDescendant(T node);

    /**
     * Returns true if this node has the specified ancestor
     * @return true if this node has the specified ancestor
     */
    boolean hasAncestor(T node);

    /**
     * Returns true if this node has the specified child
     * @param child to check
     * @return true if the node is a child of this node
     */
    boolean hasChild(T child);

    /**
     * Returns true if this node has the specified parent
     * @param parent node to check
     * @return true if this node has the specified parent
     */
    boolean hasParent(T parent);

    /**
     * Returns true if this node is a sibling of the specified node (share the same parent)
     * @param node
     * @return true if this node is a sibling of the specified node
     */
    boolean isSibling(T node);

    /**
     * Returns true if this node is a child of the specified parent
     * @param parent
     * @return true if this node is a child of the specified parent
     */
    boolean isChild(T parent);

    /**
     * Returns true if this node is the parent of the specified child
     * @param child
     * @return true if this node is the parent of the specified child
     */
    boolean isParent(T child);

    /**
     * Returns true if this node is a decendant of the specified node
     * @param node
     * @return true if this node is a decendant of the specified node
     */
    boolean isDescendant(T node);

    /**
     * Returns true if this node is an ancestor of the specified node
     * @param node
     * @return true if this node is an ancestor of the specified node
     */
    boolean isAncestor(T node);

    /**
     * Returns true if this node is a decendant of the specified node or IS the specified node
     * @param node
     * @return true if this node is a decendant of the specified node or IS the specified node
     */
    boolean isDecendantOrEqual(T node);

    /**
     * Returns true if this node is an ancestor of the specified node or IS the specified node
     * @param node
     * @return true if this node is an ancestor of the specified node or IS the specified node
     */
    boolean isAncestorOrEqual(T node);

    /**
     * Returns the set of ancestors for this node.  The set does not include itself
     *
     * @return set of ancestors, or null if none
     */
    Set<T> getAncestors();

    /**
     * Returns the set of decendants of this node.  The set does not include itself
     *
     * @return set of decendants, or null if none
     */
    Set<T> getDecendants();

}
