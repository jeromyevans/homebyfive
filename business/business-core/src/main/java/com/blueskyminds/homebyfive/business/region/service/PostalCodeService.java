package com.blueskyminds.homebyfive.business.region.service;

import com.blueskyminds.homebyfive.business.region.graph.PostalCode;
import com.blueskyminds.homebyfive.business.region.graph.State;
import com.blueskyminds.homebyfive.business.region.graph.Country;
import com.blueskyminds.homebyfive.business.region.group.RegionGroup;
import com.blueskyminds.homebyfive.framework.core.table.TableModel;

import java.util.Set;
import java.util.List;

/**
 * Date Started: 7/11/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public interface PostalCodeService extends RegionServiceI<PostalCode> {

    PostalCode lookup(String country, String state, String postCode);
    PostalCode lookup(String name, State state);
    
    RegionGroup listPostCodesAsGroup(String country, String state);
    TableModel listPostCodesAsTable(String country, String state);
    Set<PostalCode> listPostCodes(String country, String state);
    Set<PostalCode> listPostCodes(State state);
    Set<PostalCode> list(Country country);

    /**
     * Find a postcode in the specified country
     *
     * <p/>
     * Performs a fuzzy match and returns the matches in order of rank
     */
    List<PostalCode> find(String name, String countryCode);
}
