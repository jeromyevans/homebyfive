package com.blueskyminds.homebyfive.business.region;

import com.blueskyminds.homebyfive.framework.core.test.JPATestCase;
import com.blueskyminds.homebyfive.business.region.dao.CountryEAO;
import com.blueskyminds.homebyfive.business.region.PathHelper;
import com.blueskyminds.homebyfive.business.region.graph.Country;

import java.util.Set;

/**
 * Date Started: 3/03/2008
 * <p/>
 * History:
 */
public class TestCountryEAO extends JPATestCase {

    private static final String PERSISTENCE_UNIT = "TestEnterprisePersistenceUnit";

    private CountryEAO countryEAO;
    private static final String AU = "au";

    public TestCountryEAO() {
        super(PERSISTENCE_UNIT);
    }

    protected void setUp() throws Exception {
        super.setUp();
        countryEAO = new CountryEAO(em);

        Country au = new Country("Australia", "AU");
        em.persist(au);
        Country us = new Country("United States of America", "US");
        em.persist(us);
    }

    public void testListCountries() {
        Set<Country> countries = countryEAO.list("/");
        assertEquals(2, countries.size());
    }

    public void testLookupCountry() {
        Country countryBean = countryEAO.lookup(PathHelper.buildPath(AU));
        assertNotNull(countryBean);
    }
}
