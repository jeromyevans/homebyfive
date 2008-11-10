package com.blueskyminds.homebyfive.business.region.dao;

import com.blueskyminds.homebyfive.framework.core.persistence.jpa.dao.AbstractDAO;
import com.blueskyminds.homebyfive.business.region.graph.Country;
import com.blueskyminds.homebyfive.business.tag.Tag;
import com.google.inject.Inject;

import javax.persistence.Query;
import javax.persistence.EntityManager;
import java.util.Set;

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

    @Inject
    public CountryEAO(EntityManager entityManager) {
        super(entityManager, Country.class);
    }

    /**
     * Get a list of all countries
     * @return
     * @param parentPath
     */
    public Set<Country> list(String parentPath) {
        Query query = em.createNamedQuery(QUERY_ALL_COUNTRIES);
        return setOf(query.getResultList());
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


}
