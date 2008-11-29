package com.blueskyminds.homebyfive.business.region.service;

import com.blueskyminds.homebyfive.business.region.graph.Country;
import com.blueskyminds.homebyfive.business.region.graph.State;
import com.blueskyminds.homebyfive.business.region.graph.Suburb;
import com.blueskyminds.homebyfive.business.region.graph.PostalCode;
import com.blueskyminds.homebyfive.business.region.group.RegionGroup;
import com.blueskyminds.homebyfive.framework.core.table.TableModel;

import java.util.Set;
import java.util.List;

/**
 * Date Started: 7/11/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public interface StateService extends RegionServiceI<State> {

    State lookup(String country, String state);
    State lookup(Country country, String exactName);

    /**
    * Find a state in the specified country.  Pulls the result from a cache if recently used
    *
    * <p/>
    * Performs a fuzzy match and returns the matches in order of rank
    */
    List<State> find(String name, String countryAbbr);

    RegionGroup listStatesAsGroup(String country);
    TableModel listStatesAsTable(String country);
    Set<State> listStates(String country);
    Set<State> list(Country country);
}