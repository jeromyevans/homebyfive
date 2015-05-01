package com.blueskyminds.homebyfive.business.region.dao;

import com.blueskyminds.homebyfive.framework.core.persistence.jpa.dao.AbstractDAO;
import com.blueskyminds.homebyfive.business.tag.Tag;
import com.blueskyminds.homebyfive.business.tag.expression.TagExpression;
import com.blueskyminds.homebyfive.business.tag.service.TaggableSearchDAO;

import javax.persistence.Query;
import javax.persistence.EntityManager;
import java.util.Set;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Date Started: 7/11/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public abstract class AbstractRegionDAO<T> extends AbstractDAO<T> implements TaggableSearchDAO<T> {

    private static final Log LOG = LogFactory.getLog(AbstractRegionDAO.class);

    private static final String QUERY_ALL_TAGS = "region.tags.byPath";
    private static final String PARAM_PATH = "path";
    private static final String DELETE_BY_PATH = "region.deleteByPath";
    private static final String PARAM_TAG_NAME = "tagName";
    private static final String QUERY_BY_TAG = "region.byTag";
    private static final String QUERY_LIST_BY_PARENT = "regions.byParent";

    protected static final String PARAM_TAGS = "tags";

    public AbstractRegionDAO(EntityManager em, Class<T> defaultClass) {
        super(em, defaultClass);
    }

    public Set<Tag> listTags(String path) {
        Query query = em.createNamedQuery(QUERY_ALL_TAGS);
        query.setParameter(PARAM_PATH, path);
        return setOfX(query.getResultList());
    }

    public void delete(String path) {
        Query query = em.createNamedQuery(DELETE_BY_PATH);
        query.setParameter(PARAM_PATH, path);
        query.executeUpdate();
    }

    public Set<T> listByTag(String tagName) {
        Query query = em.createNamedQuery(QUERY_BY_TAG);
        query.setParameter(PARAM_TAG_NAME, tagName);
        return setOf(query.getResultList());
    }

    /** List all valid regions */
    public abstract Set<T> list();

    /**
     * @param tags   if non-empty, lists regions with any of these tags.
     * If the set is empty, list all regions are listed
     * @return
     */
    public abstract Collection<T> listByTags(Set<Tag> tags);


    /**
     * List regions in the parent path with any of the specified tags.
     * If the set is emply, all regions are listed
     */
    public abstract Collection<T> listByTags(String parentPath, Set<Tag> tags);

    /**
     * A common implementation of the listByTags method that uses the named query
     *
     * @param queryName  the name of the query to invoke
     * @param tags   if non-empty, lists regions with any of these tags.  If empty, list all parties
     * @return
     */
    protected Collection<T> default_listByTags(String queryName, Set<Tag> tags) {
        Collection<T> regions;

        if (tags.size() > 0) {
            Query query = em.createNamedQuery(queryName);
            query.setParameter(PARAM_TAGS, tags);

            regions = query.getResultList();
        } else {
            regions = list();
        }

        return regions;
    }

    /**
     * A common implementation of listByTags method
     *
     * @param queryName   the name of the query to invoke
     * @param parentPath
     * @param tags
     * @return
     */
    protected Collection<T> default_listByTags(String queryName, String parentPath, Set<Tag> tags) {
        Collection<T> regions;

        if (tags.size() > 0) {
            Query query = em.createNamedQuery(queryName);
            query.setParameter(PARAM_TAGS, tags);
            query.setParameter(PARAM_PATH, parentPath);

            regions = query.getResultList();
        } else {
            regions = list(parentPath);
        }

        return regions;
    }

    public abstract T lookup(String path);

    public abstract Set<T> list(String parentPath);


}
