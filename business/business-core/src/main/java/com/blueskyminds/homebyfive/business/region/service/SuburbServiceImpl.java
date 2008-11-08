package com.blueskyminds.homebyfive.business.region.service;

import com.wideplay.warp.persist.Transactional;
import com.blueskyminds.homebyfive.business.region.graph.Suburb;
import com.blueskyminds.homebyfive.business.region.graph.State;
import com.blueskyminds.homebyfive.business.region.PathHelper;
import com.blueskyminds.homebyfive.business.region.SuburbTableFactory;
import com.blueskyminds.homebyfive.business.region.group.RegionGroup;
import com.blueskyminds.homebyfive.business.region.group.RegionGroupFactory;
import com.blueskyminds.homebyfive.business.region.dao.SuburbEAO;
import com.blueskyminds.homebyfive.business.region.dao.AbstractRegionDAO;
import com.blueskyminds.homebyfive.business.tag.Tag;
import com.blueskyminds.homebyfive.business.tag.TagsTableFactory;
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
public class SuburbServiceImpl extends CommonRegionServices<Suburb> implements SuburbService {

    private StateService stateService;
    private SuburbEAO suburbEAO;

    public SuburbServiceImpl(EntityManager em, TagService tagService, StateService stateService, SuburbEAO suburbEAO) {
        super(em, suburbEAO, tagService);
        this.stateService = stateService;
        this.suburbEAO = suburbEAO;
    }

    public SuburbServiceImpl() {
    }

    public RegionGroup list(String parentPath) {
        Set<Suburb> suburbs = suburbEAO.listSuburbs(parentPath);
        return RegionGroupFactory.createSuburbs(suburbs);
    }

    public RegionGroup list(String country, String state) {
        Set<Suburb> suburbs = suburbEAO.listSuburbs(PathHelper.buildPath(country, state));
        return RegionGroupFactory.createSuburbs(suburbs);
    }

    public TableModel listSuburbsAsTable(String country, String state) {
        Set<Suburb> suburbs = suburbEAO.listSuburbs(PathHelper.buildPath(country, state));
        return SuburbTableFactory.createTable(suburbs);
    }

    /**
     * Create a new suburb
     * Propagates the change into the RegionGraph as well
     *
     * NOTE: Does not rollback the transaction in the case of a DuplicateRegionException as no write occurs
     * @param suburb
     */
    @Transactional(exceptOn = DuplicateRegionException.class)
    public Suburb create(Suburb suburb) throws DuplicateRegionException, InvalidRegionException {
        suburb.populateAttributes();
        Suburb existing = suburbEAO.lookupSuburb(suburb.getPath());
        if (existing == null) {

            State state = suburb.getState();
            if (state == null) {
                // see if the parent path references a state
                if (StringUtils.isNotBlank(suburb.getParentPath())) {
                    state = stateService.lookup(suburb.getParentPath());
                    if (state != null) {
                        suburb.setState(state);
                    }
                }
            }

            if (state != null) {
                em.persist(state);
                em.persist(suburb);
            } else {
                throw new InvalidRegionException("Invalid parent region (state)", suburb);
            }

//            suburb = addressService.createSuburb(suburb.getName(), suburb.getState());
        } else {
            throw new DuplicateRegionException(suburb);
        }
        return suburb;
    }

    /**
     * Update an existing suburb
     * Propagates the change into the RegionGraph as well
     *
     * NOTE: Does not rollback the transaction in the case of a DuplicateRegionException as no write occurs
     */
    @Transactional(exceptOn = {InvalidRegionException.class})
    public Suburb update(String path, Suburb suburb) throws InvalidRegionException {
        suburb.populateAttributes();
        Suburb existing = suburbEAO.lookupSuburb(path);
        if (existing != null) {
            existing.mergeWith(suburb);
            em.persist(existing);
            return existing;
        } else {
            throw new InvalidRegionException(suburb);
        }
    }


    public Suburb lookup(String country, String state, String suburb) {
        return suburbEAO.lookupSuburb(PathHelper.buildPath(country, state, suburb));
    }

    public Suburb lookup(String path) {
        return suburbEAO.lookupSuburb(path);
    }

    @Inject
    public void setStateService(StateService stateService) {
        this.stateService = stateService;
    }

    @Inject
    public void setSuburbEAO(SuburbEAO suburbEAO) {
        this.suburbEAO = suburbEAO;
    }  
}
