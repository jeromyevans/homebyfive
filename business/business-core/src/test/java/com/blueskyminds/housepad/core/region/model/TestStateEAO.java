package com.blueskyminds.housepad.core.region.model;

import com.blueskyminds.homebyfive.framework.core.test.JPATestCase;
import com.blueskyminds.enterprise.region.dao.StateEAO;
import com.blueskyminds.enterprise.region.index.CountryBean;
import com.blueskyminds.enterprise.region.index.StateBean;
import com.blueskyminds.enterprise.region.graph.State;
import com.blueskyminds.enterprise.region.graph.Country;

/**
 * Date Started: 3/03/2008
 * <p/>
 * History:
 */
public class TestStateEAO extends JPATestCase {

    private static final String PERSISTENCE_UNIT = "TestEnterprisePersistenceUnit";

    private StateEAO stateEAO;
    private static final String AU = "au";

    public TestStateEAO() {
        super(PERSISTENCE_UNIT);
    }

    protected void setUp() throws Exception {
        super.setUp();
        stateEAO = new StateEAO(em);

        Country au = new Country("Australia", "AU");
        em.persist(au);
        Country us = new Country("United States of America", "US");
        em.persist(us);
        State nsw = new State(au, "New South Wales", "NSW");
        em.persist(nsw);
    }

    public void testLookupState() {
        State stateBean = stateEAO.lookupState("/au/nsw");
        assertNotNull(stateBean);
    }
}