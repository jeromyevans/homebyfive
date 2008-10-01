package com.blueskyminds.homebyfive.framework.core.persistence.jpa.dao;

import java.util.List;

/**
 * A simple DAO interface with methods commonly used
 *
 * Date Started: 15/11/2007
 * <p/>
 * History:
 */
public interface CommonDAO<T> {

    /**
     * Search for a single entity of the underlying class.  The first result is returned
     *
     * @return the first result
     */
    T findOne();

    /**
     * Search for a single entity of the underlying class.  A random entity is returned
     *
     * @return the first result
     */
    T findRandom();

     /**
     * Lookup all entities of the underlying class (including subclasses)
     */
    List<T> findAll();

     /**
     * Search for an entity with the matching Id
     *
     * @param id    the id of the entity as defined by its @Id attribute
     * @return an instance of the entity or null if not found
     */
    T findById(Object id);

     /**
     * Count how many instances of the underlying entity are in persistence.
     *
     * @return count, as a long
     */
    long count();
}
