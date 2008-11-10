package com.blueskyminds.homebyfive.business.region.service;

import com.blueskyminds.homebyfive.business.region.graph.Suburb;
import com.blueskyminds.homebyfive.business.region.group.RegionGroup;
import com.blueskyminds.homebyfive.framework.core.table.TableModel;

import java.util.Set;

/**
 * Date Started: 7/11/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public interface SuburbService extends RegionServiceI<Suburb> {

    Suburb lookup(String country, String state, String suburb);

    RegionGroup listSuburbsAsGroup(String country, String state);
    TableModel listSuburbsAsTable(String country, String state);
    Set<Suburb> listSuburbs(String country, String state);
    
    RegionGroup listSuburbs(String country, String state, String postCode);
    TableModel listSuburbsAsTable(String country, String state, String postCode);

}
