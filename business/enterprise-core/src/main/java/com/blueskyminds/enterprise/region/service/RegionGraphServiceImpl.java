package com.blueskyminds.enterprise.region.service;

import com.blueskyminds.enterprise.region.RegionTypes;
import com.blueskyminds.enterprise.region.RegionHandle;
import com.blueskyminds.enterprise.region.dao.RegionGraphDAO;
import com.blueskyminds.framework.patterns.LevensteinDistanceTools;
import com.blueskyminds.framework.DomainObjectStatus;

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
    public List<RegionHandle> findRegion(RegionHandle parentRegion, String name) {
        RegionGraphDAO regionGraphDAO = new RegionGraphDAO(em);
         // todo: eagerly load aliases
        Set<RegionHandle> children = regionGraphDAO.findChildren(parentRegion);
        return LevensteinDistanceTools.matchName(name, children);
    }

    /**
     * Lookup a region by its name.
     * The parent must be provided to limit the search (eg. to a country)
     * <p/>
     * Uses Levenshtein Distance matching and returns results in order of relevance
     */
    public List<RegionHandle> findRegion(RegionHandle parentRegion, String name, RegionTypes type) {
        RegionGraphDAO regionGraphDAO = new RegionGraphDAO(em);

        Set<RegionHandle> children = regionGraphDAO.findChildrenOfType(parentRegion, type);
        return LevensteinDistanceTools.matchName(name, children);
    }

    /**
     * Gets the set of regions that are ancestors for the current region
     *
     * @param region
     * @return
     */
    public Set<RegionHandle> listAncestors(RegionHandle region) {
        Set<RegionHandle> ancestors = new HashSet<RegionHandle>();

        Set<RegionHandle> parents = regionGraphDAO.findParents(region);
        for (RegionHandle parent : parents) {
            visitAncestors(parent, ancestors);
        }

        return ancestors;
    }

    /** Recursive method to visit all the ancestors of a region once */
    private void visitAncestors(RegionHandle region, Set<RegionHandle> ancestors) {
        ancestors.add(region);
        Set<RegionHandle> parents = regionGraphDAO.findParents(region);

        for (RegionHandle parent : parents) {
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
    public Set<RegionHandle> listDescendants(RegionHandle region) {
        Set<RegionHandle> descendants = new HashSet<RegionHandle>();

        Set<RegionHandle> children = regionGraphDAO.findChildren(region);
        for (RegionHandle child : children) {
            visitDescendants(child, descendants);
        }

        return descendants;
    }

    public RegionHandle lookupRegionById(Long id) {
        return regionGraphDAO.findById(id);
    }

    public void deleteRegionById(Long id) {
        RegionHandle regionHandle = regionGraphDAO.findById(id);
        if (regionHandle != null) {
            regionHandle.setStatus(DomainObjectStatus.Deleted);
        }
    }

    public List<RegionHandle> autocompleteRegion(String name) {
        return regionGraphDAO.autocompleteRegionByName(name);
    }

    /**
     * Recursive method to visit all the descendants of a region once
     * This can be very slow...
     * */
    private void visitDescendants(RegionHandle region, Set<RegionHandle> descendants) {
        descendants.add(region);
        Set<RegionHandle> children = regionGraphDAO.findChildren(region);

        for (RegionHandle child : children) {
            if (!descendants.contains(child)) {
                visitDescendants(child, descendants);
            }
        }
    }

}
