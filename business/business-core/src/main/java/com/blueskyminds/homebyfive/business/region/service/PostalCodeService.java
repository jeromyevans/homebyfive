package com.blueskyminds.homebyfive.business.region.service;

import com.blueskyminds.homebyfive.business.region.graph.PostalCode;
import com.blueskyminds.homebyfive.business.region.group.RegionGroup;
import com.blueskyminds.homebyfive.framework.core.table.TableModel;

import java.util.Set;

/**
 * Date Started: 7/11/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public interface PostalCodeService extends RegionServiceI<PostalCode> {


    PostalCode lookup(String country, String state, String postCode);
        
    RegionGroup listSuburbs(String country, String state, String postCode);
    TableModel listSuburbsAsTable(String country, String state, String postCode);
}
