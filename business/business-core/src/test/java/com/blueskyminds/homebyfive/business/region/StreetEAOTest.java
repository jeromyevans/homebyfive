package com.blueskyminds.homebyfive.business.region;

import com.blueskyminds.homebyfive.framework.core.test.JPATestCase;
import com.blueskyminds.homebyfive.business.region.dao.SuburbEAO;
import com.blueskyminds.homebyfive.business.region.dao.StreetEAO;
import com.blueskyminds.homebyfive.business.region.graph.*;
import com.blueskyminds.homebyfive.business.tag.Tag;
import com.blueskyminds.homebyfive.business.address.StreetType;

import java.util.Set;
import java.util.Collection;
import java.util.HashSet;

/**
 * Date Started: 20/03/2009
 * <p/>
 * History:
 */
public class StreetEAOTest extends JPATestCase {

    private static final String PERSISTENCE_UNIT = "TestEnterprisePersistenceUnit";

    private StreetEAO streetEAO;
    private static final String AU = "au";

    private Tag testTag;
    private Set<Tag> testTags;

    public StreetEAOTest() {
        super(PERSISTENCE_UNIT);
    }

    protected void setUp() throws Exception {
        super.setUp();
        streetEAO = new StreetEAO(em);

        testTag = new Tag("test");
        em.persist(testTag);
        testTags = new HashSet<Tag>();
        testTags.add(testTag);

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
        Street sprusonStreet = new Street(neutralBay, "Spruson", StreetType.Street).withTag(testTag);
        em.persist(nsw);
        em.persist(neutralBay);
    }

    public void testLookupStreet() {
        Street street = streetEAO.lookup("/au/nsw/neutral+bay/spruson+street");
        assertNotNull(street);
    }

    public void testListByTags() {
        Collection<Street> streets = streetEAO.listByTags(testTags);
        assertEquals(1, streets.size());
    }

    public void testListByPathAndTags() {
        Collection<Street> streets = streetEAO.listByTags("/au/nsw/neutral+bay", testTags);
        assertEquals(1, streets.size());
    }

}