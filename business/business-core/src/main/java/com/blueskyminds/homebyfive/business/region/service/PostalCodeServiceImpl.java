package com.blueskyminds.homebyfive.business.region.service;

import com.wideplay.warp.persist.Transactional;
import com.blueskyminds.homebyfive.business.region.graph.PostalCode;
import com.blueskyminds.homebyfive.business.region.graph.State;
import com.blueskyminds.homebyfive.business.region.PathHelper;
import com.blueskyminds.homebyfive.business.region.PostCodeTableFactory;
import com.blueskyminds.homebyfive.business.region.dao.SuburbEAO;
import com.blueskyminds.homebyfive.business.region.dao.PostCodeEAO;
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
public class PostalCodeServiceImpl extends CommonRegionServices<PostalCode> implements PostalCodeService {

    private StateService stateService;

    public PostalCodeServiceImpl(EntityManager em, TagService tagService, StateService stateService, PostCodeEAO regionDAO) {
        super(em, regionDAO, tagService);
        this.stateService = stateService;
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
        PostalCode existing = regionDAO.lookup(postalCode.getPath());
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
        PostalCode existing = regionDAO.lookup(path);
        if (existing != null) {
            existing.mergeWith(postalCode);
            em.persist(existing);
            return existing;
        } else {
            throw new InvalidRegionException(postalCode);
        }
    }


    public RegionGroup list(String parentPath) {
        Set<PostalCode> postCodes = regionDAO.list(parentPath);
        return RegionGroupFactory.createPostCodes(postCodes);
    }



    public RegionGroup listPostCodesAsGroup(String country, String state) {
       Set<PostalCode> postCodes = listPostCodes(country, state);
       return RegionGroupFactory.createPostCodes(postCodes);
    }

    public Set<PostalCode> listPostCodes(String country, String state) {
       return regionDAO.list(PathHelper.buildPath(country, state));
    }

    public TableModel listPostCodesAsTable(String country, String state) {
       Set<PostalCode> postCodes = regionDAO.list(PathHelper.buildPath(country, state));
       return PostCodeTableFactory.createTable(postCodes);
   }
    
    public PostalCode lookup(String country, String state, String postCode) {
        return regionDAO.lookup(PathHelper.buildPath(country, state, postCode));
    }

    public PostalCode lookup(String path) {
        return regionDAO.lookup(path);
    }

    @Inject
    public void setStateService(StateService stateService) {
        this.stateService = stateService;
    }

    @Inject
    public void setRegionDAO(PostCodeEAO regionDAO) {
        this.regionDAO = regionDAO;
    }

}
