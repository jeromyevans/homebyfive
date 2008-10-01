package com.blueskyminds.homebyfive.framework.core.hierarchy;

import java.util.Collection;
import java.util.Set;
import java.util.HashSet;

/**
 * Implementation of a Hierarchy
 *
 * T should be set to the Class of the implementor.  This will ensure all methods operate on the class of the
 * implementor instead of needing to typecast to/from Hierarchy.  All of the typecasting to/from Hierarchy
 * can be self-contained within this class
 *
 * Date Started: 29/04/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public abstract class AbstractHierarchy<T> implements Hierarchy<T> {

    /**
     * The parent of this instance
     */
    private T parent;

    /**
     * List of children of this instance
     */
    private Set<T> children;

    /**
     * The policy to use when adding a child to this hierarchy
     */
    private AddChildPolicy addChildPolicy;

    // ------------------------------------------------------------------------------------------------------

    /** Create a new hierarchy with default memento */
    public AbstractHierarchy() {
        init(null);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Create a new hierarchy that uses the policy specified when adding a child
     **/
    public AbstractHierarchy(AddChildPolicy addChildPolicy) {
        init(addChildPolicy);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise a default hierarchy
     * @param addChildPolicy - the policy to use, or null to use the default
     */
    private void init(AddChildPolicy addChildPolicy) {
        parent = null;
        initialiseChildren();
        if (addChildPolicy == null) {
            // use the default policy
            initialiseDefaultPolicy();
        }
        else {
            // use the specified policy
            setAddChildStrategy(addChildPolicy);
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialises the collection of children
     */
    private void initialiseChildren() {
        children = new HashSet<T>();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialises the default policy
     */
    private void initialiseDefaultPolicy() {
        addChildPolicy = new DefaultAddChildPolicy<T>();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Gets the parent of this node
     * @return parent
     */
    public T getParent() {
        return parent;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Sets the parent of this node
     * @param parent of this node
     */
    public void setParent(T parent) {
        this.parent = parent;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the children of this node
     * @return the collection of children of this node.
     */
    public Set<T> getChildren() {
        return children;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Adds a child to this parent.  The child will only be added to this parent if it satisfies the following
     * conditions:
     *  1. The child is not null
     *  2. The child is not already a child of this parent
     *  3. The AddChildPolicy states that it's okay to add this child to this parent
     *
     * If the child implements the Hierarchy interface the child will be updated to point to this parent
     *
     * @param child to add to this parent
     */
    @SuppressWarnings({"unchecked"})
    public boolean addChild(T child) {
        boolean added = false;

        if (children == null) {
            initialiseChildren();
        }
        if (child != null) {
            // if the child doesn't already belong to this parent
            if (!children.contains(child)) {
               // use the policy to determine if the child can be added
                if (addChildPolicy.childPermitted(this, child)) {
                    // attempt to add the child
                    if (children.add(child)) {
                        added = true;
                        if (child instanceof Hierarchy) {
                            try {
                                // update the child to point to this parent
                                ((Hierarchy<T>) child).setParent((T) this);
                            }
                            catch (ClassCastException e) {
                                // ignore this class casting - means the hierarchy is not setup using the
                                // expected pattern
                            }
                        }
                    }
                }
            }
        }
        return added;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Sets the policy to use when adding a child to this hierarchy
     **/
    public void setAddChildStrategy(AddChildPolicy policy) {
        this.addChildPolicy = policy;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns true if this node has a decendant that is the specified node
     * @return true if this node has the specified decendant
     */
    @SuppressWarnings({"unchecked"})
    public boolean hasDescendant(T node) {
        boolean found = false;

        if (node != null) {
            // if this is an ancestor of the node, then the node is a decendant
            if (((Hierarchy<T>) node).hasAncestor((T) this)) {
                found = true;
            }
        }

        return found;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns true if the this node is an ancestor of the specified node
     * @return true if this node is that the specified node as an ancestor
     */
    @SuppressWarnings({"unchecked"})
    public boolean hasAncestor(T node) {
        boolean found = false;

        if ((node != null) && (parent != null)) {
            try {
                // first do a quick check to see if the node is the parent of this node
                if (!node.equals(parent)) {
                    // not the parent, so need to iterate up through the parent hierarchy
                    T currentParent = ((Hierarchy<T>) parent).getParent();
                    while ((currentParent != null) && (!found)) {
                        if (currentParent.equals(node)) {
                            found = true;
                        }
                        else {
                            // not found, try the next parent up
                            currentParent = ((Hierarchy<T>) currentParent).getParent();
                        }
                    }
                }
                else {
                    // the node is the parent of this node
                    found = true;
                }
            }
            catch (ClassCastException e) {
                // ignore this class casting - means the hierarchy is not setup using the
                // expected pattern
            }
        }

        return found;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns true if this node has the specified child
     * @param child to check
     * @return true if the node is a child of this node
     */
    public boolean hasChild(T child) {
        return children.contains(child);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns true if this node has the specified parent
     * @param parent node to check
     * @return true if this node is the parent of the child node
     */
    public boolean hasParent(T parent) {
        return parent.equals(parent);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns true if this node is a sibling of the specified node (they share the same parent)
     * @param node
     * @return true if the specified node is a sibling
     */
    @SuppressWarnings({"unchecked"})
    public boolean isSibling(T node) {
        boolean sibling = false;

        if (node != null) {
            try {
                if (((Hierarchy<T>) node).getParent().equals(parent)) {
                    sibling = true;
                }
            }
            catch (ClassCastException e) {
                // ignore this class casting - means the hierarchy is not setup using the
                // expected pattern
            }
        }
        return sibling;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns true if this node is a child of the specified parent
     * @param parent
     * @return true if this node is a child of the specified parent
     */
    @SuppressWarnings({"unchecked"})
    public boolean isChild(T parent)  {
        if (parent != null) {
            return ((Hierarchy<T>) parent).hasChild((T) this);
        }
        else
            return false;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns true if this node is the parent of the specified child
     * @param child
     * @return true if this node is the parent of the specified child
     */
    @SuppressWarnings({"unchecked"})
    public boolean isParent(T child) {
        if (child != null) {
            return ((Hierarchy<T>) child).hasParent((T) this);
        }
        else
            return false;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns true if this node is a decendant of the specified node
     * @param node
     * @return true if this node is a decendant of the specified node
     */
    @SuppressWarnings({"unchecked"})
    public boolean isDescendant(T node)  {
        if (node != null) {
            return hasAncestor(node);
        }
        else {
            return false;
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns true if this node is an ancestor of the specified node
     * @param node
     * @return true if this node is an ancestor of the specified node
     */
    @SuppressWarnings({"unchecked"})
    public boolean isAncestor(T node) {
        if (node != null) {
            return hasDescendant(node);
        }
        else {
            return false;
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns true if this node is a decendant of the specified node or IS the specified node
     * @param node
     * @return true if this node is a decendant of the specified node or IS the specified node
     */
    public boolean isDecendantOrEqual(T node) {
        if (this.equals(node)) {
            return true;
        }
        else {
            return isDescendant(node);
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns true if this node is an ancestor of the specified node or IS the specified node
     * @param node
     * @return true if this node is an ancestor of the specified node or IS the specified node
     */
    public boolean isAncestorOrEqual(T node) {
        if (this.equals(node)) {
            return true;
        }
        else {
            return isAncestor(node);
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns the set of ancestors for this node.  The set does not include itself
     *
     * @return set of ancestors, or null if none
     */
    @SuppressWarnings({"unchecked"})
    public Set<T> getAncestors() {
        Set<T> ancestors = new HashSet<T>();

        if (parent != null) {
            try {
                ancestors.add(parent);
                // iterate up through the parent hierarchy
                T currentParent = ((Hierarchy<T>) parent).getParent();
                while (currentParent != null) {
                    ancestors.add(currentParent);

                    // try the next parent up
                    currentParent = ((Hierarchy<T>) currentParent).getParent();
                }
            }
            catch (ClassCastException e) {
                // ignore this class casting - means the hierarchy is not setup using the
                // expected pattern
            }
        }

        if (ancestors.size() > 0) {
            return ancestors;
        } else {
            return null;
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Visits all of the children of this node and add them to the decendants set.
     * Recurses to visit all of the children's children, and so on.
     * @param children
     * @param decendants set (gets updated)
     */
    @SuppressWarnings({"unchecked"})
    protected void visitDecendants(Collection<T> children, Set<T> decendants) {
        // need to iterate down through the children hierarchy
        if (children != null) {
            decendants.addAll(children);

            // begin visting all of the children's children
            for (T child : children) {
                visitDecendants(((ManyParentsHierarchy<T>) child).getChildren(), decendants);
            }

        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns the set of decendants of this node.  The set does not include itself
     *
     * @return set of decendants, or null if none
     */
    @SuppressWarnings({"unchecked"})
    public Set<T> getDecendants() {
        Set<T> decendants = new HashSet<T>();

        if (getChildren() != null) {
            try {
                visitDecendants(getChildren(), decendants);
            }
            catch (ClassCastException e) {
                // ignore this class casting - means the hierarchy is not setup using the
                // expected pattern
            }
        }

        if (decendants.size() > 0) {
            return decendants;
        } else {
            return null;
        }
    }

}
