package com.blueskyminds.homebyfive.business.region;

import com.blueskyminds.homebyfive.framework.core.test.JPATestCase;
import com.blueskyminds.homebyfive.business.region.dao.StateEAO;
import com.blueskyminds.homebyfive.business.region.graph.State;
import com.blueskyminds.homebyfive.business.region.graph.Country;
import com.blueskyminds.homebyfive.business.tag.Tag;

import java.util.Set;
import java.util.HashSet;
import java.util.Collection;

/**
 * Date Started: 3/03/2008
 * <p/>
 * History:
 */
public class StateEAOTest extends JPATestCase {

    private static final String PERSISTENCE_UNIT = "TestEnterprisePersistenceUnit";

    private StateEAO stateEAO;
    private static final String AU = "au";

    private Tag testTag;
    private Set<Tag> testTags;

    public StateEAOTest() {
        super(PERSISTENCE_UNIT);
    }

    protected void setUp() throws Exception {
        super.setUp();
        stateEAO = new StateEAO(em);

        testTag = new Tag("test");
        em.persist(testTag);
        testTags = new HashSet<Tag>();
        testTags.add(testTag);

        Country au = new Country("Australia", "AU");
        em.persist(au);
        Country us = new Country("United States of America", "US");
        em.persist(us);
        State nsw = new State(au, "New South Wales", "NSW").withTag(testTag);
        em.persist(nsw);
    }

    public void testLookupState() {
        State stateBean = stateEAO.lookup("/au/nsw");
        assertNotNull(stateBean);
    }


    public void testListByTags() {
        Collection<State> countries = stateEAO.listByTags(testTags);
        assertEquals(1, countries.size());
    }

    public void testListByPathAndTags() {
        Collection<State> countries = stateEAO.listByTags("/au", testTags);
        assertEquals(1, countries.size());
    }

}