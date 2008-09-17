package com.blueskyminds.housepad.core.region.model;

import com.blueskyminds.framework.test.OutOfContainerTestCase;
import com.blueskyminds.housepad.core.region.eao.StateEAO;
import com.blueskyminds.housepad.core.region.eao.StateEAOImpl;
import com.blueskyminds.housepad.core.region.model.CountryBean;
import com.blueskyminds.housepad.core.region.model.StateBean;

/**
 * Date Started: 3/03/2008
 * <p/>
 * History:
 */
public class TesStateEAO extends OutOfContainerTestCase {

    private static final String PERSISTENCE_UNIT = "TestHousePadCorePersistenceUnit";

    private StateEAO stateEAO;
    private static final String AU = "au";

    public TesStateEAO() {
        super(PERSISTENCE_UNIT);
    }

    protected void setUp() throws Exception {
        super.setUp();
        stateEAO = new StateEAOImpl(em);

        CountryBean au = new CountryBean("Australia", "AU");
        em.persist(au);
        CountryBean us = new CountryBean("United States of America", "US");
        em.persist(us);
        StateBean nsw = new StateBean(au, "New South Wales", "NSW");
        em.persist(nsw);
    }

    public void testLookupState() {
        StateBean stateBean = stateEAO.lookupState("/au/nsw");
        assertNotNull(stateBean);
    }
}