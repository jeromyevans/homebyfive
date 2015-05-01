package com.blueskyminds.homebyfive.business.region.service;

import com.blueskyminds.homebyfive.business.region.dao.AbstractRegionDAO;
import com.blueskyminds.homebyfive.business.region.graph.Region;
import com.blueskyminds.homebyfive.business.region.graph.Country;
import com.blueskyminds.homebyfive.business.tag.Tag;
import com.blueskyminds.homebyfive.business.tag.TagsTableFactory;
import com.blueskyminds.homebyfive.business.tag.expression.TagExpression;
import com.blueskyminds.homebyfive.business.tag.service.TagService;
import com.blueskyminds.homebyfive.business.party.Party;
import com.blueskyminds.homebyfive.framework.core.table.TableModel;
import com.wideplay.warp.persist.Transactional;
import com.google.inject.Inject;

import javax.persistence.EntityManager;
import java.util.Set;
import java.util.Collection;

/**
 * Includes methods common to all the different types of region services
 *
 * Date Started: 8/11/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public abstract class CommonRegionServices<R extends Region> implements RegionServiceI<R> {

    protected EntityManager em;
    protected AbstractRegionDAO<R> regionDAO;
    protected TagService tagService;

    public CommonRegionServices(EntityManager em, AbstractRegionDAO regionDAO, TagService tagService) {
        this.em = em;
        this.regionDAO = regionDAO;
        this.tagService = tagService;
    }

    protected CommonRegionServices() {
    }

    public TableModel listTags(String path) {
        Set<Tag> tags = regionDAO.listTags(path);
        return TagsTableFactory.create(path, tags);
    }

    /**
     * Update an existing region.
     * IF the region is persistent, its simply updated
     * If the region is non-persistent, the existing region is looked up and the changed merged into the existing entity
     *
     * Propagates the change into the RegionGraph as well
     * <p/>
     * NOTE: Does not rollback the transaction in the case of a DuplicateRegionException as no write occurs
     */
    @Transactional(exceptOn = {InvalidRegionException.class})
    public R update(String path, R region) throws InvalidRegionException {
        region.populateAttributes();

        if (region.isIdSet()) {
            // path not changed, simply persist
            em.persist(region);
        } else {
            R existing = regionDAO.lookup(path);
            if (existing != null) {
                existing.mergeWith(region);
                em.persist(existing);
            } else {
                throw new InvalidRegionException(region);
            }
        }
        return region;
    }

    @Transactional(exceptOn = {InvalidRegionException.class})
    public void assignTag(String path, String tagName) throws InvalidRegionException {
        R region = lookup(path);
        if (region != null) {
            Tag tag = tagService.lookupOrCreateTag(tagName);
            if (tag != null) {
                region.addTag(tag);
            }
            em.persist(region);
        } else {
            throw new InvalidRegionException(path);
        }
    }

    @Transactional(exceptOn = {InvalidRegionException.class})
    public void removeTag(String path, String tagName) throws InvalidRegionException {
        R region = lookup(path);
        if (region != null) {
            region.removeTag(tagName);            
            em.persist(region);
        } else {
            throw new InvalidRegionException(path);
        }
    }

    /**
     * List regions with the specified tag
     * @param tagName
     * @return
     */
    public Set<R> listByTag(String tagName) {
        return regionDAO.listByTag(tagName);
    }

    /**
     * List regions matching the TagExpression
     * @param tagExpression
     * @return
     */
    public Set<R> listByTags(TagExpression tagExpression) {
        Set<Tag> tagSuperset = tagExpression.getORSuperset();
        Collection<R> regions = regionDAO.listByTags(tagSuperset);
        return tagExpression.filter(regions);
    }

    /**
     * Delete the country specified by its ID
     * @param path
     */
    public void delete(String path) {
         regionDAO.delete(path);
    }


    @Inject
    public void setTagService(TagService tagService) {
        this.tagService = tagService;
    }

    @Inject
    public void setEm(EntityManager em) {
        this.em = em;
    }
}
