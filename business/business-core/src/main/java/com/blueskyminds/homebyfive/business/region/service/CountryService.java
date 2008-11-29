package com.blueskyminds.homebyfive.business.region.service;

import com.blueskyminds.homebyfive.business.region.graph.Country;
import com.blueskyminds.homebyfive.business.region.graph.State;
import com.blueskyminds.homebyfive.business.region.group.RegionGroup;
import com.blueskyminds.homebyfive.framework.core.table.TableModel;

import java.util.Set;
import java.util.List;

/**
 * Date Started: 7/11/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public interface CountryService extends RegionServiceI<Country> {

    Country lookupCountry(String abbr);

    RegionGroup list();

    /**
     * Find the country with the specified name
     * <p/>
     * Performs a fuzzy match and returns the matches in order of rank
     */
    List<Country> find(String name);

}
