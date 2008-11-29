package com.blueskyminds.homebyfive.business.region.dao;

import com.blueskyminds.homebyfive.framework.core.persistence.jpa.dao.AbstractDAO;
import com.blueskyminds.homebyfive.business.region.graph.Suburb;
import com.google.inject.Inject;

import javax.persistence.Query;
import javax.persistence.EntityManager;
import java.util.Set;

/**
 * Date Started: 3/03/2008
 * <p/>
 * History:
 */
public class SuburbEAO extends AbstractRegionDAO<Suburb> {

    private static final String QUERY_ALL_SUBURBS_BY_PARENT_PATH = "hp.suburbs.byParentPath";
    private static final String QUERY_ALL_SUBURBS_BY_POSTCODE = "hp.suburbs.byPostCode";
    private static final String QUERY_SUBURB_BY_PATH = "hp.suburb.byPath";
    private static final String PARAM_PATH = "path";
    private static final String PARAM_POSTCODE_PATH = "postCode";
    private static final String QUERY_SUBURB_BY_HANDLE = "hp.suburb.byHandle";
    private static final String PARAM_HANDLE = "handle";
    private static final String PARAM_PARENT_PATH = "parentPath";
    private static final String QUERY_ALL_SUBURBS_BY_COUNTRY = "hp.suburbs.byCountryPath";

    @Inject   
    public SuburbEAO(EntityManager entityManager) {
        super(entityManager, Suburb.class);
    }

     /**
     * Get a list of all the suburbs in the specified state (eg. /au/nsw)
     *
     * @return Suburbs, or empty set if not found
     */
    public Set<Suburb> list(String parentPath) {

        Query query = em.createNamedQuery(QUERY_ALL_SUBURBS_BY_PARENT_PATH);
        query.setParameter(PARAM_PATH, parentPath);
        return setOf(query.getResultList());
    }

     /**
     * Get a list of all the suburbs in the specified postcode (eg. /au/nsw)
     *
     * @return Suburbs, or empty set if not found
     */
    public Set<Suburb> listSuburbsInPostCode(String postCodePath) {

        Query query = em.createNamedQuery(QUERY_ALL_SUBURBS_BY_POSTCODE);
        query.setParameter(PARAM_POSTCODE_PATH, postCodePath);
        return setOf(query.getResultList());
    }

    /**
     * Get the suburb with the specified path (eg. /au/nsw/neutral+bay)
     *
     * @return State, or null if not found
     */
    public Suburb lookup(String path) {

        Query query = em.createNamedQuery(QUERY_SUBURB_BY_PATH);
        query.setParameter(PARAM_PATH, path);

        return firstIn(query.getResultList());
    }

    /**
     * Get a list of all the suburbs in the specified country (eg. /au)
     * Uses a like wildcard on the parent path
     * 
     * @return Suburbs, or empty set if not found
     */
    public Set<Suburb> listSuburbsInCountry(String countryPath) {
        Query query = em.createNamedQuery(QUERY_ALL_SUBURBS_BY_COUNTRY);
        query.setParameter(PARAM_PARENT_PATH, countryPath+"%");
        return setOf(query.getResultList());
    }
   
}