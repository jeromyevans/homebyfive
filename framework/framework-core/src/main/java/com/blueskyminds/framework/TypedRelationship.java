package com.blueskyminds.framework;

import com.blueskyminds.framework.ManyToManyMap;

/**
 *
 * A typed association is a relationship between two entities, where the relationship has attributes and those
 * attributes don't belong in other of the entities.
 *
 * The typed association is used to enforce a pattern.
 *
 * The parameter P is the primary entity
 * The paramater R is the referenced entity
 * The parameter T should be the underlying Type class, which may be a class or enumeration, or hierarchical type
 *
 * Date Started: 4/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@ManyToManyMap
public interface TypedRelationship<P, R, T> {

    /**
     * Get the type of relationship
     * @return the type of relationship
     */
    T getType();

    /**
     * The relationship is from this primary object
     * @return primary object
     */
    P getPrimary();

    /**
     * The relatinoship is to this referenced object
     * @return referenced object
     */
    R getReference();

}
