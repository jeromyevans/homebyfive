package com.blueskyminds.homebyfive.business.region.service;

import com.wideplay.warp.persist.Transactional;
import com.blueskyminds.homebyfive.business.region.graph.State;
import com.blueskyminds.homebyfive.business.region.graph.Country;
import com.blueskyminds.homebyfive.business.region.PathHelper;
import com.blueskyminds.homebyfive.business.region.StateTableFactory;
import com.blueskyminds.homebyfive.business.region.dao.StateEAO;
import com.blueskyminds.homebyfive.business.region.group.RegionGroup;
import com.blueskyminds.homebyfive.business.region.group.RegionGroupFactory;
import com.blueskyminds.homebyfive.business.tag.service.TagService;
import com.blueskyminds.homebyfive.business.address.dao.AddressDAO;
import com.blueskyminds.homebyfive.framework.core.table.TableModel;
import com.blueskyminds.homebyfive.framework.core.patterns.LevensteinDistanceTools;
import com.google.inject.Inject;
import org.apache.commons.lang.StringUtils;

import javax.persistence.EntityManager;
import java.util.*;

/**
 * Date Started: 7/11/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class StateServiceImpl extends CommonRegionServices<State> implements StateService {

    private CountryService countryService;

    /** Cache of recently used state names */
    private transient Map<String, List<State>> stateNameCache;

    public StateServiceImpl(EntityManager em, TagService tagService, CountryService countryService, StateEAO regionEAO) {
        super(em, regionEAO, tagService);
        this.countryService = countryService;
        init();
    }

    public StateServiceImpl() {
        init();
    }

    private void init() {
        stateNameCache = new HashMap<String, List<State>>();
    }

    /**
    * Find a state in the specified country.  Pulls the result from a cache if recently used
    *
    * <p/>
    * Performs a fuzzy match and returns the matches in order of rank
    */
    public List<State> find(String name, String countryAbbr) {
        List<State> states = stateNameCache.get(countryAbbr + ":" + name);
        if (states == null) {

            Country country = countryService.lookup(PathHelper.buildPath(countryAbbr));
            if (country != null) {
                Set<State> allStates = regionDAO.list(country.getPath());

                states = LevensteinDistanceTools.matchName(name, allStates);

                stateNameCache.put(countryAbbr + ":" + name, states);
            } else {
                states = new LinkedList<State>();
            }
        }

        return states;
    }

    /**
     * Create a new State
     * Propagates the change into the RegionGraph as well
     * <p/>
     * If the state is not bound to a country, the parentPath will be used to lookup the country
     * <p/>
     * NOTE: Does not rollback the transaction in the case of a DuplicateRegionException as no write occurs
     */
    @Transactional(exceptOn = {InvalidRegionException.class, DuplicateRegionException.class})
    public State create(State state) throws DuplicateRegionException, InvalidRegionException {
        state.populateAttributes();
        State existing = regionDAO.lookup(state.getPath());
        if (existing == null) {
            Country country = state.getCountry();
            if (country == null) {
                // see if the parent path references a country
                if (StringUtils.isNotBlank(state.getParentPath())) {
                    country = countryService.lookup(state.getParentPath());
                    if (country != null) {
                        country.addState(state);
                        state.setCountry(country);
                    }
                }
            } else {
                // ensure the state has been added as a chill
                country.addState(state);
            }

//            state = addressService.createState(state.getName(), state.getAbbr(), state.getCountry());
            if (country != null) {                
                em.persist(country);
            } else {
                throw new InvalidRegionException("Invalid parent region (country)", state);
            }
        } else {
            throw new DuplicateRegionException(state);
        }
        return state;
    }  

    public State lookup(String country, String state) {
        return lookup(PathHelper.buildPath(country, state));
    }

    public State lookup(Country country, String exactName) {
        return lookup(PathHelper.joinPath(country.getPath(), exactName));
    }

    public State lookup(String statePath) {
        return regionDAO.lookup(statePath);
    }

     /** List the states in the specified country */
    public Set<State> list(Country country) {
        return regionDAO.list(country.getPath());
    }

    public RegionGroup list(String parentPath) {
        Set<State> states = regionDAO.list(parentPath);
        return RegionGroupFactory.createStates(states);
    }     

    public RegionGroup listStatesAsGroup(String country) {
        Set<State> states = listStates(country);
        return RegionGroupFactory.createStates(states);
    }

    public TableModel listStatesAsTable(String country) {
        Set<State> states = listStates(country);
        return StateTableFactory.createTable(states);
    }

    public Set<State> listStates(String country) {
        return regionDAO.list(PathHelper.buildPath(country));
    }
    
    @Inject
    public void setCountryService(CountryService countryService) {
        this.countryService = countryService;
    }

    @Inject
    public void setRegionEAO(StateEAO regionEAO) {
        this.regionDAO = regionEAO;
    }

}
