package com.blueskyminds.homebyfive.business.region.dao;

import com.blueskyminds.homebyfive.framework.core.persistence.jpa.dao.AbstractDAO;
import com.blueskyminds.homebyfive.business.tag.Tag;
import com.blueskyminds.homebyfive.business.region.graph.Region;

import javax.persistence.Query;
import javax.persistence.EntityManager;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Date Started: 7/11/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public abstract class AbstractRegionDAO<T> extends AbstractDAO<T> {

    private static final Log LOG = LogFactory.getLog(AbstractRegionDAO.class);

    private static final String QUERY_ALL_TAGS = "region.tags.byPath";
    private static final String PARAM_PATH = "path";
    private static final String DELETE_BY_PATH = "region.deleteByPath";
    private static final String PARAM_TAG_NAME = "tagName";
    private static final String QUERY_BY_TAG = "region.byTag";
    private static final String QUERY_LIST_BY_PARENT = "regions.byParent";

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

    public abstract T lookup(String path);

    public abstract Set<T> list(String parentPath);


}
