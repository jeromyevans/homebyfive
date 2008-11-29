package com.blueskyminds.homebyfive.business.region.service;

import com.blueskyminds.homebyfive.business.region.graph.Suburb;
import com.blueskyminds.homebyfive.business.region.Countries;
import com.blueskyminds.homebyfive.business.region.RegionTestCase;
import com.blueskyminds.homebyfive.framework.core.tools.DebugTools;

import java.util.List;

/**
 * Date Started: 29/11/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class TestSuburbService extends RegionTestCase {

    public void testLookup() throws Exception {
        Suburb suburb = suburbService.lookup("/au/wa/armadale");
        assertNotNull(suburb);
    }

    public void testFindSuburb() throws Exception {

        List<Suburb> suburbs1 = suburbService.find("Neutral Bay", Countries.AU);
        assertNotNull(suburbs1);
        assertEquals(1, suburbs1.size());
        assertEquals("Neutral Bay", suburbs1.get(0).getName());
    }

    /**
     * Tests the lookup of a suburb by name
     */
    public void testSuburbSearch() throws Exception {
        List<Suburb> sub1 = suburbService.find("Neutral Bay", Countries.AU);
        List<Suburb> sub2 = suburbService.find("NeutralBay", Countries.AU);
        List<Suburb> sub3 = suburbService.find("Neutrel Bay", Countries.AU);

        assertEquals(1, sub1.size());
        assertEquals(1, sub2.size());
        assertEquals(1, sub3.size());

        // try some places that aren't unique names
        List<Suburb> sub4 = suburbService.find("Aberdeen", Countries.AU);
        List<Suburb> sub5 = suburbService.find("Aberdean", Countries.AU);
        List<Suburb> sub6 = suburbService.find("Aberden", Countries.AU);

        DebugTools.printCollection(sub6);
        assertEquals(2, sub4.size());
        assertEquals(2, sub5.size());
        assertEquals(3, sub6.size());   // two aberdeens and aberdare


    }
}
