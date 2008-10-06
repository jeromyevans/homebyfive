package com.blueskyminds.business.region.dao;

import com.blueskyminds.business.region.RegionTypes;
import com.blueskyminds.business.region.graph.Region;
import com.blueskyminds.homebyfive.framework.core.persistence.jpa.dao.AbstractDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

/**
 * Methods for finding regions
 *
 * Date Started: 17/06/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class RegionGraphDAO extends AbstractDAO<Region> {

    public static final Log LOG = LogFactory.getLog(RegionGraphDAO.class);

    private static final String QUERY_REGION_CHILDREN = "region.findChildren";
    private static final String PARAM_REGION_PARENT = "parent";
    private static final String PARAM_REGION_PARENT_ID = "parentId";
    private static final String QUERY_REGION_PARENTS = "region.findParents";
    private static final String PARAM_REGION_CHILD = "child";
    private static final String QUERY_REGION_BY_NAME = "region.byName";
    private static final String QUERY_REGION_BY_NAME_AND_TYPE = "region.byNameAndType";
    private static final String PARAM_NAME = "name";
    private static final String PARAM_TYPE = "type";
    private static final String QUERY_REGION_BY_NAME_TYPE_AND_PARENT = "region.byNameTypeAndParent";
    private static final String QUERY_REGION_CHILDREN_AND_TYPE = "region.findChildrenOfType";



//    private static final String QUERY_REGION_BY_NAME = "region.byName";
//    private static final String PARAM_REGION_NAME= "name";
//
//    private static final String PARAM_REGION_SET = "regionSet";
//    private static final String PARAM_EXCLUDES = "excludes";
//
//    private static final String QUERY_PARENTS = "region.parents";
//    private static final String QUERY_CHILDREN = "region.children";
//    private static final String PARAM_REGION = "region";
//
//    private static final String QUERY_ANY_PARENTS = "region.anyParents";
//    private static final String QUERY_ANY_PARENTS_EXCLUDING = "region.anyParentsEx";
//    private static final String QUERY_ANY_CHILDREN = "region.anyChildren";
//    private static final String QUERY_ANY_CHILDREN_EXCLUDING = "region.anyChildrenEx";

    public RegionGraphDAO(EntityManager em) {
        super(em, Region.class);
    }

    /**
     * Finds regions matching the given name using a basic like match on the name and aliases
     *
     * @param name
     * @return set of RegionHandles
     */
    public Set<Region> findRegionByName(String name) {
        Set<Region> regions;

        Query query = em.createNamedQuery(QUERY_REGION_BY_NAME);
        query.setParameter(PARAM_NAME, name);
        regions = new HashSet<Region>(query.getResultList());

        return regions;
    }

     /**
     * Finds regions matching the given name using a basic like match on the name and aliases with a wildcard
     *  after name
     *
     * @param name
     * @return set of RegionHandles
     */
    public List<Region> autocompleteRegionByName(String name) {
        List<Region> regions;

        Query query = em.createNamedQuery(QUERY_REGION_BY_NAME);
        query.setParameter(PARAM_NAME, name+"%");
        regions = query.getResultList();

        return regions;
    }

    /**
     * Quick lookup of the children of the specified region
     *
     * @return set of RegionHandles
     **/
    public Set<Region> findChildren(Region handle) {
        Set<Region> regions;

        Query query = em.createNamedQuery(QUERY_REGION_CHILDREN);
        query.setParameter(PARAM_REGION_PARENT, handle);
        regions = new HashSet<Region>(query.getResultList());

        return regions;
    }

    /**
     * Quick lookup of the children of the specified region and of a particular type
     *
     * @return set of RegionHandles
     **/
    public Set<Region> findChildrenOfType(Region parentHandle, RegionTypes type) {
        Set<Region> regions;

        Query query = em.createNamedQuery(QUERY_REGION_CHILDREN_AND_TYPE);
        query.setParameter(PARAM_REGION_PARENT_ID, parentHandle.getId());
        query.setParameter(PARAM_TYPE, type);
        regions = new HashSet<Region>(query.getResultList());

        return regions;
    }

    /**
     * Quick lookup of the parents of the specified region
     **/
    public Set<Region> findParents(Region handle) {
        Set<Region> regions;

        Query query = em.createNamedQuery(QUERY_REGION_PARENTS);
        query.setParameter(PARAM_REGION_CHILD, handle);
        regions = new HashSet<Region>(query.getResultList());

        return regions;
    }

    /**
     * Get a country by its exact name or alias.  Returns the first match.
     *
     * @param name
     * @return
     */
    public Region getCountry(String name) {
        Set<Region> regions;

        Query query = em.createNamedQuery(QUERY_REGION_BY_NAME_AND_TYPE);
        query.setParameter(PARAM_NAME, name);
        query.setParameter(PARAM_TYPE, RegionTypes.Country);
        regions = new HashSet<Region>(query.getResultList());

        if (regions.size() > 0) {
            return regions.iterator().next();
        } else {
            return null;
        }
    }

    /**
     * Lookup a region by its name (like match), type and parent.  Returns the first found
     *
     * @param name
     * @param type
     * @param parentHandle
     * @return
     */
    private Region getRegionByNameTypeParent(String name, RegionTypes type, Region parentHandle) {
        Set<Region> regions;

        Query query = em.createNamedQuery(QUERY_REGION_BY_NAME_TYPE_AND_PARENT);
        query.setParameter(PARAM_NAME, name);
        query.setParameter(PARAM_TYPE, type);
        query.setParameter(PARAM_REGION_PARENT, parentHandle);
        regions = new HashSet<Region>(query.getResultList());

        if (regions.size() > 0) {
            return regions.iterator().next();
        } else {
            return null;
        }
    }

    /**
     * Get a state in the specified country by its exact name or alias.  Returns the first match.
     *
     * @param name
     * @return
     */
    public Region getState(String name, Region country) {
        return getRegionByNameTypeParent(name, RegionTypes.State, country);
    }

    /**
     * Get a postcode in the specified state by its exact name or alias.  Returns the first match.
     *
     * @param name
     * @return
     */
    public Region getPostCode(String name, Region state) {
        return getRegionByNameTypeParent(name, RegionTypes.PostCode, state);
    }

/*
    *//**
     * Get an instance of a region matching the given name
     *
     * @param name
     * @return Region instance, or null if not found
     *//*
    public List<Region> findRegionByName(String name) {
        List<Region> regions;

        Query query = em.createNamedQuery(QUERY_REGION_BY_NAME);
        query.setParameter(PARAM_REGION_NAME, name);
        regions = query.getResultList();

        return regions;
    }   

//
//    *//** Lookup the ancestors of the specified region using the index *//*
//    public Set<Region> findAncestors(Region region) {
//        List<Region> regions;
//
//        Query query = em.createNamedQuery(QUERY_ANCESTORS);
//        query.setParameter(PARAM_REGION_SET, region);
//        regions = query.getResultList();
//
//        return new HashSet<Region>(regions);
//    }

    *//** Lookup the ancestors of the specified region using recurive queries *//*
    public Set<Region> findAncestors(Region descendant) {
        Set<Region> ancestors = new HashSet<Region>();

        // this is a recursive search up the region graph.  It stops when there's no more parents.
        Set<Region> parents = findParents(descendant);
        recurseAncestors(parents, ancestors);

        return ancestors;
    }

     private Set<Region> findParents(Region child) {
        Query query = em.createNamedQuery(QUERY_PARENTS);
        query.setParameter(PARAM_REGION, child);
        return new HashSet<Region>(query.getResultList());
    }

    *//**
     * Performs a recusive query until the ancestors of all parents have been obtained.
     *
     * It's quite simple - looks up all the parents of the current set of new regions.
     * For each of the new regions found, their parents are recursively looked up.
     * The set of known ancestors is used to filter out parents that have already been 
     * encountered.
     **//*
    private void recurseAncestors(Set<Region> newRegions, Set<Region> knownAncestors) {
        Set<Region> parents;

        Query query;
        if (knownAncestors.size() > 0) {
            query = em.createNamedQuery(QUERY_ANY_PARENTS_EXCLUDING);
            query.setParameter(PARAM_EXCLUDES, knownAncestors);
        } else {
            query = em.createNamedQuery(QUERY_ANY_PARENTS);
        }

        query.setParameter(PARAM_REGION_SET, newRegions);
        parents = new HashSet<Region>(query.getResultList());
        if (parents.size() > 0) {
            knownAncestors.addAll(parents);

            recurseAncestors(parents, knownAncestors);
        }
    }

    */

    /** Lookup the descendants of the specified region using recurive queries */
//    public Set<Region> findDescendants(Region ancestor) {
//        Set<Region> descendants = new HashSet<Region>();
//
//        // this is a recursive search down the region graph.  It stops when there's no more children.
//        Set<Region> children = findChildren(ancestor);
//        recurseDescendants(children, descendants);
//
//        return descendants;
//    }

//    private Set<Region> findChildren(Region parent) {
//        Query query = em.createNamedQuery(QUERY_CHILDREN);
//        query.setParameter(PARAM_REGION, parent);
//        return new HashSet<Region>(query.getResultList());
//    }

    /**
     * Performs a recusive query until all the descendants of region are found .
     *
     * It's quite simple - looks up the children of the current region, then for
     *  each of its children, recursively looks up their children.
     *
     * The set of known descendants is used to filter out children that have already been
     * encountered (it's a graph, not a tree)
     * */
//    private void recurseDescendants(Set<Region> newRegions, Set<Region> descendants) {
//        Set<Region> children;
//        Query query;
//        if (descendants.size() > 0) {
//            query = em.createNamedQuery(QUERY_ANY_CHILDREN_EXCLUDING);
//            query.setParameter(PARAM_EXCLUDES, descendants);
//        } else {
//            query = em.createNamedQuery(QUERY_ANY_CHILDREN);
//        }
//
//        query.setParameter(PARAM_REGION_SET, newRegions);
//        children = new HashSet<Region>(query.getResultList());
//        if (children.size() > 0) {
//            descendants.addAll(children);
//
//            recurseDescendants(children, descendants);
//        }
//    }
}
