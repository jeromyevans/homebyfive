package com.blueskyminds.enterprise.region;

import com.blueskyminds.enterprise.AddressTestTools;
import com.blueskyminds.enterprise.region.dao.RegionGraphDAO;
import com.blueskyminds.enterprise.region.service.RegionGraphService;
import com.blueskyminds.enterprise.region.service.RegionGraphServiceImpl;
import com.blueskyminds.enterprise.region.RegionHandle;
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
public class TestRegionService  extends JPATestCase {

    private static final Log LOG = LogFactory.getLog(TestRegionDAO.class);

    private static final String TEST_ENTERPRISE_PERSISTENCE_UNIT = "TestEnterprisePersistenceUnit";

    private RegionGraphDAO regionGraphDAO;
    private RegionGraphService regionGraphService;

    public TestRegionService() {
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
        RegionHandle nsw = regionGraphDAO.findRegionByName(TestRegionTools.NSW).iterator().next();

        assertNotNull(nsw);
        LOG.info("Found :"+nsw.getName());

        LOG.info("Matching "+TestRegionTools.NEUTRAL_BAY);
        List<RegionHandle> matches1 = regionGraphService.findRegion(nsw, TestRegionTools.NEUTRAL_BAY);

        assertNotNull(matches1);
        assertEquals(1, matches1.size());

        // test a minor spelling mistake
        LOG.info("Matching 'Neutrel Bay'");
        List<RegionHandle> matches2 = regionGraphService.findRegion(nsw, "Neutrel bay");

        assertNotNull(matches2);
        assertEquals(1, matches2.size());

        // test a gross spelling mistake
        LOG.info("Matching 'Neut bay'");
        List<RegionHandle> matches3 = regionGraphService.findRegion(nsw, "Neut bay");

        assertNotNull(matches3);
        assertEquals(0, matches3.size());
    }

    /**
     * Tests the recurive lookuip of ancestors of a region
     *
     * @throws Exception
     */
    public void testListAncestors() throws Exception {
        RegionHandle nsw = regionGraphDAO.findRegionByName(TestRegionTools.NSW).iterator().next();
        RegionHandle neutralBay = regionGraphService.findRegion(nsw, TestRegionTools.NEUTRAL_BAY).iterator().next();

        Set<RegionHandle> ancestors = regionGraphService.listAncestors(neutralBay);

        DebugTools.printCollection(ancestors);
        assertEquals(3, ancestors.size());
    }
   
}
