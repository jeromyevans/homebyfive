package com.blueskyminds.homebyfive.business.region.service;

import com.blueskyminds.homebyfive.business.region.graph.Suburb;
import com.blueskyminds.homebyfive.business.region.graph.Street;
import com.blueskyminds.homebyfive.business.region.graph.Country;
import com.blueskyminds.homebyfive.business.region.group.RegionGroup;
import com.blueskyminds.homebyfive.business.address.StreetType;
import com.blueskyminds.homebyfive.business.address.StreetSection;
import com.blueskyminds.homebyfive.framework.core.table.TableModel;

import java.util.Set;
import java.util.List;

/**
 * Date Started: 20/11/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public interface StreetService extends RegionServiceI<Street> {

    Street lookup(String country, String state, String suburb, String street);

    RegionGroup listStreetsAsGroup(String country, String state, String suburb);
    TableModel listStreetsAsTable(String country, String state, String suburb);
    Set<Street> listStreets(String country, String state, String suburb);
    Set<Street> listStreets(Country country);
    Set<Street> listStreets(Suburb suburb);

    /**
     * Find a street in the specified country
     * <p/>
     * <p/>
     * Performs a fuzzy match and returns the matches in order of rank
     */
    List<Street> find(String name, String countryCode);
    List<Street> find(String name, final StreetType streetType, final StreetSection streetSection, Suburb suburb);


}