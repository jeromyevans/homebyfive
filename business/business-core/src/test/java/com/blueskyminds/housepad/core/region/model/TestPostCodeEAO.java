package com.blueskyminds.housepad.core.region.model;

import com.blueskyminds.homebyfive.framework.core.test.JPATestCase;
import com.blueskyminds.enterprise.region.dao.PostCodeEAO;
import com.blueskyminds.enterprise.region.index.PostalCodeBean;
import com.blueskyminds.enterprise.region.index.StateBean;
import com.blueskyminds.enterprise.region.index.CountryBean;

/**
 * Date Started: 3/03/2008
 * <p/>
 * History:
 */
public class TestPostCodeEAO extends JPATestCase {

    private static final String PERSISTENCE_UNIT = "TestHousePadCorePersistenceUnit";

    private PostCodeEAO postCodeEAO;
    private static final String AU = "au";

    public TestPostCodeEAO() {
        super(PERSISTENCE_UNIT);
    }

    protected void setUp() throws Exception {
        super.setUp();
        postCodeEAO = new PostCodeEAO(em);

        CountryBean au = new CountryBean("Australia", "AU");
        em.persist(au);
        CountryBean us = new CountryBean("United States of America", "US");
        em.persist(us);
        StateBean nsw = new StateBean(au, "New South Wales", "NSW");
        em.persist(nsw);
        PostalCodeBean postCode2089 = new PostalCodeBean(au, nsw, "2089");
        em.persist(postCode2089);
    }

    public void testLookupSuburb() {
        PostalCodeBean postCodeBean = postCodeEAO.lookupPostCode("/au/nsw/2089");
        assertNotNull(postCodeBean);
    }
}