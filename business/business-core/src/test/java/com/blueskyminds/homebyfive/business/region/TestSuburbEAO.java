package com.blueskyminds.homebyfive.business.region;

import com.blueskyminds.homebyfive.framework.core.test.JPATestCase;
import com.blueskyminds.homebyfive.business.region.dao.SuburbEAO;
import com.blueskyminds.homebyfive.business.region.graph.Suburb;
import com.blueskyminds.homebyfive.business.region.graph.PostalCode;
import com.blueskyminds.homebyfive.business.region.graph.Country;
import com.blueskyminds.homebyfive.business.region.graph.State;

import java.util.Set;

/**
 * Date Started: 3/03/2008
 * <p/>
 * History:
 */
public class TestSuburbEAO extends JPATestCase {

    private static final String PERSISTENCE_UNIT = "TestEnterprisePersistenceUnit";

    private SuburbEAO suburbEAO;
    private static final String AU = "au";

    public TestSuburbEAO() {
        super(PERSISTENCE_UNIT);
    }

    protected void setUp() throws Exception {
        super.setUp();
        suburbEAO = new SuburbEAO(em);

        Country au = new Country("Australia", "AU");
        em.persist(au);
        em.flush();
        Country us = new Country("United States of America", "US");
        em.persist(us);
        State nsw = new State(au, "New South Wales", "NSW");
        au.addState(nsw);
        em.persist(au);

        PostalCode postCode2089 = new PostalCode(nsw, "2089");
        nsw.addPostCode(postCode2089);
        em.persist(nsw);
        Suburb neutralBay = new Suburb(nsw, postCode2089, "Neutral Bay");
        nsw.addSuburb(neutralBay);
        postCode2089.addSuburb(neutralBay);
        em.persist(nsw);
    }

    public void testLookupSuburb() {
        Suburb suburbBean = suburbEAO.lookup("/au/nsw/neutral+bay");
        assertNotNull(suburbBean);
    }

    public void testListSuburbsByPostCode() {
        Set<Suburb> suburbBeans = suburbEAO.listSuburbsInPostCode("/au/nsw/2089");
        assertEquals(1, suburbBeans.size());
    }
}