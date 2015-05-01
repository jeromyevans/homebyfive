package com.blueskyminds.homebyfive.business.region;

import com.blueskyminds.homebyfive.framework.core.test.JPATestCase;
import com.blueskyminds.homebyfive.business.region.dao.CountryEAO;
import com.blueskyminds.homebyfive.business.region.PathHelper;
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
public class CountryEAOTest extends JPATestCase {

    private static final String PERSISTENCE_UNIT = "TestEnterprisePersistenceUnit";

    private CountryEAO countryEAO;
    private static final String AU = "au";

    private Tag testTag;
    private Set<Tag> testTags;

    public CountryEAOTest() {
        super(PERSISTENCE_UNIT);
    }

    protected void setUp() throws Exception {
        super.setUp();
        countryEAO = new CountryEAO(em);

        testTag = new Tag("test");
        em.persist(testTag);
        testTags = new HashSet<Tag>();
        testTags.add(testTag);

        Country au = new Country("Australia", "AU").withTag(testTag);
        em.persist(au);
        Country us = new Country("United States of America", "US");
        em.persist(us);
    }

    public void testListCountries() {
        Set<Country> countries = countryEAO.list("/");
        assertEquals(2, countries.size());
    }

    public void testLookupCountry() {
        Country countryBean = countryEAO.lookup(PathHelper.buildPath(AU));
        assertNotNull(countryBean);
    }

    public void testListByTags() {
        Collection<Country> countries = countryEAO.listByTags(testTags);
        assertEquals(1, countries.size());
    }

    public void testListByPathAndTags() {
        Collection<Country> countries = countryEAO.listByTags("/", testTags);
        assertEquals(1, countries.size());
    }
}
