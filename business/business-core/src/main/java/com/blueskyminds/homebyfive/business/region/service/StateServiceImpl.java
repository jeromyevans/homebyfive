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
import com.blueskyminds.homebyfive.framework.core.table.TableModel;
import com.google.inject.Inject;
import org.apache.commons.lang.StringUtils;

import javax.persistence.EntityManager;
import java.util.Set;

/**
 * Date Started: 7/11/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class StateServiceImpl extends CommonRegionServices<State> implements StateService {

    private CountryService countryService;

    public StateServiceImpl(EntityManager em, TagService tagService, CountryService countryService, StateEAO regionEAO) {
        super(em, regionEAO, tagService);
        this.countryService = countryService;
    }

    public StateServiceImpl() {
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
                        state.setCountry(country);
                    }
                }
            }

//            state = addressService.createState(state.getName(), state.getAbbr(), state.getCountry());
            if (country != null) {
                em.persist(state);
                em.persist(country);
            } else {
                throw new InvalidRegionException("Invalid parent region (country)", state);
            }
        } else {
            throw new DuplicateRegionException(state);
        }
        return state;
    }

    /**
     * Update an existing state
     * Propagates the change into the RegionGraph as well
     * <p/>
     * NOTE: Does not rollback the transaction in the case of a DuplicateRegionException as no write occurs
     */
    @Transactional(exceptOn = {InvalidRegionException.class})
    public State update(String path, State state) throws InvalidRegionException {
        state.populateAttributes();

        State existing = regionDAO.lookup(path);
        if (existing != null) {
            existing.mergeWith(state);
            em.persist(existing);
        } else {
            throw new InvalidRegionException(state);
        }
        return state;
    }

    public State lookup(String country, String state) {
        return lookup(PathHelper.buildPath(country, state));
    }

    public State lookup(String statePath) {
        return regionDAO.lookup(statePath);
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
