package com.blueskyminds.homebyfive.business.region.service;

import com.blueskyminds.homebyfive.business.region.graph.Country;
import com.blueskyminds.homebyfive.business.region.Countries;
import com.blueskyminds.homebyfive.business.region.RegionTestCase;
import com.blueskyminds.homebyfive.business.region.composite.RegionComposite;
import com.blueskyminds.homebyfive.business.region.group.RegionGroup;
import com.blueskyminds.homebyfive.business.tag.expression.TagExpression;

import java.util.List;
import java.util.Set;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * Date Started: 29/11/2008
 * <p/>
 * Copyright (c) 2009 Blue Sky Minds Pty Ltd
 */
public class CountryServiceTest extends RegionTestCase {

    private static final Log LOG = LogFactory.getLog(CountryServiceTest.class);

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Country aus = countryService.lookup("/au");
        aus.addTag(a);
        em.persist(aus);

        Country us = countryService.lookup("/us");
        us.addTag(b);
        em.persist(us);
    }

    public void testFindCountry() throws Exception {
        List<Country> countries1 = countryService.find("Australia");
        assertNotNull(countries1);
        assertEquals(1, countries1.size());
        Country x = countries1.get(0);
        assertEquals("Australia", x.getName());

        List<Country> countries2 = countryService.find(Countries.AU);
        assertNotNull(countries2);
        assertEquals(1, countries2.size());
        assertEquals("Australia", countries2.iterator().next().getName());

        List<Country> countries3 = countryService.find("Australa");
        assertNotNull(countries3);
        assertEquals(2, countries3.size());
        assertEquals("Australia", countries3.iterator().next().getName());
    }

    public void testListCountries() throws Exception {
        RegionGroup countries = countryService.list();
        
        for (RegionComposite composite : countries.getRegions()) {
            LOG.info(composite);
        }
    }

    public void testListByTags() throws Exception {
        TagExpression aOrB = tagExpressionFactory.evaluate("a or b");

        Set<Country> regions = countryService.listByTags(aOrB);
        assertEquals(2, regions.size());
    }
}
