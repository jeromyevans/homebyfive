package com.blueskyminds.housepad.core.region.eao;

import com.blueskyminds.framework.persistence.jpa.dao.AbstractDAO;
import com.blueskyminds.housepad.core.region.model.CountryBean;
import com.blueskyminds.enterprise.region.country.CountryHandle;
import com.google.inject.Inject;

import javax.persistence.Query;
import javax.persistence.EntityManager;
import java.util.Set;

/**
 * Date Started: 3/03/2008
 * <p/>
 * History:
 */
public class CountryEAOImpl extends AbstractDAO<CountryBean> implements CountryEAO {

    private static final String QUERY_ALL_COUNTRIES = "hp.countries";
    private static final String QUERY_COUNTRY_BY_PATH = "hp.country.byPath";
    private static final String PARAM_PATH = "path";
    private static final String QUERY_COUNTRY_BY_HANDLE = "hp.country.byHandle";
    private static final String PARAM_HANDLE = "handle";

    @Inject
    public CountryEAOImpl(EntityManager entityManager) {
        super(entityManager, CountryBean.class);
    }

    /**
     * Get a list of all countries
     * @return
     */
    public Set<CountryBean> listCountries() {
        Query query = em.createNamedQuery(QUERY_ALL_COUNTRIES);
        return setOf(query.getResultList());
    }

    /**
     * Get an instance of a country matching path  eg. /au
     *
     * @param path
     * @return Country instance, or null if not found
     */
    public CountryBean lookupCountry(String path) {
        Query query = em.createNamedQuery(QUERY_COUNTRY_BY_PATH);
        query.setParameter(PARAM_PATH, path);
        return firstIn(query.getResultList());
    }

       /**
     * Get the country by its handle
     *
     * @return CountryBean, or null if not found
     */
    public CountryBean lookupCountry(CountryHandle countryHandle) {

        Query query = em.createNamedQuery(QUERY_COUNTRY_BY_HANDLE);
        query.setParameter(PARAM_HANDLE, countryHandle);

        return firstIn(query.getResultList());
    }
}
