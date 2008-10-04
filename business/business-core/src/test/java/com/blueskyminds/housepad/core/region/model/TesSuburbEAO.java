package com.blueskyminds.housepad.core.region.model;

import com.blueskyminds.homebyfive.framework.core.test.JPATestCase;
import com.blueskyminds.enterprise.region.dao.SuburbEAO;
import com.blueskyminds.enterprise.region.index.SuburbBean;
import com.blueskyminds.enterprise.region.index.StateBean;
import com.blueskyminds.enterprise.region.index.CountryBean;
import com.blueskyminds.enterprise.region.index.PostalCodeBean;

import java.util.Set;

/**
 * Date Started: 3/03/2008
 * <p/>
 * History:
 */
public class TesSuburbEAO extends JPATestCase {

    private static final String PERSISTENCE_UNIT = "TestHousePadCorePersistenceUnit";

    private SuburbEAO suburbEAO;
    private static final String AU = "au";

    public TesSuburbEAO() {
        super(PERSISTENCE_UNIT);
    }

    protected void setUp() throws Exception {
        super.setUp();
        suburbEAO = new SuburbEAO(em);

        CountryBean au = new CountryBean("Australia", "AU");
        em.persist(au);
        CountryBean us = new CountryBean("United States of America", "US");
        em.persist(us);
        StateBean nsw = new StateBean(au, "New South Wales", "NSW");
        em.persist(nsw);
        PostalCodeBean postCode2089 = new PostalCodeBean(au, nsw, "2089");
        em.persist(postCode2089);
        SuburbBean neutralBay = new SuburbBean(au, nsw, postCode2089, "Neutral Bay");
        em.persist(neutralBay);
    }

    public void testLookupSuburb() {
        SuburbBean suburbBean = suburbEAO.lookupSuburb("/au/nsw/neutral+bay");
        assertNotNull(suburbBean);
    }

    public void testListSuburbsByPostCode() {
        Set<SuburbBean> suburbBeans = suburbEAO.listSuburbsInPostCode("/au/nsw/2089");
        assertEquals(1, suburbBeans.size());
    }
}