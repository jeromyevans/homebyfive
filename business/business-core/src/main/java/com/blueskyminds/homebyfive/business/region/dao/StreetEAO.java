package com.blueskyminds.homebyfive.business.region.dao;

import com.blueskyminds.homebyfive.business.region.graph.Suburb;
import com.blueskyminds.homebyfive.business.region.graph.Street;
import com.blueskyminds.homebyfive.business.region.graph.PostalCode;
import com.blueskyminds.homebyfive.business.tag.Tag;
import com.google.inject.Inject;

import javax.persistence.Query;
import javax.persistence.EntityManager;
import java.util.Set;
import java.util.Collection;

/**
 * Date Started: 20/03/2008
 * <p/>
 * History:
 */
public class StreetEAO extends AbstractRegionDAO<Street> {

    private static final String QUERY_ALL_STREETS_BY_PARENT_PATH = "hp.streets.byParentPath";
    private static final String QUERY_STREET_BY_PATH = "hp.street.byPath";
    private static final String PARAM_PATH = "path";
    private static final String QUERY_STREET_BY_HANDLE = "hp.street.byHandle";
    private static final String PARAM_HANDLE = "handle";
    private static final String QUERY_ALL_STREETS_BY_COUNTRY = "hp.streets.byCountryPath";
    private static final String PARAM_PARENT_PATH = "parentPath";

    private static final String QUERY_ALL_STREETS = "street.all";
    
    private static final String QUERY_BY_TAGS = "street.byTags";
    private static final String QUERY_BY_PARENTPATH_AND_TAGS = "street.byParentPathAndTags";

    @Inject
    public StreetEAO(EntityManager entityManager) {
        super(entityManager, Street.class);
    }

     /**
     * Get a list of all the streets in the specified state (eg. /au/nsw/neutral+bay)
     *
     * @return Suburbs, or empty set if not found
     */
    public Set<Street> list(String parentPath) {

        Query query = em.createNamedQuery(QUERY_ALL_STREETS_BY_PARENT_PATH);
        query.setParameter(PARAM_PATH, parentPath);
        return setOf(query.getResultList());
    }

    /**
     * Get the street with the specified path (eg. /au/nsw/neutral+bay/darley+street)
     *
     * @return SAtreet, or null if not found
     */
    public Street lookup(String path) {

        Query query = em.createNamedQuery(QUERY_STREET_BY_PATH);
        query.setParameter(PARAM_PATH, path);

        return firstIn(query.getResultList());
    }

    /**
     * Get a list of all the streets in the specified country (eg. /au)
     * Uses a like wildcard on the parent path
     *
     * @return Suburbs, or empty set if not found
     */
    public Set<Street> listStreetsInCountry(String countryPath) {
        Query query = em.createNamedQuery(QUERY_ALL_STREETS_BY_COUNTRY);
        query.setParameter(PARAM_PARENT_PATH, countryPath+"%");
        return setOf(query.getResultList());
    }

    public Set<Street> list() {
        Query query = em.createNamedQuery(QUERY_ALL_STREETS);
        return setOf(query.getResultList());
    }

      /**
     * @param tags   if non-empty, lists regions with any of these tags.
     * If the set is empty, list all regions are listed
     * @return
     */
    @Override
    public Collection<Street> listByTags(Set<Tag> tags) {
        return super.default_listByTags(QUERY_BY_TAGS, tags);
    }

    /**
     * List regions in the parent path with any of the specified tags.
     * If the set is emply, all regions are listed
     */
    @Override
    public Collection<Street> listByTags(String parentPath, Set<Tag> tags) {
        return super.default_listByTags(QUERY_BY_PARENTPATH_AND_TAGS, parentPath, tags);
    }
}