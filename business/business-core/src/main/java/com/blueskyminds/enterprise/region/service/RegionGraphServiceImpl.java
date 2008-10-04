package com.blueskyminds.enterprise.region.service;

import com.blueskyminds.enterprise.region.RegionTypes;
import com.blueskyminds.enterprise.region.graph.Region;
import com.blueskyminds.enterprise.region.dao.RegionGraphDAO;
import com.blueskyminds.homebyfive.framework.core.patterns.LevensteinDistanceTools;
import com.blueskyminds.homebyfive.framework.core.DomainObjectStatus;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Access to the region graph
 *
 * Date Started: 17/06/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd<br/>
 */
public class RegionGraphServiceImpl implements RegionGraphService {

    private EntityManager em;
    private RegionGraphDAO regionGraphDAO;

    public RegionGraphServiceImpl(EntityManager em, RegionGraphDAO regionGraphDAO) {
        this.em = em;
        this.regionGraphDAO = regionGraphDAO;
    }

    public RegionGraphServiceImpl() {
    }

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    public void setRegionDAO(RegionGraphDAO regionGraphDAO) {
        this.regionGraphDAO = regionGraphDAO;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the RegionServiceImpl with default attributes
     */
    private void init() {
    }

     /**
     * Lookup a region by its name.
     * The parent must be provided to limit the search (eg. to a country)
     * <p/>
     * Uses Levenshtein Distance matching and returns results in order of relevance
     */
    public List<Region> findRegion(Region parentRegion, String name) {
        RegionGraphDAO regionGraphDAO = new RegionGraphDAO(em);
         // todo: eagerly load aliases
        Set<Region> children = regionGraphDAO.findChildren(parentRegion);
        return LevensteinDistanceTools.matchName(name, children);
    }

    /**
     * Lookup a region by its name.
     * The parent must be provided to limit the search (eg. to a country)
     * <p/>
     * Uses Levenshtein Distance matching and returns results in order of relevance
     */
    public List<Region> findRegion(Region parentRegion, String name, RegionTypes type) {
        RegionGraphDAO regionGraphDAO = new RegionGraphDAO(em);

        Set<Region> children = regionGraphDAO.findChildrenOfType(parentRegion, type);
        return LevensteinDistanceTools.matchName(name, children);
    }

    /**
     * Gets the set of regions that are ancestors for the current region
     *
     * @param region
     * @return
     */
    public Set<Region> listAncestors(Region region) {
        Set<Region> ancestors = new HashSet<Region>();

        Set<Region> parents = regionGraphDAO.findParents(region);
        for (Region parent : parents) {
            visitAncestors(parent, ancestors);
        }

        return ancestors;
    }

    /** Recursive method to visit all the ancestors of a region once */
    private void visitAncestors(Region region, Set<Region> ancestors) {
        ancestors.add(region);
        Set<Region> parents = regionGraphDAO.findParents(region);

        for (Region parent : parents) {
            if (!ancestors.contains(parent)) {
                visitAncestors(parent, ancestors);
            }
        }
    }

    /**
     * Gets the set of regions that are descendants for the current region
     *
     * @param region
     * @return
     */
    public Set<Region> listDescendants(Region region) {
        Set<Region> descendants = new HashSet<Region>();

        Set<Region> children = regionGraphDAO.findChildren(region);
        for (Region child : children) {
            visitDescendants(child, descendants);
        }

        return descendants;
    }

    public Region lookupRegionById(Long id) {
        return regionGraphDAO.findById(id);
    }

    public void deleteRegionById(Long id) {
        Region regionHandle = regionGraphDAO.findById(id);
        if (regionHandle != null) {
            regionHandle.setStatus(DomainObjectStatus.Deleted);
        }
    }

    public List<Region> autocompleteRegion(String name) {
        return regionGraphDAO.autocompleteRegionByName(name);
    }

    /**
     * Recursive method to visit all the descendants of a region once
     * This can be very slow...
     * */
    private void visitDescendants(Region region, Set<Region> descendants) {
        descendants.add(region);
        Set<Region> children = regionGraphDAO.findChildren(region);

        for (Region child : children) {
            if (!descendants.contains(child)) {
                visitDescendants(child, descendants);
            }
        }
    }

}
