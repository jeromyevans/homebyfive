package com.blueskyminds.homebyfive.business.region.dao;

import com.blueskyminds.homebyfive.business.region.graph.PostalCode;
import com.blueskyminds.homebyfive.business.tag.Tag;
import com.google.inject.Inject;

import javax.persistence.Query;
import javax.persistence.EntityManager;
import java.util.Set;
import java.util.Collection;

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
    private static final String QUERY_ALL_POSTCODES = "postCode.all";

    private static final String QUERY_BY_TAGS = "postCode.byTags";
    private static final String QUERY_BY_PARENTPATH_AND_TAGS = "postCode.byParentPathAndTags";

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


    public Set<PostalCode> list() {
        Query query = em.createNamedQuery(QUERY_ALL_POSTCODES);
        return setOf(query.getResultList());
    }

     /**
     * @param tags   if non-empty, lists regions with any of these tags.
     * If the set is empty, list all regions are listed
     * @return
     */
    @Override
    public Collection<PostalCode> listByTags(Set<Tag> tags) {
        return super.default_listByTags(QUERY_BY_TAGS, tags);
    }

    /**
     * List regions in the parent path with any of the specified tags.
     * If the set is emply, all regions are listed
     */
    @Override
    public Collection<PostalCode> listByTags(String parentPath, Set<Tag> tags) {
        return super.default_listByTags(QUERY_BY_PARENTPATH_AND_TAGS, parentPath, tags);
    }
}