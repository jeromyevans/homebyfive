package com.blueskyminds.homebyfive.business.region;

import com.blueskyminds.homebyfive.framework.core.test.JPATestCase;
import com.blueskyminds.homebyfive.business.region.dao.PostCodeEAO;
import com.blueskyminds.homebyfive.business.region.graph.PostalCode;
import com.blueskyminds.homebyfive.business.region.graph.Country;
import com.blueskyminds.homebyfive.business.region.graph.State;
import com.blueskyminds.homebyfive.business.region.graph.Suburb;
import com.blueskyminds.homebyfive.business.tag.Tag;

import java.util.Set;
import java.util.HashSet;
import java.util.Collection;

/**
 * Date Started: 3/03/2008
 * <p/>
 * History:
 */
public class PostCodeEAOTest extends JPATestCase {

    private static final String PERSISTENCE_UNIT = "TestEnterprisePersistenceUnit";

    private PostCodeEAO postCodeEAO;
    private static final String AU = "au";

    private Tag testTag;
    private Set<Tag> testTags;

    public PostCodeEAOTest() {
        super(PERSISTENCE_UNIT);
    }

    protected void setUp() throws Exception {
        super.setUp();
        postCodeEAO = new PostCodeEAO(em);

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

        PostalCode postCode2089 = new PostalCode(nsw, "2089").withTag(testTag);
        nsw.addPostCode(postCode2089);
        em.persist(nsw);
        Suburb neutralBay = new Suburb(nsw, postCode2089, "Neutral Bay");
        nsw.addSuburb(neutralBay);
        postCode2089.addSuburb(neutralBay);
        em.persist(nsw);
    }

    public void testLookupPostalCode() {
        PostalCode postCodeBean = postCodeEAO.lookup("/au/nsw/2089");
        assertNotNull(postCodeBean);
    }

     public void testListByTags() {
        Collection<PostalCode> postalCodes = postCodeEAO.listByTags(testTags);
        assertEquals(1, postalCodes.size());
    }

    public void testListByPathAndTags() {
        Collection<PostalCode> postalCodes = postCodeEAO.listByTags("/au/nsw", testTags);
        assertEquals(1, postalCodes.size());
    }
}