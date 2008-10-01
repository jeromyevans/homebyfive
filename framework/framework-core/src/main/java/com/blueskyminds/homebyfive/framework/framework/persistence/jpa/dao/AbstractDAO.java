package com.blueskyminds.homebyfive.framework.framework.persistence.jpa.dao;

import com.blueskyminds.homebyfive.framework.framework.persistence.jpa.JpaPage;
import com.blueskyminds.homebyfive.framework.framework.persistence.paging.Page;
import com.blueskyminds.homebyfive.framework.framework.persistence.paging.QueryPager;
import com.blueskyminds.homebyfive.framework.framework.persistence.query.QueryFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

/**
 * Provides common methods for a DAO
 * <p/>
 * All DAO implementations must be stateless and thread safe
 * <p/>
 * Date Started: 6/06/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class AbstractDAO<T> implements QueryPager, CommonDAO<T> {

    protected EntityManager em;
    protected Class<T> defaultClass;

    public AbstractDAO(EntityManager em, Class<T> defaultClass) {
        this.em = em;
        this.defaultClass = defaultClass;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Search for a single entity using the supplied query.  The first result is returned
     *
     * The implemetation does not use query.getSingleResult() because that method throws an exception if the
     *  result is anything other than 1 (ie. an exception even if there's 0 result)
     *
     * @return the first result, or null if not found
     */
    public T findOne(Query query) {
        T result = null;
        query.setMaxResults(1);
        List<T> results = query.getResultList();

        if (results.size() > 0) {
            result = results.get(0);
        }
        return result;
    }

    /**
     * Search for a single entity of the underlying class.  The first result is returned
     *
     * @return the first result
     */
    public T findOne() {
        return findOne(defaultClass);
    }

    /**
     * Search for a single entity.  The first result is returned
     *
     * @return the first result
     */
    protected T findOne(Class clazz) {
        Query query = prepareFindAllQuery(clazz);
        return (T) findOne(query);
    }

    /**
     * Search for a single entity of the underlying class.  A random entity is returned
     *
     * @return the first result
     */
    public T findRandom() {
        long totalEntities = count(defaultClass);

        Random generator = new Random();
        int offset = generator.nextInt((int) totalEntities);

        // uses a page size of 1
        Page page = findPage(offset, 1);

        if (page != null) {
            if (page.getPageResults().size() > 0) {
                return (T) page.getPageResults().get(0);
            }
        }

        return null;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Creates a commonly used query to find all objects of a particular class
     **/
    private Query prepareFindAllQuery(Class clazz) {
        return QueryFactory.createFindAllQuery(em, clazz);
    }

    /**
     * Lookup all entities of the underlying class (including subclasses)
     */
    public List<T> findAll() {
        return findAll(defaultClass);
    }

    /**
     * Lookup all entities of the specified class (including subclasses)
     */
    protected <T> List<T> findAll(Class clazz) {
        Query query = prepareFindAllQuery(clazz);
        return (List<T>) query.getResultList();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Search for an entity with the matching Id
     *
     * @param id    the id of the entity as defined by its @Id attribute
     * @return an instance of the entity or null if not found
     */
    public T findById(Object id) {
        return findById(defaultClass, id);
    }

      /**
     * Search for an entity with the matching Id and class
     *
     * @param id    the id of the entity as defined by its @Id attribute
     * @return an instance of the entity or null if not found
     */
    protected <X> X findById(Class<X> clazz, Object id) {
        return em.find(clazz, id);
    }

    // ------------------------------------------------------------------------------------------------------

    protected void setLikeParameter(Query query, String paramName, String value) {
        if (value != null) {
            query.setParameter(paramName, "%" + value + "%");
        } else {
            query.setParameter(paramName, "%");
        }
    }

    // ------------------------------------------------------------------------------------------------------


    /**
     * Count how many instances of the underlying entity are in persistence.
     *
     * @return count, as a long
     */
    public long count() {
        return count(defaultClass);
    }

    /**
     * Count how many instances of the underlying entity are in persistence.
     *
     * @return count, as a long
     */
    protected long count(Class clazz) {
        Long result;

        Query query = em.createQuery("select count(*) from "+ clazz.getName());
        result = (Long) query.getSingleResult();
        if (result != null) {
            return result;
        } else {
            return 0;
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /** Returns a page containing entities of the underlying class. */
    public Page findPage(int pageNo, int pageSize) {
        return findPage(defaultClass, pageNo, pageSize);
    }

    /** Returns a page containing entities of the specified class. */
    protected Page findPage(Class clazz, int pageNo, int pageSize) {
        return new JpaPage(prepareFindAllQuery(clazz), pageNo, pageSize);
    }

    /**
     * Search for domain objects that match the given criteria, returning the requested page
     * @param query
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Page findPage(Query query, int pageNo, int pageSize){
        return new JpaPage(query, pageNo,  pageSize);
    }

    /**
     * Returns a Set containing the collection
     *
     * @param collection
     * @return the set or empty set if null/empty
     */
    protected Set<T> setOf(Collection<T> collection) {
        if (collection != null) {
            return new HashSet<T>(collection);
        }
       return new HashSet<T>();
    }
    
    /**
     * Returns the first item in a collection
     * Handles null case.  First item in a set will be the first returned by an iterator. 
     * @param collection
     * @return
     */
    protected T firstIn(Collection<T> collection) {
        if (collection != null) {
            if (collection.size() > 0) {
                return collection.iterator().next();
            }
        }
       return null;
    }
}
