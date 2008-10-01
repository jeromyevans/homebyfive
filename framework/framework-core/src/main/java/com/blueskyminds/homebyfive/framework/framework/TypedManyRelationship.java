package com.blueskyminds.homebyfive.framework.framework;

import com.blueskyminds.homebyfive.framework.framework.ManyToManyMap;

import java.util.Set;

/**
 *
 * A relationship between two entities, where the relationship has attributes and those
 * attributes don't belong in other of the entities.  Similar to a TypedRelationship, but allowing more than one
 * type (or role) on the relationshp.
 *
 * The TypedManyRelationship interface is used to enforce a pattern.
 *
 * The parameter P is the primary entity
 * The paramater R is the referenced entity
 * The parameter T is be the underlying Type class, for which there is a collection
 *
 * Date Started: 4/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@ManyToManyMap
public interface TypedManyRelationship<P,R,T> {

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

    /**
     * The roles of this relationship
     * @return a set of the roles on this relationship
     */
   Set<T> getRoleMaps();

}
