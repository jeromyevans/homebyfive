package com.blueskyminds.enterprise.region.service;

import com.blueskyminds.enterprise.region.RegionTypes;
import com.blueskyminds.enterprise.region.graph.RegionHandle;

import java.util.List;
import java.util.Set;

/**
 * Provides access to the generalised RegionGraph
 *   
 * Date Started: 17/06/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd<br/>
 */
public interface RegionGraphService {

    /**
     * Lookup a region by its name.
     * The IMMEDIATE parent must be provided to limit the search (eg. to a country or state)
     * <p/>
     * Uses Levenshtein Distance matching and returns results in order of relevance
     */
    List<RegionHandle> findRegion(RegionHandle parentRegion, String name);

    /**
     * Lookup a region by its name and type.
     * The parent must be provided to limit the search (eg. to a country)
     * <p/>
     * Uses Levenshtein Distance matching and returns results in order of relevance
     */
    List<RegionHandle> findRegion(RegionHandle parentRegion, String name, RegionTypes type);

    /**
     * Gets the set of regions that are ancestors of the specified region
     *
     * @param region
     * @return
     */
    Set<RegionHandle> listAncestors(RegionHandle region);

    /**
     * Gets the set of regions that are descendants for the current region
     *
     * @param region
     * @return
     */
    Set<RegionHandle> listDescendants(RegionHandle region);

    RegionHandle lookupRegionById(Long id);

    /** Sets the status of the specified RegionHandle to Deleted */
    void deleteRegionById(Long id);

    List<RegionHandle> autocompleteRegion(String name);
}
