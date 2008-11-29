package com.blueskyminds.homebyfive.business.region.service;

import com.wideplay.warp.persist.Transactional;
import com.blueskyminds.homebyfive.business.region.graph.Suburb;
import com.blueskyminds.homebyfive.business.region.graph.Street;
import com.blueskyminds.homebyfive.business.region.graph.Country;
import com.blueskyminds.homebyfive.business.region.PathHelper;
import com.blueskyminds.homebyfive.business.region.StreetTableFactory;
import com.blueskyminds.homebyfive.business.region.group.RegionGroup;
import com.blueskyminds.homebyfive.business.region.group.RegionGroupFactory;
import com.blueskyminds.homebyfive.business.region.dao.SuburbEAO;
import com.blueskyminds.homebyfive.business.region.dao.StreetEAO;
import com.blueskyminds.homebyfive.business.tag.service.TagService;
import com.blueskyminds.homebyfive.business.address.dao.AddressDAO;
import com.blueskyminds.homebyfive.business.address.StreetType;
import com.blueskyminds.homebyfive.business.address.StreetSection;
import com.blueskyminds.homebyfive.framework.core.table.TableModel;
import com.blueskyminds.homebyfive.framework.core.patterns.LevensteinDistanceTools;
import com.blueskyminds.homebyfive.framework.core.tools.filters.FilterTools;
import com.blueskyminds.homebyfive.framework.core.tools.filters.Filter;
import com.google.inject.Inject;
import org.apache.commons.lang.StringUtils;

import javax.persistence.EntityManager;
import java.util.Set;
import java.util.List;

/**
 * Date Started: 20/11/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class StreetServiceImpl extends CommonRegionServices<Street> implements StreetService {

    private SuburbService suburbService;

    public StreetServiceImpl(EntityManager em, TagService tagService, SuburbService suburbService, StreetEAO regionDAO) {
        super(em, regionDAO, tagService);
        this.suburbService = suburbService;
    }

    public StreetServiceImpl() {
    }

    public RegionGroup list(String parentPath) {
        Set<Street> suburbs = regionDAO.list(parentPath);
        return RegionGroupFactory.createStreets(suburbs);
    }

    public RegionGroup list(String country, String state, String suburb) {
        Set<Street> streets = regionDAO.list(PathHelper.buildPath(country, state, suburb));
        return RegionGroupFactory.createStreets(streets);
    }

    public TableModel listStreetsAsTable(String country, String state, String suburb) {
        Set<Street> streets = regionDAO.list(PathHelper.buildPath(country, state, suburb));
        return StreetTableFactory.createTable(streets);
    }

    public RegionGroup listStreetsAsGroup(String country, String state, String suburb) {
        Set<Street> streetstreets = listStreets(country, state, suburb);
        return RegionGroupFactory.createStreets(streetstreets);
    }

    public Set<Street> listStreets(String country, String state, String suburb) {
        return regionDAO.list(PathHelper.buildPath(country, state, suburb));
    }

    public Set<Street> listStreets(Country country) {
        return ((StreetEAO) regionDAO).listStreetsInCountry(PathHelper.buildPath(country));
    }

    public Set<Street> listStreets(Suburb suburb) {
        return regionDAO.list(suburb.getPath());
    }

    /**
     * Find a street in the specified country
     * <p/>
     * <p/>
     * Performs a fuzzy match and returns the matches in order of rank
     */
    public List<Street> find(String name, String countryCode) {
        Set<Street> streets = ((StreetEAO) regionDAO).listStreetsInCountry(PathHelper.buildPath(countryCode));
        return LevensteinDistanceTools.matchName(name, streets);
    }

    public List<Street> find(String name, final StreetType streetType, final StreetSection streetSection, Suburb suburb) {        
        Set<Street> streets = regionDAO.list(suburb.getPath());
        List<Street> filteredStreets = FilterTools.getMatching(streets, new Filter<Street>() {
            public boolean accept(Street street) {
                return ((streetType == null || streetType.equals(street.getStreetType())) && (streetSection == null || streetSection == StreetSection.NA || streetSection.equals(street.getSection())));
            }
        });
        return LevensteinDistanceTools.matchName(name, filteredStreets);
    }


    /**
     * Create a new Street
     * Propagates the change into the RegionGraph as well
     *
     * NOTE: Does not rollback the transaction in the case of a DuplicateRegionException as no write occurs
     * @param street
     */
    @Transactional(exceptOn = DuplicateRegionException.class)
    public Street create(Street street) throws DuplicateRegionException, InvalidRegionException {
        street.populateAttributes();
        Street existing = regionDAO.lookup(street.getPath());
        if (existing == null) {

            Suburb suburb = street.getSuburb();
            if (suburb == null) {
                // see if the parent path references a suburb
                if (StringUtils.isNotBlank(street.getParentPath())) {
                    suburb = suburbService.lookup(street.getParentPath());
                    if (suburb != null) {
                        street.setSuburb(suburb);
                    }
                }
            }

            if (suburb != null) {
                em.persist(suburb);
                em.persist(street);
            } else {
                throw new InvalidRegionException("Invalid parent region (suburb)", street);
            }

        } else {
            throw new DuplicateRegionException(street);
        }
        return street;
    }

    /**
     * Update an existing street
     * Propagates the change into the RegionGraph as well
     *
     * NOTE: Does not rollback the transaction in the case of a DuplicateRegionException as no write occurs
     */
    @Transactional(exceptOn = {InvalidRegionException.class})
    public Street update(String path, Street street) throws InvalidRegionException {
        street.populateAttributes();
        Street existing = regionDAO.lookup(path);
        if (existing != null) {
            existing.mergeWith(street);
            em.persist(existing);
            return existing;
        } else {
            throw new InvalidRegionException(street);
        }
    }


    public Street lookup(String country, String state, String suburb, String street) {
        return regionDAO.lookup(PathHelper.buildPath(country, state, suburb, street));
    }

    public Street lookup(String path) {
        return regionDAO.lookup(path);
    }

    @Inject
    public void setSuburbService(SuburbService suburbService) {
        this.suburbService = suburbService;
    }

    @Inject
    public void setRegionDAO(StreetEAO regionDAO) {
        this.regionDAO = regionDAO;
    }
}