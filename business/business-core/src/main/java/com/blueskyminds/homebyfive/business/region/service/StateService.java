package com.blueskyminds.homebyfive.business.region.service;

import com.blueskyminds.homebyfive.business.region.graph.Country;
import com.blueskyminds.homebyfive.business.region.graph.State;
import com.blueskyminds.homebyfive.business.region.graph.Suburb;
import com.blueskyminds.homebyfive.business.region.graph.PostalCode;
import com.blueskyminds.homebyfive.business.region.group.RegionGroup;
import com.blueskyminds.homebyfive.framework.core.table.TableModel;

import java.util.Set;

/**
 * Date Started: 7/11/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public interface StateService extends RegionServiceI<State> {

    State lookup(String country, String state);

    RegionGroup listStatesAsGroup(String country);
    TableModel listStatesAsTable(String country);
    Set<State> listStates(String country);
}