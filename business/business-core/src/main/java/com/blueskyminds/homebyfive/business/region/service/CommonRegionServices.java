package com.blueskyminds.homebyfive.business.region.service;

import com.blueskyminds.homebyfive.business.region.dao.AbstractRegionDAO;
import com.blueskyminds.homebyfive.business.region.graph.Country;
import com.blueskyminds.homebyfive.business.region.graph.Region;
import com.blueskyminds.homebyfive.business.tag.Tag;
import com.blueskyminds.homebyfive.business.tag.TagsTableFactory;
import com.blueskyminds.homebyfive.business.tag.service.InvalidTagException;
import com.blueskyminds.homebyfive.business.tag.service.TagService;
import com.blueskyminds.homebyfive.framework.core.table.TableModel;
import com.wideplay.warp.persist.Transactional;
import com.google.inject.Inject;

import javax.persistence.EntityManager;
import java.util.Set;

/**
 * Includes methods common to all the different types of region services
 *
 * Date Started: 8/11/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public abstract class CommonRegionServices<R extends Region> implements RegionServiceI<R> {

    protected EntityManager em;
    private AbstractRegionDAO regionDAO;
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

    @Transactional(exceptOn = {InvalidRegionException.class, InvalidTagException.class})
    public void assignTag(String path, String tagName) throws InvalidRegionException, InvalidTagException {
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
