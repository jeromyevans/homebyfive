package com.blueskyminds.homebyfive.business.region.service;

import com.blueskyminds.homebyfive.business.region.graph.Suburb;
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
public interface SuburbService extends RegionServiceI<Suburb> {

    Suburb lookup(String country, String state, String suburb);

    RegionGroup listSuburbsAsGroup(String country, String state);
    TableModel listSuburbsAsTable(String country, String state);
    Set<Suburb> listSuburbs(String country, String state);

      /** List the suburbs in the specified state */
    Set<Suburb> listSuburbs(State state);
    RegionGroup listSuburbs(String country, String state, String postCode);
    TableModel listSuburbsAsTable(String country, String state, String postCode);
    Set<Suburb> listSuburbs(Country country);

    Set<Suburb> listSuburbs(String country);

    /**
     * Find a suburb in the specified country
     *
     * <p/>
     * Performs a fuzzy match and returns the matches in order of rank
     */
    List<Suburb> find(String name, String countryCode);
    
     /**
     * Find a suburb in the specified state(s)
     *
     * <p/>
     * Performs a fuzzy match and returns the matches in order of rank
     */
    List<Suburb> find(String name, List<State> states);

     /**
     * Find a suburb in the specified state(s)
     *
     * <p/>
     * Performs a fuzzy match and returns the matches in order of rank
     */
    List<Suburb> find(String name, State state);
}
