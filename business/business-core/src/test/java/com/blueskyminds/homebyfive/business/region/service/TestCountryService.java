package com.blueskyminds.homebyfive.business.region.service;

import com.blueskyminds.homebyfive.business.region.graph.Country;
import com.blueskyminds.homebyfive.business.region.Countries;
import com.blueskyminds.homebyfive.business.region.RegionTestCase;

import java.util.List;

/**
 * Date Started: 29/11/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class TestCountryService extends RegionTestCase {

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
}
