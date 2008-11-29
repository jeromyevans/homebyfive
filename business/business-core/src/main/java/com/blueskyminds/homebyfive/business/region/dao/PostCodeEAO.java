package com.blueskyminds.homebyfive.business.region.dao;

import com.blueskyminds.homebyfive.business.region.graph.PostalCode;
import com.google.inject.Inject;

import javax.persistence.Query;
import javax.persistence.EntityManager;
import java.util.Set;

/**
 * Date Started: 5/03/2008
 * <p/>
 * History:
 */
public class PostCodeEAO extends AbstractRegionDAO<PostalCode> {

    private static final String QUERY_ALL_POSTCODES_BY_PARENT_PATH = "hp.postCodes.byParentPath";
    private static final String QUERY_POSTCODE_BY_PATH = "hp.postCode.byPath";
    private static final String PARAM_PATH = "path";
    private static final String QUERY_ALL_POSTCODES_BY_COUNTRY = "hp.postCodes.byCountryPath";
    private static final String PARAM_PARENT_PATH = "parentPath";

    @Inject  
    public PostCodeEAO(EntityManager entityManager) {
        super(entityManager, PostalCode.class);
    }

    /**
     * Get a list of all the postcodes in the specified state (eg. /au/nsw)
     *
     * @return PostCodes, or empty set if not found
     */
    public Set<PostalCode> list(String parentPath) {

        Query query = em.createNamedQuery(QUERY_ALL_POSTCODES_BY_PARENT_PATH);
        query.setParameter(PARAM_PATH, parentPath);

        return setOf(query.getResultList());
    }

    /**
     * Get the postcode with the specified path (eg. /au/nsw/2089)
     *
     * @return PostCode, or null if not found
     */
    public PostalCode lookup(String path) {

        Query query = em.createNamedQuery(QUERY_POSTCODE_BY_PATH);
        query.setParameter(PARAM_PATH, path);

        return firstIn(query.getResultList());
    }

/**
     * Get a list of all the postcodes in the specified country (eg. /au)
     * Uses a like wildcard on the parent path
     *
     * @return PostalCodes, or empty set if not found
     */
    public Set<PostalCode> listPostalCodesInCountry(String countryPath) {
        Query query = em.createNamedQuery(QUERY_ALL_POSTCODES_BY_COUNTRY);
        query.setParameter(PARAM_PARENT_PATH, countryPath+"%");
        return setOf(query.getResultList());
    }
}