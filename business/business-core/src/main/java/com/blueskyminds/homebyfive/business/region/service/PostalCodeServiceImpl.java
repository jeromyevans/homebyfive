package com.blueskyminds.homebyfive.business.region.service;

import com.wideplay.warp.persist.Transactional;
import com.blueskyminds.homebyfive.business.region.graph.PostalCode;
import com.blueskyminds.homebyfive.business.region.graph.State;
import com.blueskyminds.homebyfive.business.region.graph.Suburb;
import com.blueskyminds.homebyfive.business.region.PathHelper;
import com.blueskyminds.homebyfive.business.region.PostCodeTableFactory;
import com.blueskyminds.homebyfive.business.region.SuburbTableFactory;
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
public class PostalCodeServiceImpl extends CommonRegionServices<PostalCode> implements PostalCodeService {

    private StateService stateService;
    private PostCodeEAO postCodeEAO;
    private SuburbEAO suburbEAO;

    public PostalCodeServiceImpl(EntityManager em, TagService tagService, StateService stateService, PostCodeEAO postCodeEAO, SuburbEAO suburbEAO) {
        super(em, postCodeEAO, tagService);
        this.stateService = stateService;
        this.postCodeEAO = postCodeEAO;
        this.suburbEAO = suburbEAO;
    }

    public PostalCodeServiceImpl() {
    }

    /**
     * Create a new PostalCode
     * Propagates the change into the RegionGraph as well
     * <p/>
     * NOTE: Does not rollback the transaction in the case of a DuplicateRegionException as no write occurs
     *
     * @param postalCode
     */
    @Transactional(exceptOn = DuplicateRegionException.class)
    public PostalCode create(PostalCode postalCode) throws DuplicateRegionException, InvalidRegionException {
        postalCode.populateAttributes();
        PostalCode existing = postCodeEAO.lookupPostCode(postalCode.getPath());
        if (existing == null) {

            State state = postalCode.getState();
            if (state == null) {
                // see if the parent path references a country
                if (StringUtils.isNotBlank(postalCode.getParentPath())) {
                    state = stateService.lookup(postalCode.getParentPath());
                    if (state != null) {
                        postalCode.setState(state);
                    }
                }
            }
//            postalCode = addressService.createPostCode(postalCode.getName(), postalCode.getState());
            if (state != null) {
                em.persist(state);
                em.persist(postalCode);
            } else {
                throw new InvalidRegionException("Invalid parent", postalCode);
            }
        } else {
            throw new DuplicateRegionException(postalCode);
        }
        return postalCode;
    }

    /**
     * Update an existing postalcode
     * Propagates the change into the RegionGraph as well
     * <p/>
     * NOTE: Does not rollback the transaction in the case of a DuplicateRegionException as no write occurs
     */
    @Transactional(exceptOn = {InvalidRegionException.class})
    public PostalCode update(String path, PostalCode postalCode) throws InvalidRegionException {
        postalCode.populateAttributes();
        PostalCode existing = postCodeEAO.lookupPostCode(path);
        if (existing != null) {
            existing.mergeWith(postalCode);
            em.persist(existing);
            return existing;
        } else {
            throw new InvalidRegionException(postalCode);
        }
    }


    public RegionGroup list(String parentPath) {
        Set<PostalCode> postCodes = postCodeEAO.listPostCodes(parentPath);
        return RegionGroupFactory.createPostCodes(postCodes);
    }

   
    public PostalCode lookup(String country, String state, String postCode) {
        return postCodeEAO.lookupPostCode(PathHelper.buildPath(country, state, postCode));
    }

    public PostalCode lookup(String path) {
        return postCodeEAO.lookupPostCode(path);
    }

    public RegionGroup listSuburbs(String country, String state, String postCode) {
        Set<Suburb> suburbs = suburbEAO.listSuburbsInPostCode(PathHelper.buildPath(country, state, postCode));
        return RegionGroupFactory.createSuburbs(suburbs);
    }

    public TableModel listSuburbsAsTable(String country, String state, String postCode) {
        Set<Suburb> suburbs = suburbEAO.listSuburbsInPostCode(PathHelper.buildPath(country, state, postCode));
        return SuburbTableFactory.createTable(suburbs);
    }

    @Inject
    public void setStateService(StateService stateService) {
        this.stateService = stateService;
    }

    @Inject
    public void setPostCodeEAO(PostCodeEAO postCodeEAO) {
        this.postCodeEAO = postCodeEAO;
    }

    @Inject
    public void setSuburbEAO(SuburbEAO suburbEAO) {
        this.suburbEAO = suburbEAO;
    }    
}
