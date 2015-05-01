package com.blueskyminds.homebyfive.business.region.dao;

import com.blueskyminds.homebyfive.business.region.graph.Country;
import com.blueskyminds.homebyfive.business.tag.Tag;
import com.google.inject.Inject;

import javax.persistence.Query;
import javax.persistence.EntityManager;
import java.util.Set;
import java.util.Collection;

/**
 * Date Started: 3/03/2008
 * <p/>
 * History:
 */
public class CountryEAO extends AbstractRegionDAO<Country> {

    private static final String QUERY_ALL_COUNTRIES = "hp.countries";
    private static final String QUERY_COUNTRY_BY_PATH = "hp.country.byPath";
    private static final String PARAM_PATH = "path";
    private static final String QUERY_COUNTRY_BY_HANDLE = "hp.country.byHandle";
    private static final String PARAM_HANDLE = "handle";
    private static final String QUERY_BY_TAGS = "country.byTags";

    @Inject
    public CountryEAO(EntityManager entityManager) {
        super(entityManager, Country.class);
    }

    /**
     * Get a list of all valid countries
     * Parent path is ignored
     * @return
     * @param parentPath
     */
    public Set<Country> list(String parentPath) {
        Query query = em.createNamedQuery(QUERY_ALL_COUNTRIES);  
        return setOf(query.getResultList());
    }

    /**
     * Get a list of all valid countries (same as list(null))
     *
     * @return
     */
    public Set<Country> list() {
        return list(null);
    }


    /**
     * Get an instance of a country matching path  eg. /au
     *
     * @param path
     * @return Country instance, or null if not found
     */
    public Country lookup(String path) {
        Query query = em.createNamedQuery(QUERY_COUNTRY_BY_PATH);
        query.setParameter(PARAM_PATH, path);
        return firstIn(query.getResultList());
    }

    /**
     * @param tags   if non-empty, lists regions with any of these tags.
     * If the set is empty, list all regions are listed
     * @return
     */
    @Override
    public Collection<Country> listByTags(Set<Tag> tags) {
        return super.default_listByTags(QUERY_BY_TAGS, tags);
    }

    /**
     * List regions in the parent path with any of the specified tags.
     * If the set is empty, all regions are listed
     *
     * Parent path is ignored
     */
    @Override
    public Collection<Country> listByTags(String parentPath, Set<Tag> tags) {
        return super.default_listByTags(QUERY_BY_TAGS, tags);
    }
       
}
