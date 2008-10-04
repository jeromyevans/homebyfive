package com.blueskyminds.housepad.core.region.model;

import com.blueskyminds.homebyfive.framework.core.test.JPATestCase;
import com.blueskyminds.enterprise.region.dao.CountryEAO;
import com.blueskyminds.enterprise.region.index.CountryBean;
import com.blueskyminds.enterprise.region.PathHelper;

import java.util.Set;

/**
 * Date Started: 3/03/2008
 * <p/>
 * History:
 */
public class TestCountryEAO extends JPATestCase {

    private static final String PERSISTENCE_UNIT = "TestHousePadCorePersistenceUnit";

    private CountryEAO countryEAO;
    private static final String AU = "au";

    public TestCountryEAO() {
        super(PERSISTENCE_UNIT);
    }

    protected void setUp() throws Exception {
        super.setUp();
        countryEAO = new CountryEAO(em);

        CountryBean au = new CountryBean("Australia", "AU");
        em.persist(au);
        CountryBean us = new CountryBean("United States of America", "US");
        em.persist(us);
    }

    public void testListCountries() {
        Set<CountryBean> countries = countryEAO.listCountries();
        assertEquals(2, countries.size());
    }

    public void testLookupCountry() {
        CountryBean countryBean = countryEAO.lookupCountry(PathHelper.buildPath(AU));
        assertNotNull(countryBean);
    }
}
