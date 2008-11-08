package com.blueskyminds.homebyfive.business.region.service;

import com.blueskyminds.homebyfive.business.region.dao.*;
import com.blueskyminds.homebyfive.business.region.graph.Country;
import com.blueskyminds.homebyfive.business.region.graph.State;
import com.blueskyminds.homebyfive.business.region.PathHelper;
import com.blueskyminds.homebyfive.business.region.StateTableFactory;
import com.blueskyminds.homebyfive.business.region.group.RegionGroup;
import com.blueskyminds.homebyfive.business.region.group.RegionGroupFactory;
import com.blueskyminds.homebyfive.business.tag.service.TagService;
import com.blueskyminds.homebyfive.framework.core.table.TableModel;
import com.wideplay.warp.persist.Transactional;
import com.google.inject.Inject;

import javax.persistence.EntityManager;
import java.util.Set;

/**
 * Date Started: 7/11/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class CountryServiceImpl extends CommonRegionServices<Country> implements CountryService {

    private CountryEAO countryEAO;
    private StateEAO stateEAO;

    public CountryServiceImpl(EntityManager em, TagService tagService, CountryEAO countryEAO, StateEAO stateEAO) {
        super(em, countryEAO, tagService);
        this.countryEAO = countryEAO;
        this.stateEAO = stateEAO;
    }

    public CountryServiceImpl() {
    }

    public RegionGroup list() {
        Set<Country> countries = countryEAO.listCountries();
        return RegionGroupFactory.createCountries(countries);
    }

    public RegionGroup list(String parentPath) {
        Set<Country> countries = countryEAO.listCountries();
        return RegionGroupFactory.createCountries(countries);
    }

    /**
     * @param path
     * @return
     */
    public Country lookup(String path) {
        return countryEAO.lookupCountry(path);
    }

    /**
     * Create a new country
     * Propagates the change into the RegionGraph as well
     *
     * NOTE: Does not rollback the transaction in the cases of an InvalidRegionException or DuplicateRegionException as
     *  no writes occur
     * @param country
     */
    @Transactional(exceptOn = {InvalidRegionException.class, DuplicateRegionException.class})
    public Country create(Country country) throws DuplicateRegionException, InvalidRegionException {
        country.populateAttributes();
        if (country.isValid()) {
            Country existing = countryEAO.lookupCountry(country.getPath());
            if (existing == null) {
//                RegionFactory regionFactory = new RegionFactory();
//                country = regionFactory.createCountry(country.getName(), country.getAbbr(), null, null);
//                //country = addressService.createCountry(country.getName(), country.getAbbr(), null, null);
                em.persist(country);
            } else {
                throw new DuplicateRegionException(country);
            }
        } else {
            throw new InvalidRegionException(country);
        }
        return country;
    }

    /**
     * Update an existing country
     * Propagates the change into the RegionGraph as well
     *
     * NOTE: Does not rollback the transaction in the cases of an InvalidRegionException or DuplicateRegionException as
     *  no writes occur
     * @param path       existing path (may be changed))
     * @param country
     */
    @Transactional(exceptOn = {InvalidRegionException.class})
    public Country update(String path, Country country) throws InvalidRegionException {
        country.populateAttributes();
        if (country.isValid()) {
            Country existing = countryEAO.lookupCountry(path);
            if (existing != null) {
                existing.mergeWith(country);
                em.persist(existing);
                return existing;
            } else {
                throw new InvalidRegionException(country);
            }
        } else {
            throw new InvalidRegionException(country);
        }
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
        return stateEAO.listStates(PathHelper.buildPath(country));
    }

    @Inject
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Inject
    public void setCountryEAO(CountryEAO countryEAO) {
        this.countryEAO = countryEAO;
    }

    @Inject
    public void setStateEAO(StateEAO stateEAO) {
        this.stateEAO = stateEAO;
    }
   
}
