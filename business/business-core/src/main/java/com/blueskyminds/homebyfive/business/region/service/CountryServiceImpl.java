package com.blueskyminds.homebyfive.business.region.service;

import com.blueskyminds.homebyfive.business.region.dao.*;
import com.blueskyminds.homebyfive.business.region.graph.Country;
import com.blueskyminds.homebyfive.business.region.group.RegionGroup;
import com.blueskyminds.homebyfive.business.region.group.RegionGroupFactory;
import com.blueskyminds.homebyfive.business.region.PathHelper;
import com.blueskyminds.homebyfive.business.region.CountryTableFactory;
import com.blueskyminds.homebyfive.business.tag.service.TagService;
import com.blueskyminds.homebyfive.framework.core.patterns.LevensteinDistanceTools;
import com.blueskyminds.homebyfive.framework.core.table.TableModel;
import com.wideplay.warp.persist.Transactional;
import com.google.inject.Inject;

import javax.persistence.EntityManager;
import java.util.Set;
import java.util.List;

/**
 * Date Started: 7/11/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class CountryServiceImpl extends CommonRegionServices<Country> implements CountryService {

    public CountryServiceImpl(EntityManager em, TagService tagService, CountryEAO regionDAO) {
        super(em, regionDAO, tagService);
    }

    public CountryServiceImpl() {
    }

    public Country lookupCountry(String abbr) {
        return lookup(PathHelper.buildPath(abbr));
    }

    public RegionGroup list() {
        Set<Country> countries = regionDAO.list("/");
        return RegionGroupFactory.createCountries(countries);
    }

    public TableModel listCountriesAsTable() {
        Set<Country> countries = regionDAO.list("/");
        return CountryTableFactory.createTable(countries);
    }

    public RegionGroup list(String parentPath) {
        Set<Country> countries = regionDAO.list(parentPath);
        return RegionGroupFactory.createCountries(countries);
    }

    /**
     * @param path
     * @return
     */
    public Country lookup(String path) {
        return regionDAO.lookup(path);
    }

    /**
     * Find the country with the specified name
     * <p/>
     * Performs a fuzzy match and returns the matches in order of rank
     */
    public List<Country> find(String name) {
        Set<Country> countries = regionDAO.list("/");
        return LevensteinDistanceTools.matchName(name, countries);
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
            Country existing = regionDAO.lookup(country.getPath());
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
            Country existing = regionDAO.lookup(path);
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

    @Inject
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Inject
    public void setRegionDAO(CountryEAO regionDAO) {
        this.regionDAO = regionDAO;
    }
   
}
