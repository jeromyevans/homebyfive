package com.blueskyminds.homebyfive.business.region;

import com.blueskyminds.homebyfive.business.AddressTestTools;
import com.blueskyminds.homebyfive.business.region.dao.RegionGraphDAO;
import com.blueskyminds.homebyfive.business.region.service.RegionGraphService;
import com.blueskyminds.homebyfive.business.region.service.RegionGraphServiceImpl;
import com.blueskyminds.homebyfive.business.region.graph.Region;
import com.blueskyminds.homebyfive.framework.core.test.JPATestCase;
import com.blueskyminds.homebyfive.framework.core.tools.DebugTools;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.Set;

/**
 * Date Started: 17/06/2007
 * <p/>
 * History:
 *  8 July 07 - modified to use RegionXDAO
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class TestRegionGraphService extends JPATestCase {

    private static final Log LOG = LogFactory.getLog(TestRegionGraphService.class);

    private static final String TEST_ENTERPRISE_PERSISTENCE_UNIT = "TestEnterprisePersistenceUnit";

    private RegionGraphDAO regionGraphDAO;
    private RegionGraphService regionGraphService;

    public TestRegionGraphService() {
        super(TEST_ENTERPRISE_PERSISTENCE_UNIT);
    }

    /**
     * Creates some reference data for the fixtures
     *
     * @throws Exception
     */
    protected void setUp() throws Exception {
        super.setUp();
        AddressTestTools.initialiseCountryList();
        regionGraphDAO = new RegionGraphDAO(em);
        regionGraphService = new RegionGraphServiceImpl(em, regionGraphDAO);
    }

    /** Tests the find methods of the region service */
    public void testLookupMethods() throws Exception {

        LOG.info("Searching for NSW...");
        Region nsw = regionGraphDAO.findRegionByName(TestRegionTools.NSW).iterator().next();

        assertNotNull(nsw);
        LOG.info("Found :"+nsw.getName());

        LOG.info("Matching "+TestRegionTools.NEUTRAL_BAY);
        List<Region> matches1 = regionGraphService.findRegion(nsw, TestRegionTools.NEUTRAL_BAY);

        assertNotNull(matches1);
        assertEquals(1, matches1.size());

        // test a minor spelling mistake
        LOG.info("Matching 'Neutrel Bay'");
        List<Region> matches2 = regionGraphService.findRegion(nsw, "Neutrel bay");

        assertNotNull(matches2);
        assertEquals(1, matches2.size());

        // test a gross spelling mistake
        LOG.info("Matching 'Neut bay'");
        List<Region> matches3 = regionGraphService.findRegion(nsw, "Neut bay");

        assertNotNull(matches3);
        assertEquals(0, matches3.size());
    }

    /**
     * Tests the recurive lookuip of ancestors of a region
     *
     * @throws Exception
     */
    public void testListAncestors() throws Exception {
        Region nsw = regionGraphDAO.findRegionByName(TestRegionTools.NSW).iterator().next();
        Region neutralBay = regionGraphService.findRegion(nsw, TestRegionTools.NEUTRAL_BAY).iterator().next();

        Set<Region> ancestors = regionGraphService.listAncestors(neutralBay);

        DebugTools.printCollection(ancestors);
        assertEquals(3, ancestors.size());
    }


    public void testAutocomplete() throws Exception {
        List<Region> regionHandles = regionGraphService.autocompleteRegion("Bull");
        assertNotNull(regionHandles);
        DebugTools.printCollection(regionHandles);
    }

}
