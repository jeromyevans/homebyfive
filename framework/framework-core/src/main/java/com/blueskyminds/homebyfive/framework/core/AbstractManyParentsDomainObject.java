package com.blueskyminds.homebyfive.framework.core;

import com.blueskyminds.homebyfive.framework.core.hierarchy.AddChildPolicy;
import com.blueskyminds.homebyfive.framework.core.hierarchy.DefaultAddChildPolicy;
import com.blueskyminds.homebyfive.framework.core.hierarchy.ManyParentsHierarchy;

import java.util.*;

/**
 *
 * An abstract implementation of a DomainObject that implements the ManyParentsHierachy interface.
 *
 * Extends this implementation for domain objects that themselves hierarchical.
 *
 * Date Started: 15/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class AbstractManyParentsDomainObject<T extends DomainObject> extends AbstractHierarchicalDomainObject<T> implements ManyParentsHierarchy<T> {

    /**
     * The parents of this instance
     */
    private Set<T> parents;

    /**
     * The policy to use when adding a parent to this hierarchy
     */
    private AddChildPolicy addParentPolicy;

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /** Create a new hierarchy with default memento */
    public AbstractManyParentsDomainObject() {
        init(null);
    }

     // ------------------------------------------------------------------------------------------------------

    /**
     * Create a new hierarchy that uses the policy specified when adding children and parents
     **/
    public AbstractManyParentsDomainObject(AddChildPolicy addChildPolicy) {
        super(addChildPolicy);
        init(addChildPolicy);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise a default hierarchy
     * @param addParentPolicy - the policy to use, or null to use the default
     */
    private void init(AddChildPolicy addParentPolicy) {
        initialiseParents();
        if (addParentPolicy == null) {
            // use the default policy
            initialiseDefaultPolicy();
        }
        else {
            // use the specified policy
            setAddParentStrategy(addParentPolicy);
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialises the collection of parents
     */
    private void initialiseParents() {
        parents = new HashSet<T>();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialises the default policy
     */
    private void initialiseDefaultPolicy() {
        addParentPolicy = new DefaultAddChildPolicy<T>();
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the parents of this nide
     * @return collection of nodes that are the parents
     */
    public Set<T> getParents() {
        return parents;
    }

    protected void setParents(Set<T> parents) {
        this.parents = parents;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Add the parent to this node
     * @param parent to add to this node
     */
    public void setParent(T parent) {
        addParent(parent);
    }

    /**
     * Not supported - use getParents
     * @return null
     */
    public T getParent() {
        return null;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Adds a parent to this child node.  The parent will only be added to this child if it satisfies the following
     * conditions:
     *  1. The parent is not null
     *  2. The parent is not already a parent of this child
     *  3. The AddParentPolicy states that it's okay to add this parent to this child
     *
     * If the parent implements the ManyParentHierarchy interface the parent will be updated to point to this child
     *
     * @param parent to add to this child
     */
    @SuppressWarnings({"unchecked"})
    public boolean addParent(T parent) {
        boolean added = false;

        if (parents == null) {
            initialiseParents();
        }
        if (parent != null) {
            // use the policy to determine if the child can be added
            if (addParentPolicy.childPermitted(this, parent)) {
                // attempt to add the parent
                if (parents.add(parent)) {
                    added = true;
                    if (parent instanceof ManyParentsHierarchy) {
                        try {
                            // update the parent to point to this child
                            ((ManyParentsHierarchy<T>) parent).addChild((T) this);
                        }
                        catch (ClassCastException e) {
                            // ignore this class casting - means the hierarchy is not setup using the
                            // expected pattern
                        }
                    }
                }
            }
        }
        return added;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Sets the policy to use when adding a parent to this hierarchy
     **/
    public void setAddParentStrategy(AddChildPolicy policy) {
        this.addParentPolicy = policy;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Visits all of the parents.  Returns true if one of the parents equals the specified node
     * @param parents
     * @param node
     * @return true if one of the parents equals the node
     */
    @SuppressWarnings({"unchecked"})
    private boolean searchParents(Set<T> parents, T node) {
        T parent;
        boolean found = false;

        // not the parent, so need to iterate up through the parent hierarchy
        if (parents != null) {
            // check if any of the parents match the node
            for (T currentParent : parents) {
                if (node.equals(currentParent)) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                // not found, so begin visting all of the parent's parents
                Iterator<T> iterator = parents.iterator();
                while ((iterator.hasNext()) && (!found)) {
                    parent = iterator.next();
                    found = searchParents(((ManyParentsHierarchy<T>) parent).getParents(), node);
                }
            }
        }

        return found;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns true if the this node is an ancestor of the specified node
     * @return true if this node is is an ancestor of the specified node
     */
    public boolean hasAncestor(T node) {
        boolean found = false;

        if ((node != null) && (parents != null)) {
            try {
                found = searchParents(parents, node);
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
     * Visits all of the parents of this node and add them to the ancestors set.
     * Recurses to visit all of the parent's parents, and so on.
     * @param parents
     * @param ancestors set (gets updated)
     */
    @SuppressWarnings({"unchecked"})
    protected void visitAncestors(Collection<T> parents, Set<T> ancestors) {
        // need to iterate up through the parent hierarchy
        if (parents != null) {
            ancestors.addAll(parents);

            // begin visting all of the parent's parents
            for (T parent : parents) {
                visitAncestors(((ManyParentsHierarchy<T>) parent).getParents(), ancestors);
            }

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

        if (parents != null) {
            try {
                visitAncestors(parents, ancestors);
            }
            catch (ClassCastException e) {
                // ignore this class casting - means the hierarchy is not setup using the
                // expected pattern
            }
        }

        if (ancestors.size() > 0) {
            return ancestors;
        } else {
            return new HashSet<T>(); // empty set
        }
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns true if this node has the specified parent
     * @param parent node to check
     * @return true if this node has the specified parent
     */
    public boolean hasParent(T parent) {
        if (parents != null) {
            return parents.contains(parent);
        } else {
            return false;
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns true if this node is a sibling of the specified node (they share one of the same
     * parents)
     * @param node
     * @return true if the specified node is a sibling
     */
    @SuppressWarnings({"unchecked"})
    public boolean isSibling(T node) {
        boolean sibling = false;
        Collection<T> theirParents;

        if ((node != null) && (parents != null)) {
            try {
                theirParents = ((ManyParentsHierarchy<T>) node).getParents();
                if (theirParents != null) {
                    // check whether any of our parents are equal to one of their parents
                    for (T parent : parents) {
                        if (theirParents.contains(parent)) {
                            sibling = true;
                            break;
                        }
                    }
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
    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

}
