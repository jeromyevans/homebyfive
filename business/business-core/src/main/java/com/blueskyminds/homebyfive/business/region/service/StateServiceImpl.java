package com.blueskyminds.homebyfive.business.region.service;

import com.wideplay.warp.persist.Transactional;
import com.blueskyminds.homebyfive.business.region.graph.State;
import com.blueskyminds.homebyfive.business.region.graph.Country;
import com.blueskyminds.homebyfive.business.region.graph.Suburb;
import com.blueskyminds.homebyfive.business.region.graph.PostalCode;
import com.blueskyminds.homebyfive.business.region.PathHelper;
import com.blueskyminds.homebyfive.business.region.SuburbTableFactory;
import com.blueskyminds.homebyfive.business.region.PostCodeTableFactory;
import com.blueskyminds.homebyfive.business.region.dao.StateEAO;
import com.blueskyminds.homebyfive.business.region.dao.SuburbEAO;
import com.blueskyminds.homebyfive.business.region.dao.PostCodeEAO;
import com.blueskyminds.homebyfive.business.region.dao.AbstractRegionDAO;
import com.blueskyminds.homebyfive.business.region.group.RegionGroup;
import com.blueskyminds.homebyfive.business.region.group.RegionGroupFactory;
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
public class StateServiceImpl extends CommonRegionServices<State> implements StateService {

    private CountryService countryService;
    private StateEAO stateEAO;
    private SuburbEAO suburbEAO;
    private PostCodeEAO postCodeEAO;

    public StateServiceImpl(EntityManager em, TagService tagService, CountryService countryService, StateEAO stateEAO, SuburbEAO suburbEAO, PostCodeEAO postCodeEAO) {
        super(em, stateEAO, tagService);
        this.countryService = countryService;
        this.stateEAO = stateEAO;
        this.suburbEAO = suburbEAO;
        this.postCodeEAO = postCodeEAO;
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
        State existing = stateEAO.lookupState(state.getPath());
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

        State existing = stateEAO.lookupState(path);
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
        return stateEAO.lookupState(statePath);
    }

    public RegionGroup listSuburbsAsGroup(String country, String state) {
        Set<Suburb> suburbs = listSuburbs(country, state);
        return RegionGroupFactory.createSuburbs(suburbs);
    }

    public RegionGroup list(String parentPath) {
        Set<Suburb> suburbs = suburbEAO.listSuburbs(parentPath);
        return RegionGroupFactory.createSuburbs(suburbs);
    }

    public TableModel listSuburbsAsTable(String country, String state) {
        Set<Suburb> suburbs = listSuburbs(country, state);
        return SuburbTableFactory.createTable(suburbs);
    }

    public Set<Suburb> listSuburbs(String country, String state) {
        return suburbEAO.listSuburbs(PathHelper.buildPath(country, state));
    }

    public RegionGroup listPostCodesAsGroup(String country, String state) {
       Set<PostalCode> postCodes = listPostCodes(country, state);
       return RegionGroupFactory.createPostCodes(postCodes);
    }

   public Set<PostalCode> listPostCodes(String country, String state) {
       return postCodeEAO.listPostCodes(PathHelper.buildPath(country, state));
   }

   public TableModel listPostCodesAsTable(String country, String state) {
       Set<PostalCode> postCodes = postCodeEAO.listPostCodes(PathHelper.buildPath(country, state));
       return PostCodeTableFactory.createTable(postCodes);
   }

    @Inject
    public void setCountryService(CountryService countryService) {
        this.countryService = countryService;
    }

    @Inject
    public void setStateEAO(StateEAO stateEAO) {
        this.stateEAO = stateEAO;
    }

    @Inject
    public void setSuburbEAO(SuburbEAO suburbEAO) {
        this.suburbEAO = suburbEAO;
    }

    @Inject
    public void setPostCodeEAO(PostCodeEAO postCodeEAO) {
        this.postCodeEAO = postCodeEAO;
    }

}
