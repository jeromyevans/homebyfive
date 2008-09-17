package com.blueskyminds.housepad.core.region.model;

import com.blueskyminds.framework.test.OutOfContainerTestCase;
import com.blueskyminds.housepad.core.region.eao.CountryEAO;
import com.blueskyminds.housepad.core.region.eao.CountryEAOImpl;
import com.blueskyminds.housepad.core.region.model.CountryBean;
import com.blueskyminds.housepad.core.region.PathHelper;

import java.util.Set;

/**
 * Date Started: 3/03/2008
 * <p/>
 * History:
 */
public class TestCountryEAO extends OutOfContainerTestCase {

    private static final String PERSISTENCE_UNIT = "TestHousePadCorePersistenceUnit";

    private CountryEAO countryEAO;
    private static final String AU = "au";

    public TestCountryEAO() {
        super(PERSISTENCE_UNIT);
    }

    protected void setUp() throws Exception {
        super.setUp();
        countryEAO = new CountryEAOImpl(em);

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
