package com.blueskyminds.housepad.core.region.model;

import com.blueskyminds.framework.test.JPATestCase;
import com.blueskyminds.housepad.core.region.eao.PostCodeEAO;
import com.blueskyminds.housepad.core.region.eao.PostCodeEAOImpl;
import com.blueskyminds.housepad.core.region.model.PostCodeBean;
import com.blueskyminds.housepad.core.region.model.StateBean;
import com.blueskyminds.housepad.core.region.model.CountryBean;

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
        postCodeEAO = new PostCodeEAOImpl(em);

        CountryBean au = new CountryBean("Australia", "AU");
        em.persist(au);
        CountryBean us = new CountryBean("United States of America", "US");
        em.persist(us);
        StateBean nsw = new StateBean(au, "New South Wales", "NSW");
        em.persist(nsw);
        PostCodeBean postCode2089 = new PostCodeBean(au, nsw, "2089");
        em.persist(postCode2089);
    }

    public void testLookupSuburb() {
        PostCodeBean postCodeBean = postCodeEAO.lookupPostCode("/au/nsw/2089");
        assertNotNull(postCodeBean);
    }
}