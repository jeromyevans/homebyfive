package com.blueskyminds.housepad.core.region.model;

import com.blueskyminds.framework.test.JPATestCase;
import com.blueskyminds.housepad.core.region.eao.SuburbEAO;
import com.blueskyminds.housepad.core.region.eao.SuburbEAOImpl;
import com.blueskyminds.housepad.core.region.model.SuburbBean;
import com.blueskyminds.housepad.core.region.model.CountryBean;
import com.blueskyminds.housepad.core.region.model.PostCodeBean;

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
        suburbEAO = new SuburbEAOImpl(em);

        CountryBean au = new CountryBean("Australia", "AU");
        em.persist(au);
        CountryBean us = new CountryBean("United States of America", "US");
        em.persist(us);
        StateBean nsw = new StateBean(au, "New South Wales", "NSW");
        em.persist(nsw);
        PostCodeBean postCode2089 = new PostCodeBean(au, nsw, "2089");
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